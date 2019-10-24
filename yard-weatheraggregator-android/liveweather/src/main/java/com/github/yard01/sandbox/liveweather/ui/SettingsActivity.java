package com.github.yard01.sandbox.liveweather.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.github.yard01.sandbox.liveweather.R;
import com.github.yard01.sandbox.weatherforecast.WeatherForecastProvider;
import com.github.yard01.sandbox.weatherforecast.WeatherForecastProviderFactory;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    private WeatherForecastProviderFactory currentWeatherForecastProviderFactory;
    private WeatherForecastProvider currentWeatherForecastProvider;

    private Spinner serverSelectorSpinner;
    private WeatherServerSelectorAdapter serverSelectorAdapter;

    private void viewPreferences(String preferencesFragmentClassName) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) Class.forName(preferencesFragmentClassName
            ).newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            //fragment = new ItemDetailFragment();
        }
        this.getSupportFragmentManager().beginTransaction()  //
                .replace(R.id.weather_server_fragment_place, fragment)          // подменяем содержмое контейнера
                .commit();
        /////////////////////////////////////////////////////////////////////////
    }

//    private void openWallpaperManager() {
//        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
//        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
//                new ComponentName(SettingsActivity.this, WeatherWallpaperService.class));
//        startActivity(intent);
//    }

    public void createContent() {

        serverSelectorAdapter = new WeatherServerSelectorAdapter(this);

        serverSelectorSpinner = findViewById(R.id.spinner_weatherserver_selector);

        serverSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { //сработает для первого (с номером 0) в списке элемента
                currentWeatherForecastProviderFactory = serverSelectorAdapter.getData(position); // если ранее не был установлен выделенный элемент
                viewPreferences(currentWeatherForecastProviderFactory.getFragmentClassName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        serverSelectorSpinner.setAdapter(serverSelectorAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences( this );
        createContent();
    }

    private void loadPreferences() {
        String factoryClassName = preferences.getString(WeatherForecastProviderFactory.FORECASTFACTORY_PREFERENCE, "");
        if (!"".equals(factoryClassName)) {
            currentWeatherForecastProviderFactory = WeatherForecastProviderFactory.buildFactory(this,factoryClassName);
        }
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(WeatherForecastProviderFactory.FORECASTFACTORY_PREFERENCE, currentWeatherForecastProviderFactory.getClass().getName()).commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPreferences();
        if (currentWeatherForecastProviderFactory != null)
            serverSelectorSpinner.setSelection(serverSelectorAdapter.getPosition(currentWeatherForecastProviderFactory.getClass().getName()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePreferences();
        finish();
    }
}
