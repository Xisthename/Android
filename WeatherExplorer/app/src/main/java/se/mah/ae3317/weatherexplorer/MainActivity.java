package se.mah.ae3317.weatherexplorer;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/*
* @startuml
* @enduml
* */

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private boolean deselectMarker = false;
    private Controller controller;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        controller = new Controller(this);
        setupTabs();

        if (bundle == null) {
            controller.showRegister();
        }
    }

    private void setupTabs() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec(getString(R.string.tabMap));
        tabSpec.setContent(R.id.tabMap);
        tabSpec.setIndicator(getString(R.string.tabMap));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(getString(R.string.tabInfo));
        tabSpec.setContent(R.id.tabGroup);
        tabSpec.setIndicator(getString(R.string.tabInfo));
        tabHost.addTab(tabSpec);

        for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
            TextView textView = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGray));
        }
        ViewGroup view = (ViewGroup) ((ViewGroup) this.findViewById(R.id.tabGroup)).getChildAt(0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (deselectMarker == false) {
                    controller.executeWeatherTask(latLng);
                }
                else {
                    deselectMarker = false;
                }
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                deselectMarker = true;
                return false;
            }
        });

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getBaseContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getBaseContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getBaseContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
    }

    public void addMarker(LatLng latLng, String country, String place, String weather, int temperature) {
        Marker marker = map.addMarker(new MarkerOptions().position(latLng).title("Country: " + country).snippet("Place: " + place + "\n" +
                "Weather: " + weather + "\n" + "Temperature: " + temperature + "°C"));
        marker.showInfoWindow();
        deselectMarker = true;
    }

    @Override
    public void onBackPressed() {
        controller.showRegister();
    }
}
