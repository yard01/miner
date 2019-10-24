package com.github.yard01.sandbox.lib_openweathermap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;


public class OWMFragment extends PreferenceFragmentCompat {
    SharedPreferences preferences;
    SharedPreferences.OnSharedPreferenceChangeListener changeListener;

    public void createContent() {
        Preference button = findPreference(getString(R.string.openweathermap_apikey_request_button));

        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //code for what you want it to do
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(OWMFragment.this.getString(R.string.openweathermap_server_sign_up)));
                startActivity(i);
                return true;
            }
        });


        button = findPreference(getString(R.string.openweathermap_reset_button));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(getString(R.string.openweathermap_apikey_textview)).commit();// .putString()
                createContent();
                return true;
            }
        });

        Preference apikey = findPreference(getString(R.string.openweathermap_apikey_textview));

        changeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                  String key) {
                // your stuff here
                createContent();
            }
        };
        preferences.registerOnSharedPreferenceChangeListener(changeListener);


        apikey.setSummary(preferences.getString(getString(R.string.openweathermap_apikey_textview), ""));
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.openweathermap_preferences, rootKey);
        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        createContent();
    }

    @Override
    public void onPause() {
        super.onPause();
        preferences.unregisterOnSharedPreferenceChangeListener(changeListener);
    }
}