package com.github.yard01.sandbox.lib_yahooweather;

import android.content.Context;

import com.github.yard01.sandbox.lib_yahooweather.pojo.YahooForecastItem;
import com.github.yard01.sandbox.lib_yahooweather.pojo.YahooResponse;
import com.github.yard01.sandbox.weatherforecast.Weather;
import com.github.yard01.sandbox.weatherforecast.WeatherForecast;
import com.github.yard01.sandbox.weatherforecast.persistence.converters.WeatherConverter;

import java.util.Date;

public class YahooForecast implements WeatherForecast {
    private Date date;
    private Weather[] forecast;

    public YahooForecast(Context context, YahooResponse response) {
            date = new Date();

            forecast = new Weather[response.getForecasts().length];
            int i = 0;
            for (YahooForecastItem fm : response.getForecasts()) {

                //try {
                //    iconDowloader.accept(fm.getWeather()[0].getIcon());
                //} catch (Exception e) {
                //    e.printStackTrace();
                //}

                Weather w = new Weather() {

                    float minT = fm.getLow();
                    float maxT = fm.getHigh();
                    Date dt = fm.getDate();

                    String iconName = String.valueOf(fm.getCode()); // fm.getWeather()[0].getIcon();
                    float windSpeed = 0; //fm.getWind().getSpeed();
                    float windDirection = 0; //fm.getWind().getDegree();

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
                forecast[i] = w; i++;
            }

    }

    public static Weather getWeather(Context context, YahooResponse response) {
        return new Weather() {
            float minT = response.getObservation().getCondition().getTemperature();
            float maxT = minT;
               //serverResponse.
            String iconName = String.valueOf(response.getObservation().getCondition().getCode()); //serverResponse.getWeather()[0].getIcon();

            Date dt = response.getObservation().getDate();

            float windSpeed = Math.round(response.getObservation().getWind().getSpeed()*1000/360)/10;
            float windDirection = response.getObservation().getWind().getDirection();
            String locationName = response.getLocation().getCity();
            float pressure = Math.round(response.getObservation().getAtmosphere().getPressure()*10/1.33322)/10;
            float humidity =  response.getObservation().getAtmosphere().getHumidity();

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
        return forecast;
    }
}
