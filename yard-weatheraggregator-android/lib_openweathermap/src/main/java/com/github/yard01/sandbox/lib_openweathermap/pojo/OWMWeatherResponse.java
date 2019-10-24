package com.github.yard01.sandbox.lib_openweathermap.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class OWMWeatherResponse {

    @SerializedName("dt")
    private long date;
    private Date dt_date;

    @SerializedName("main")
    @Expose
    private OWMResponse_Main main;

    @SerializedName("wind")
    @Expose
    private OWMResponse_Wind wind;

    @SerializedName("weather")
    @Expose
    private OWMResponse_Weather[] weather;

    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OWMResponse_Main getMain() {
        return main;
    }

    public OWMResponse_Wind getWind() {
        return wind;
    }

    public OWMResponse_Weather[] getWeather() {
        return weather;
    }

    public Date getDate() {
        if (dt_date == null)
            dt_date = new Date(date * 1000);
        return dt_date;
    }

}
