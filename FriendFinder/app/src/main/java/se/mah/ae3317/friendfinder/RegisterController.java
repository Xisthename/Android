package se.mah.ae3317.friendfinder;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by xisth on 2016-10-07.
 */
public class RegisterController {

    RegisterActivity activity;

    public RegisterController(RegisterActivity activity) {
        this.activity = activity;
        SharedPreferences preferences = activity.getSharedPreferences(activity.getString(R.string.userKey), MainActivity.MODE_PRIVATE);
        String name = preferences.getString(activity.getString(R.string.userName), "");
        activity.setName(name);
    }

    public void Register(String name) {
        if (name.length() > 3) {
            saveLanguage();
            saveName(name);
            GroupHandler.name = name;
            activity.finish();
        }
        else {
            Toast.makeText(activity, activity.getString(R.string.tooShort), Toast.LENGTH_LONG).show();
        }
    }

    public void makeHint() {
        Toast.makeText(activity, R.string.hintLanguage, Toast.LENGTH_LONG).show();
    }

    public void saveName(String name) {
        SharedPreferences preferences = activity.getSharedPreferences(activity.getString(R.string.userKey), MainActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(activity.getString(R.string.userName), name);
        editor.commit();
    }

    public void saveLanguage() {
        Log.d("Save Language: ", activity.getLanguage());
        SharedPreferences preferences = activity.getSharedPreferences(activity.getString(R.string.languageKey), MainActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(activity.getString(R.string.currentLanguage), activity.getLanguage());
        editor.commit();
    }
}
