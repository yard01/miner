package com.github.yard01.sandbox.lib_yahooweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class YahooFragment extends PreferenceFragmentCompat {
    SharedPreferences preferences;
    SharedPreferences.OnSharedPreferenceChangeListener changeListener;

    public void createContent() {
        Preference button = findPreference(getString(R.string.yahoo_key_request_button));

        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //code for what you want it to do
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(YahooFragment.this.getString(R.string.yahoo_server_sign_up)));
                startActivity(i);
                return true;
            }
        });


        button = findPreference(getString(R.string.yahoo_reset_button));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //preferences = PreferenceManager.getDefaultSharedPreferences( YahooFragment.this.getActivity() );
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(getString(R.string.yahoo_appid_textview));// .putString()
                editor.remove(getString(R.string.yahoo_clientid_textview));// .putString()
                editor.remove(getString(R.string.yahoo_clientsecret_textview));// .putString()
                editor.commit();
                //createContent();
                return true;
            }
        });


         changeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                          String key) {
                        // your stuff here
                        createContent();
                    }
         };

        preferences.registerOnSharedPreferenceChangeListener(changeListener);

        Preference appid = findPreference(getString(R.string.yahoo_appid_textview));
        if ("".equals(preferences.getString(getString(R.string.yahoo_appid_textview), ""))) appid.setSummary(""); else appid.setSummary(R.string.yahoo_already_adjusted);

        Preference clientid = findPreference(getString(R.string.yahoo_clientid_textview));
        if ("".equals(preferences.getString(getString(R.string.yahoo_clientid_textview), ""))) clientid.setSummary(""); else clientid.setSummary(R.string.yahoo_already_adjusted);

        Preference clientsecret = findPreference(getString(R.string.yahoo_clientsecret_textview));
        if ("".equals(preferences.getString(getString(R.string.yahoo_clientsecret_textview), "")))
            clientsecret.setSummary(""); else clientsecret.setSummary(R.string.yahoo_already_adjusted);

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.yahoo_preferences, rootKey);
        preferences = PreferenceManager.getDefaultSharedPreferences( this.getActivity() );

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
