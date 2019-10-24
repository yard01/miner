package com.github.yard01.sandbox.lib_openweathermap;

import com.github.yard01.sandbox.weatherforecast.Weather;

import java.util.Date;

public class OWMWeather implements Weather {

    @Override
    public Date getDate() {
        return null;
    }

    @Override
    public float getMinTemperature() {
        return 0;
    }

    @Override
    public float getMaxTemperature() {
        return 0;
    }

    @Override
    public float getAtmPressure() {
        return 0;
    }

    @Override
    public float getHumidity() {
        return 0;
    }

    @Override
    public float getMinWindSpeed() {
        return 0;
    }

    @Override
    public float getMaxWindSpeed() {
        return 0;
    }

    @Override
    public float getWindDirection() {
        return 0;
    }

    @Override
    public String getWindDirectionString() {
        return null;
    }

    @Override
    public String getIconName() {
        return null;
    }

    @Override
    public String getLocationName() {
        return null;
    }
}
