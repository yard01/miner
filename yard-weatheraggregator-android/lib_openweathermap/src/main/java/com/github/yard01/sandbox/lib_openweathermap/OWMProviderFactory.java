package com.github.yard01.sandbox.lib_openweathermap;

import android.content.Context;

import com.github.yard01.sandbox.weatherforecast.WeatherForecastProvider;
import com.github.yard01.sandbox.weatherforecast.WeatherForecastProviderFactory;

import java.lang.reflect.InvocationTargetException;

public class OWMProviderFactory extends WeatherForecastProviderFactory {

    private static final String providerClassName = "com.github.yard01.sandbox.lib_openweathermap.OWMProvider";
    private static final String fragmentClassName = "com.github.yard01.sandbox.lib_openweathermap.OWMFragment";

    private static WeatherForecastProvider forecastProvider = null;

    public OWMProviderFactory(Context context) {
        super(context);
    }

    @Override
    public WeatherForecastProvider getWeatherForecastProvider() throws NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        if (forecastProvider == null)
            forecastProvider = (WeatherForecastProvider) Class.forName(providerClassName).getConstructor(Context.class).newInstance(context);
        return forecastProvider;
    }

    @Override
    public String getFactoryName() {
        return context.getString(R.string.openweathermap_server_name);
    }

    @Override
    public String getFragmentClassName() {
        return fragmentClassName;
    }


}
