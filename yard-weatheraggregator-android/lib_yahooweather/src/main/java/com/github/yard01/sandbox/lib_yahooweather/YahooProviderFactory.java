package com.github.yard01.sandbox.lib_yahooweather;

import android.content.Context;

import com.github.yard01.sandbox.weatherforecast.WeatherForecastProvider;
import com.github.yard01.sandbox.weatherforecast.WeatherForecastProviderFactory;

import java.lang.reflect.InvocationTargetException;

public class YahooProviderFactory extends WeatherForecastProviderFactory {

    WeatherForecastProvider forecastProvider;
    private static final String providerClassName = "com.github.yard01.sandbox.lib_yahooweather.YahooProvider";
    private static final String fragmentClassName = "com.github.yard01.sandbox.lib_yahooweather.YahooFragment";

    public YahooProviderFactory(Context _context) {
        super(_context);
    }

    @Override
    public WeatherForecastProvider getWeatherForecastProvider() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (forecastProvider == null)
            forecastProvider = (WeatherForecastProvider)Class.forName(providerClassName).getConstructor(Context.class).newInstance(context);
        return forecastProvider;
    }

    @Override
    public String getFactoryName() {
        return context.getString(R.string.yahoo_server_name);
    }

    @Override
    public String getFragmentClassName() {
        return fragmentClassName;
    }
}
