package com.github.yard01.sandbox.weatherforecast;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;

public abstract class WeatherForecastProviderFactory {

    public static final String FORECASTFACTORY_PREFERENCE = "forecastfactory";
    public static final long WEATHER_REFRESH_TIME_MIN = 30; //30 минут (время автоматического обновления погоды)
    public static final long FORECAST_REFRESH_TIME_MIN = 360; //6 часов (обновление прогноза)


    protected Context context;

    public WeatherForecastProviderFactory(Context _context) {
        this.context = _context;
    }

    public abstract WeatherForecastProvider getWeatherForecastProvider() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;
    public abstract String getFactoryName();
    public abstract String getFragmentClassName();

    public static WeatherForecastProviderFactory buildFactory(Context _context, String className) {
        try {
            return (WeatherForecastProviderFactory) Class.forName(className).getConstructor(Context.class).newInstance(_context);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
                e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }



}
