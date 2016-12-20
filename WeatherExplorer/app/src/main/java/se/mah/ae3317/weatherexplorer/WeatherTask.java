package se.mah.ae3317.weatherexplorer;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

/**
 * Created by xisth on 2016-11-31.
 */
public class WeatherTask extends AsyncTask<String, Void, String> {
    private Controller controller;
    private LatLng latLng;
    private boolean celsius = true;

    public WeatherTask(Controller controller, LatLng latLng) {
        this.controller = controller;
        this.latLng = latLng;
    }

    @Override
    protected String doInBackground(String... urls) {
        String result = "";
        URL url;
        HttpURLConnection urlConnection;

        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream input = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);

            int data = reader.read();

            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            JSONObject jsonObject = new JSONObject(result);

            JSONArray weatherData = new JSONArray(jsonObject.getString("weather"));

            String weather = weatherData.getJSONObject(0).getString("description");

            JSONObject sys = new JSONObject(jsonObject.getString("sys"));

            String countryCode = sys.getString("country");

            Locale locale = new Locale("", countryCode);
            String country = locale.getDisplayCountry();

            String place = jsonObject.getString("name");

            JSONObject tempData = new JSONObject(jsonObject.getString("main"));

            double kalvin = Double.parseDouble(tempData.getString("temp"));

            int temperature;

            if (celsius) {
                temperature = controller.convertToC(kalvin);
            }
            else {
                temperature = controller.convertToF(kalvin);
            }
            controller.addMarker(latLng, country, place, weather, temperature);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
