package com.github.yard01.sandbox.weatherforecast;

import java.util.Date;

public interface WeatherForecast {
    public Date getForecastDate();
    public Weather[] getForecast();
}
