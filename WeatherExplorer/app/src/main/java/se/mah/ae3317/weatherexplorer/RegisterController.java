package se.mah.ae3317.weatherexplorer;

import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by xisth on 2016-11-31.
 */
public class RegisterController {

    RegisterActivity activity;

    public RegisterController(RegisterActivity activity) {
        this.activity = activity;
        SharedPreferences preferences = activity.getSharedPreferences(activity.getString(R.string.userKey), activity.MODE_PRIVATE);
        String name = preferences.getString(activity.getString(R.string.userName), "");
        activity.setName(name);
    }

    public void Register(String name) {
        if (name.length() > 3) {
            saveName(name);
            activity.finish();
        }
        else {
            Toast.makeText(activity, activity.getString(R.string.tooShort), Toast.LENGTH_LONG).show();
        }
    }

    public void saveName(String name) {
        SharedPreferences preferences = activity.getSharedPreferences(activity.getString(R.string.userKey), activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(activity.getString(R.string.userName), name);
        editor.commit();
    }
}
