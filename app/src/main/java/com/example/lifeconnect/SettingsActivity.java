package com.example.lifeconnect;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {

    private SharedPreferences settings;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    private EditTextPreference edtEmail;
    private EditTextPreference edtPassword;
    private EditTextPreference edtFirstName;
    private EditTextPreference edtLastName;
    private ListPreference lstGender;
    private EditTextPreference edtAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);
        addPreferencesFromResource(R.xml.preferences);

        edtEmail = (EditTextPreference) findPreference("Email");
        edtPassword = (EditTextPreference) findPreference("Password");
        edtFirstName = (EditTextPreference) findPreference("FirstName");
        edtLastName = (EditTextPreference) findPreference("LastName");
        lstGender = (ListPreference) findPreference("Gender");
        edtAge = (EditTextPreference) findPreference("Age");

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

                switch (key) {
                    case "Email":
                        if( settings.contains("Email")) {
                            edtEmail.setSummary(settings.getString("Email", ""));
                        }
                        break;
                    case "Password":
                        if( settings.contains("Password")) {
                            String password = settings.getString("Password", "");
                            String summary = "";
                            for (int i = 0; i < password.length(); i++) {
                                summary += '*';
                            }
                            edtPassword.setSummary(summary);
                        }
                        break;
                    case "FirstName":
                        if( settings.contains("FirstName")) {
                            edtFirstName.setSummary(settings.getString("FirstName", ""));
                        }
                        break;
                    case "LastName":
                        if( settings.contains("LastName")) {
                            edtLastName.setSummary(settings.getString("LastName", ""));
                        }
                        break;
                    case "Gender":
                        if( settings.contains("Gender")) {
                            lstGender.setSummary(settings.getString("Gender", ""));
                        }
                        break;
                    case "Age":
                        if( settings.contains("Age")) {
                            edtAge.setSummary(settings.getString("Age", ""));
                        }
                        break;
                }
            }
        };
        settings.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();

        if( settings.contains("Email")) {
            edtEmail.setSummary(settings.getString("Email", ""));
        }
        if( settings.contains("Password")) {
            edtPassword.setSummary(settings.getString("Password", ""));
        }
        if( settings.contains("FirstName")) {
            edtFirstName.setSummary(settings.getString("FirstName", ""));
        }
        if( settings.contains("LastName")) {
            edtLastName.setSummary(settings.getString("LastName", ""));
        }
        if( settings.contains("Gender")) {
            lstGender.setSummary(settings.getString("Gender", ""));
        }
        if( settings.contains("Age")) {
            edtAge.setSummary(settings.getString("Age", ""));
        }
    }
}
