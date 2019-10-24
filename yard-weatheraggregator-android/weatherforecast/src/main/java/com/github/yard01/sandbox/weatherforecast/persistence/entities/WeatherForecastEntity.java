package com.github.yard01.sandbox.weatherforecast.persistence.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.github.yard01.sandbox.weatherforecast.Weather;

import java.util.Date;

@Entity(tableName = "forecast",
        indices= @Index(value = {"PROVIDER"})
)
public class WeatherForecastEntity {//implements Weather
    @PrimaryKey
    @ColumnInfo(name = "DT")
    private Date date;

    @ColumnInfo(name = "MINT")
    private float minTemperature;

    @ColumnInfo(name = "MAXT")
    private float maxTemperature;

    @ColumnInfo(name = "HUMIDITY")
    private float humidity;

    @ColumnInfo(name = "PRESSURE")
    private float atmPressure;

    @ColumnInfo(name = "MINWINDSPEED")
    private float minWindSpeed;

    @ColumnInfo(name = "MAXWINDSPEED")
    private float maxWindSpeed;

    @ColumnInfo(name = "WINDDIR")
    private float windDirection;

    @ColumnInfo(name = "WINDDIRTEXT")
    private String windDirectionString;

    @ColumnInfo(name = "ICON")
    private String iconName;

    @ColumnInfo(name = "PROVIDER")
    private String provider;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(float minTemperature) {
        this.minTemperature = minTemperature;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getAtmPressure() {
        return atmPressure;
    }

    public void setAtmPressure(float pressure) {
        this.atmPressure = pressure;
    }

    public float getMinWindSpeed() {
        return minWindSpeed;
    }

    public float getMaxWindSpeed() {
        return maxWindSpeed;
    }

    //@Override
    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public void setMaxWindSpeed(float windSpeed) {
        this.maxWindSpeed = windSpeed;
    }

    public void setMinWindSpeed(float windSpeed) {
        this.minWindSpeed = windSpeed;
    }

    public void setWindDirection(float windDirection) {
        this.windDirection = windDirection;
    }

    public float getWindDirection() {
        return windDirection;
    }

    public String getWindDirectionString() {
        return windDirectionString;
    }

    public void setWindDirectionString(String windDirectionString) {
        this.windDirectionString = windDirectionString;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
