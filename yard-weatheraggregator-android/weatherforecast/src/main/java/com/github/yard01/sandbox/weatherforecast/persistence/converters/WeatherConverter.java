package com.github.yard01.sandbox.weatherforecast.persistence.converters;

import android.content.Context;

import com.github.yard01.sandbox.weatherforecast.R;
import com.github.yard01.sandbox.weatherforecast.Weather;
import com.github.yard01.sandbox.weatherforecast.WeatherForecast;
import com.github.yard01.sandbox.weatherforecast.persistence.entities.WeatherForecastEntity;

import java.util.Date;

public final class WeatherConverter {

    public static WeatherForecastEntity fromWeather(Weather weather) {
        WeatherForecastEntity entity = new WeatherForecastEntity();
        entity.setDate(weather.getDate());
        entity.setMinTemperature(weather.getMinTemperature());
        entity.setMaxTemperature(weather.getMaxTemperature());
        entity.setIconName(weather.getIconName());
        entity.setMinWindSpeed(weather.getMinWindSpeed());
        entity.setMaxWindSpeed(weather.getMaxWindSpeed());
        entity.setWindDirection(weather.getWindDirection());
        entity.setWindDirectionString(weather.getWindDirectionString());

        return entity;
    }

    public static String getWindDirectionString(Context context, float degree) {
        if (degree < 22.5 || degree > 337.5) return context.getString(R.string.wind_N);
        else if (degree >= 22.5 && degree <= 67.5) return context.getString(R.string.wind_NE);
        else if (degree > 67.5 && degree < 112.5) return context.getString(R.string.wind_E);
        else if (degree >= 112.5 && degree <= 157.5) return context.getString(R.string.wind_SE);
        else if (degree > 157.5 && degree < 202.5) return context.getString(R.string.wind_S);
        else if (degree >= 202.5 && degree <= 247.5) return context.getString(R.string.wind_SW);
        else if (degree > 247.5 && degree < 292.5) return context.getString(R.string.wind_W);
        else if (degree >= 292.5 && degree <= 337.5) return context.getString(R.string.wind_NW);
        return "";
    }

    public static Weather toWeather(WeatherForecastEntity entity) {
        return new Weather() {

            Date date  = entity.getDate();
            float maxT = entity.getMaxTemperature();
            float minT = entity.getMinTemperature();
            String iconName = entity.getIconName();
            float windSpeed = entity.getMaxWindSpeed();
            float windDirection = entity.getWindDirection();
            String windDirectionString = entity.getWindDirectionString();
            String locationName = "";

            @Override
            public Date getDate() {
                return date;
            }

            @Override
            public float getMinTemperature() {
                return minT;
            }

            @Override
            public float getMaxTemperature() {
                return maxT;
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
                return windSpeed;
            }

            @Override
            public float getMaxWindSpeed() {
                return windSpeed;
            }

            @Override
            public float getWindDirection() {
                return windDirection;
            }

            @Override
            public String getWindDirectionString() {
                return windDirectionString;
            }

            @Override
            public String getIconName() {
                return iconName;
            }

            @Override
            public String getLocationName() {
                return locationName;
            }


        };
    }

}
