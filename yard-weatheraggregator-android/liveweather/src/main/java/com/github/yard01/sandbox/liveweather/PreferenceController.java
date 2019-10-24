package com.github.yard01.sandbox.liveweather;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.yard01.sandbox.weatherforecast.WeatherForecastProvider;
import com.github.yard01.sandbox.weatherforecast.WeatherForecastProviderFactory;
import com.github.yard01.sandbox.weatherforecast.viewmodel.WeatherForecastViewModel;

import java.util.Random;

import io.reactivex.Observable;

public class PreferenceController {

    private static String getRandomWeatherForecastProviderFactoryName(Context context) {
        String names[] = context.getResources().getStringArray(R.array.forecastfactoryclasses);
        return names[(new Random(System.currentTimeMillis())).nextInt(names.length)];
    }

    public static void loadPreferences(Context context) {

    }

    public static WeatherForecastProviderFactory createWeatherForecastProviderFactory(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String factoryClassName = prefs.getString(WeatherForecastProviderFactory.FORECASTFACTORY_PREFERENCE, "");

        if ("".equals(factoryClassName)) {
            factoryClassName = getRandomWeatherForecastProviderFactoryName(context);
            prefs.edit().putString(WeatherForecastProviderFactory.FORECASTFACTORY_PREFERENCE, factoryClassName).commit();

        }
        return WeatherForecastProviderFactory.buildFactory(context, factoryClassName);

    }

    public static WeatherForecastProvider createWeatherForecastProvider(Context context) {

        WeatherForecastProviderFactory factory = createWeatherForecastProviderFactory(context);

        try {
            WeatherForecastProvider currentWeatherForecastProvider = factory.getWeatherForecastProvider();
            return currentWeatherForecastProvider;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
