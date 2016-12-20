package se.mah.ae3317.weatherexplorer;

import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by xisth on 2016-11-31.
 */
public class Controller {
    private MainActivity activity;

    public Controller(MainActivity activity) {
        this.activity = activity;
    }

    public void showRegister() {
        Intent i = new Intent(activity, RegisterActivity.class);
        activity.startActivity(i);
    }

    public void addMarker(LatLng latLng, String country, String place, String weather, int temperature) {
        activity.addMarker(latLng, country, place, weather, temperature);
    }

    public void executeWeatherTask(LatLng latLng) {
        WeatherTask weatherTask = new WeatherTask(this, latLng);
        weatherTask.execute("http://api.openweathermap.org/data/2.5/weather?lat=" + latLng.latitude +
                "&lon=" + latLng.longitude + "&appid=87fc0016991681e9631f03ae29545bd3");
    }

    public int convertToC(double kalvin) {
        return (int) (kalvin -  273.15); // [°C] = [K] − 273.15
    }

    public int convertToF(double kalvin) {
        return (int) ((kalvin * 1.8) - 459.67); // [F] = ([K] * 1.8) − 459.67
    }
}