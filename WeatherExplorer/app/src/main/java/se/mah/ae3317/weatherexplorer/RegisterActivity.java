package se.mah.ae3317.weatherexplorer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by xisth on 2016-11-31.
 */
public class RegisterActivity extends AppCompatActivity {

    private RegisterController registerController;
    private EditText editName;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editName = (EditText) findViewById(R.id.registerName);
        registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new ButtonListener());

        registerController = new RegisterController(this);

        if (!editName.equals("")) {
            registerButton.setText(getString(R.string.loginButton));
        }
    }

    public void setName(String name) {
        editName.setText(name);
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            registerController.Register(editName.getText().toString());
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerController.saveName(editName.getText().toString());
    }
}
