package uni.mobile.mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    //Button Variable
    private Button buttonDayMode;
    private Button buttonNightMode;
    private Button buttonAutoMode;
    private Button buttonBatteryMode;

    private SharedPreferences preferenceManager;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        initListeners();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) buttonBatteryMode.setVisibility(View.GONE);
        else buttonAutoMode.setVisibility(View.GONE);

        preferenceManager = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferenceManager.edit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                Intent intentProfile = new Intent(SettingsActivity.this, HomeActivity.class);
                                startActivity(intentProfile);
                                finish();
                                break;
                            case R.id.action_profile:
                                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
                                finish();
                                break;
                            case R.id.action_settings:
                                break;
                        }
                        return true;
                    }
                });
    }

    /**
     * method to initialize the views
     */
    private void initViews() {
        buttonDayMode = (Button) findViewById(R.id.buttonDayMode);
        buttonNightMode = (Button) findViewById(R.id.buttonNightMode);
        buttonAutoMode = (Button) findViewById(R.id.buttonAutoMode);
        buttonBatteryMode = (Button) findViewById(R.id.buttonBatteryMode);

    }

    /**
     * method to initialize the listeners
     */
    private void initListeners() {
        buttonAutoMode.setOnClickListener(this);
        buttonDayMode.setOnClickListener(this);
        buttonNightMode.setOnClickListener(this);
    }

    /**
     * onClick Listener to capture button click
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonDayMode:
                editor.putInt("Theme", AppCompatDelegate.MODE_NIGHT_NO);
                editor.commit();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case R.id.buttonNightMode:
                editor.putInt("Theme", AppCompatDelegate.MODE_NIGHT_YES);
                editor.commit();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case R.id.buttonAutoMode:
                editor.putInt("Theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                editor.commit();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case R.id.buttonBatteryMode:
                editor.putInt("Theme", AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                editor.commit();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                break;
        }

    }
}
