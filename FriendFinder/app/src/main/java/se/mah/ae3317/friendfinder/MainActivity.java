package se.mah.ae3317.friendfinder;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int REQUEST_ACCESS_FINE_LOCATION = 1;
    private TabHost tabHost;
    private GoogleMap map;
    private Controller controller;
    private List<MapMarker> mapMarkers;
    private GPSListener gpsListener;
    private final String defaultLanguage = "en"; // the default language is english
    private final String splitName = ":";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SharedPreferences preferences = getSharedPreferences(getString(R.string.languageKey), MainActivity.MODE_PRIVATE);
        String currentLanguage = preferences.getString(getString(R.string.currentLanguage), "");
        Log.d("Load language: ", currentLanguage);

        if (currentLanguage.equals("")) {
            setLanguage(defaultLanguage);
        }
        else {
            setLanguage(currentLanguage);
        }
        setContentView(R.layout.activity_main);
        setupMap();
        controller = new Controller(this, gpsListener, bundle);
        setupTabs();

        if (bundle == null) {
            controller.showRegister();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void setLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private void setupMap() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gpsListener = new GPSListener();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, gpsListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, gpsListener);
        }
        mapMarkers = new ArrayList<>();
    }

    private void setupTabs() {
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec(getString(R.string.tabMap));
        tabSpec.setContent(R.id.tabMap);
        tabSpec.setIndicator(getString(R.string.tabMap));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(getString(R.string.tabGroup));
        tabSpec.setContent(R.id.tabGroup);
        tabSpec.setIndicator(getString(R.string.tabGroup));
        tabHost.addTab(tabSpec);

        for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
            TextView textView = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGray));
        }
        ViewGroup view = (ViewGroup) ((ViewGroup) this.findViewById(R.id.tabGroup)).getChildAt(0);
        new GroupFragment(view, controller);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        if (controller != null) {
            return controller;
        }
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        controller.onSaveInstanceState(bundle);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onDestroy() {
        controller.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        controller.showRegister();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private MapMarker getMarker(String name, String groupName) {
        for (MapMarker mapMarker : mapMarkers) {
            if (mapMarker.getMarker().getTitle().equals(name)) {
                Log.d("Found a marker: ", mapMarker.getMarker() + "");
                return mapMarker;
            }
            else if (mapMarker.getGroupName().equals(groupName)) {
                String[] tempSplit = mapMarker.getMarker().getTitle().split(splitName);
                if (tempSplit[1].equals(name)) {
                    return mapMarker;
                }
            }
        }
        return null;
    }

    private MapMarker addMarker(String name, String groupName, String latitude, String longitude) {
        MarkerOptions m = new MarkerOptions().position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
        if (name.equals(controller.getGroupHandler().getName())) {
            m.title(getString(R.string.me) + splitName + name);
            m.icon(BitmapDescriptorFactory.fromResource(R.drawable.me));
        }
        else {
            m.title(name);
            m.icon(BitmapDescriptorFactory.fromResource(R.drawable.friend));
        }
        MapMarker mapMarker = new MapMarker(map.addMarker(m), groupName);
        mapMarkers.add(mapMarker);
        return mapMarker;
    }

    public void updateMaker(String userName, String groupName, String latitude, String longitude) {
        MapMarker mapMarker = getMarker(userName, groupName);

        if (mapMarker == null) {
            mapMarker = addMarker(userName, groupName, latitude, longitude);
        }

        mapMarker.getMarker().setPosition(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
        Log.d("MAKERER UPDATE: ", mapMarker + "");
    }

    public void removeMarkers(String groupName) {
        List<MapMarker> removeList = new ArrayList<>();
        for (MapMarker mapMarker : mapMarkers) {
            if (mapMarker.getGroupName().equals(groupName)) {
                Log.d("MAKERER REMOVE!: ", mapMarker.getMarker().toString() + "");
                removeList.add(mapMarker);
                mapMarker.getMarker().remove();
            }
        }
        for (MapMarker marker : removeList) {
            mapMarkers.remove(marker);
        }
        removeList.clear();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }


    //added a THUMBNAIL if i want to continue my work later on

   /*private static final int THUMBNAIL = 2;
    private boolean mapReady;
    private LatLng location;
    private com.google.android.gms.maps.model.Marker marker;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        mapReady = true;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                location = latLng;

                if (mapReady) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, THUMBNAIL);
                    }
                }
                else {
                    Log.d("Thumb", "mapReady = false");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == THUMBNAIL && resultCode == Activity.RESULT_OK) {
            Bitmap thumbnail = data.getParcelableExtra("data");
            com.google.android.gms.maps.model.Marker marker = map.addMarker(new MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.fromBitmap(thumbnail)));
        }
    }*/
}
