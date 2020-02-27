package uni.mobile.mobileapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import uni.mobile.mobileapp.MainActivity;
import uni.mobile.mobileapp.R;


public class SettingsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RadioGroup radioTheme;
    private RadioButton radioDay, radioNight, radioAuto, radioBattery;
    private SwitchMaterial useBiometricsSwitch;

    private SharedPreferences preferenceManager;
    private SharedPreferences.Editor editor;

    public static Fragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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

        radioTheme = view.findViewById(R.id.radioTheme);
        radioDay = view.findViewById(R.id.radioDay);
        radioNight = view.findViewById(R.id.radioNight);
        radioAuto = view.findViewById(R.id.radioAuto);
        radioBattery = view.findViewById(R.id.radioBattery);

        useBiometricsSwitch = view.findViewById(R.id.useBiometricsSwitch);

        radioTheme.check(preferenceManager.getInt("radioThemeId", R.id.radioDay));
        useBiometricsSwitch.setChecked(preferenceManager.getBoolean("UseBiometrics", false));

        radioTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case R.id.radioDay:
                        editor.putInt("Theme", AppCompatDelegate.MODE_NIGHT_NO);
                        editor.commit();
                        editor.putInt("radioThemeId", R.id.radioDay);
                        editor.commit();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        break;
                    case R.id.radioNight:
                        editor.putInt("Theme", AppCompatDelegate.MODE_NIGHT_YES);
                        editor.commit();
                        editor.putInt("radioThemeId", R.id.radioNight);
                        editor.commit();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        break;
                    case R.id.radioAuto:
                        editor.putInt("Theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                        editor.commit();
                        editor.putInt("radioThemeId", R.id.radioAuto);
                        editor.commit();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                        break;
                    case R.id.radioBattery:
                        editor.putInt("Theme", AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                        editor.commit();
                        editor.putInt("radioThemeId", R.id.radioBattery);
                        editor.commit();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                        break;
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) radioBattery.setVisibility(View.GONE);
        else radioAuto.setVisibility(View.GONE);

        useBiometricsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    editor.putBoolean("UseBiometrics", true);
                    editor.commit();
                }
                else {
                    editor.putBoolean("UseBiometrics", false);
                    editor.commit();
                }
            }
        });
    }

    private void restartApp() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        this.startActivity(intent);
        getActivity().finish();
    }

}
