package se.mah.ae3317.personalfinanceapp;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private MainController controller;
    private EditText editFirstName;
    private EditText editSurname;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        setController();
        initComponents();
        checkRegister();

        if (bundle == null) {
            controller.setFragment(new OverallFragment());
            navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    private void setController() {
        controller = new MainController(this);
    }

    private void initComponents() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawerOpen, R.string.drawerClose);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    private void checkRegister() {
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        String name = prefs.getString("name", null);
        String surname = prefs.getString("surname", null);
        if (name != null && surname != null) {
            View viewTemp = navigationView.getHeaderView(0);
            TextView navHeader = (TextView) viewTemp.findViewById(R.id.navHeader);
            navHeader.setTextSize(20);
            navHeader.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorWhite));
            navHeader.setText("User: " + name + " " + surname);
        }
        else {
            register();
        }
    }

    private void register() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Welcome please enter:");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        editFirstName = new EditText(this);
        editFirstName.setHint("First name");
        editFirstName.setSingleLine();
        layout.addView(editFirstName);

        editSurname = new EditText(this);
        editSurname.setHint("Surname");
        editSurname.setSingleLine();
        layout.addView(editSurname);

        dialog.setView(layout);
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editFirstName.getText().toString().trim();
                String surname = editSurname.getText().toString().trim();
                SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                editor.putString("name", name);
                editor.putString("surname", surname);
                editor.commit();
                checkRegister(); // Has to change the navHeader's text
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment;

        if (id == R.id.nav_overall) {
            fragment = new OverallFragment();
            controller.setFragment(fragment);
        }
        else if (id == R.id.nav_show_income) {
            States.setCurrentState(States.STATE_iNCOME);
            controller.showList();
        }
        else if (id == R.id.nav_show_expenses) {
            States.setCurrentState(States.STATE_EXPENSE);
            controller.showList();
        }
        else if (id == R.id.nav_add_income) {
            States.setCurrentState(States.STATE_iNCOME);
            fragment = new AddFragment();
            controller.setFragment(fragment);
        }
        else if (id == R.id.nav_add_expense) {
            States.setCurrentState(States.STATE_EXPENSE);
            fragment = new AddFragment();
            controller.setFragment(fragment);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
