package se.mah.ae3317.friendfinder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * Created by xisth on 2016-10-07.
 */
public class RegisterActivity extends AppCompatActivity {

    private RegisterController registerController;
    private EditText editName;
    private Button registerButton;
    private RadioButton englishButton;
    private RadioButton swedishButton;
    private final String english = "en";
    private final String swedish = "sv";
    private String language = english;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editName = (EditText) findViewById(R.id.registerName);
        englishButton = (RadioButton) findViewById(R.id.englishButton);
        swedishButton = (RadioButton) findViewById(R.id.swedishButton);
        registerButton = (Button) findViewById(R.id.registerButton);

        englishButton.setOnClickListener(new ButtonListener());
        swedishButton.setOnClickListener(new ButtonListener());
        registerButton.setOnClickListener(new ButtonListener());

        SharedPreferences preferences = getSharedPreferences(getString(R.string.languageKey), MainActivity.MODE_PRIVATE);
        String loadLanguage = preferences.getString(getString(R.string.currentLanguage), "");

        if (!loadLanguage.equals("")) {
            registerButton.setText(getString(R.string.loginButton));
            setLanguage(loadLanguage);
        }

        if (language.equals(english)) {
            englishButton.setChecked(true);
        }
        else {
            swedishButton.setChecked(true);
        }
        registerController = new RegisterController(this);
    }

    public void setName(String name) {
        editName.setText(name);
    }

    private void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.englishButton:
                    setLanguage(english);
                    registerController.makeHint();
                    break;
                case R.id.swedishButton:
                    setLanguage(swedish);
                    registerController.makeHint();
                    break;
                case R.id.registerButton:
                    registerController.Register(editName.getText().toString());
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerController.saveLanguage();
        registerController.saveName(editName.getText().toString());
    }
}
