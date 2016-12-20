package se.mah.ae3317.friendfinder;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by xisth on 2016-10-04.
 */
public class GPSListener implements LocationListener {

    private Location location;
    private double latitude;
    private double longitude;

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
        setLocation(location);
        Log.d(this.location.getLatitude() + "", this.location.getLongitude() + "");
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
