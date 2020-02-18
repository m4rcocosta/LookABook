package uni.mobile.mobileapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    //Button Variable
    private Button buttonDayMode;
    private Button buttonNightMode;
    private Button buttonAutoMode;
    private Button buttonBatteryMode;

    private SharedPreferences preferenceManager;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        preferenceManager = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferenceManager.edit();

        buttonDayMode = (Button) view.findViewById(R.id.buttonDayMode);
        buttonNightMode = (Button) view.findViewById(R.id.buttonNightMode);
        buttonAutoMode = (Button) view.findViewById(R.id.buttonAutoMode);
        buttonBatteryMode = (Button) view.findViewById(R.id.buttonBatteryMode);

        buttonDayMode.setOnClickListener(this);
        buttonNightMode.setOnClickListener(this);
        buttonAutoMode.setOnClickListener(this);
        buttonBatteryMode.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) buttonBatteryMode.setVisibility(View.GONE);
        else buttonAutoMode.setVisibility(View.GONE);
    }

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
