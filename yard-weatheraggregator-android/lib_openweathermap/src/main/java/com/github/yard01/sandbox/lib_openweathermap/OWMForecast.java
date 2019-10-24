package com.github.yard01.sandbox.lib_openweathermap;

import android.content.Context;

import com.github.yard01.sandbox.lib_openweathermap.pojo.OWMForecastResponse;
import com.github.yard01.sandbox.lib_openweathermap.pojo.OWMResponse_Item;
import com.github.yard01.sandbox.lib_openweathermap.pojo.OWMWeatherResponse;
import com.github.yard01.sandbox.weatherforecast.Weather;
import com.github.yard01.sandbox.weatherforecast.WeatherForecast;
import com.github.yard01.sandbox.weatherforecast.persistence.converters.WeatherConverter;

import java.util.Date;

import io.reactivex.functions.Consumer;

public class OWMForecast implements WeatherForecast {


    private Date date;
    private Weather[] forecast;

    //public OWMForecast() {


    public OWMForecast(Context context, OWMForecastResponse serverResponse, Consumer<String> iconDowloader) {
        date = new Date();

        forecast = new Weather[serverResponse.getList().length];
        int i = 0;
        for (OWMResponse_Item fm : serverResponse.getList()) {

            try {
                iconDowloader.accept(fm.getWeather()[0].getIcon());
            } catch (Exception e) {
                e.printStackTrace();
            }

            Weather w = new Weather() {

                float minT = fm.getMain().getMinTemp();
                float maxT = fm.getMain().getMaxTemp();
                Date dt = fm.getDate();

                String iconName = fm.getWeather()[0].getIcon();
                float windSpeed = fm.getWind().getSpeed();
                float windDirection = fm.getWind().getDegree();
                float humidity = fm.getMain().getHumidity();
                float pressure = Math.round(fm.getMain().getPressure() * 10 / 1.33322) / 10;

                @Override
                public Date getDate() {
                    return dt;
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
                    return pressure;
                }

                @Override
                public float getHumidity() {
                    return humidity;
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
                    return WeatherConverter.getWindDirectionString(context, windDirection);
                }

                @Override
                public String getIconName() {
                    return iconName;
                }

                @Override
                public String getLocationName() {
                    return "";
                }
            };
            //fm = new OWMForecast();
            forecast[i] = w;
            i++;
        }
    }

    public static Weather getWeather(Context context, OWMWeatherResponse serverResponse) {
        return new Weather() {
            float minT = serverResponse.getMain().getMinTemp();
            float maxT = serverResponse.getMain().getMaxTemp();
            //serverResponse.
            String iconName = serverResponse.getWeather()[0].getIcon();

            Date dt = serverResponse.getDate();
            float windSpeed = serverResponse.getWind().getSpeed();
            float windDirection = serverResponse.getWind().getDegree();
            String locationName = serverResponse.getName();
            float humidity = serverResponse.getMain().getHumidity();
            float pressure = Math.round(serverResponse.getMain().getPressure() * 10 / 1.33322) / 10;

            @Override
            public Date getDate() {
                return dt;
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
                return pressure;
            }

            @Override
            public float getHumidity() {
                return humidity;
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
                return WeatherConverter.getWindDirectionString(context, windDirection);
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

    @Override
    public Date getForecastDate() {
        return date;
    }


    @Override
    public Weather[] getForecast() {

        //WeatherForecast[] result = new WeatherForecast[pojoResponse.getList().length];

        //for (OWMResponse_Main forecast : pojoResponse.getList()) {

        //}
        return forecast;// result;
    }
}
