package com.github.yard01.sandbox.weatherforecast;

import java.util.Date;

public interface Weather {
    public Date getDate();
    public float getMinTemperature();
    public float getMaxTemperature();
    public float getAtmPressure();
    public float getHumidity();
    public float getMinWindSpeed();
    public float getMaxWindSpeed();
    public float getWindDirection();
    public String getWindDirectionString();
    public String getIconName();
    public String getLocationName();
}
