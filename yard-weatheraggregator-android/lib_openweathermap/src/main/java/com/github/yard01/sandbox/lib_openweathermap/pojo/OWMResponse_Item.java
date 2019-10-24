package com.github.yard01.sandbox.lib_openweathermap.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class OWMResponse_Item {

    @SerializedName("dt")
    private long date;

    private Date dt_date;

    @SerializedName("main")
    private OWMResponse_Main main;

    @SerializedName("wind")
    private OWMResponse_Wind wind;

    @SerializedName("weather")
    private OWMResponse_Weather[] weather;

    public OWMResponse_Main getMain() {
        return main;
    }

    public void setMain(OWMResponse_Main main) {
        this.main = main;
    }

    public OWMResponse_Weather[] getWeather() {
        return weather;
    }

    public void setWeather(OWMResponse_Weather[] weather) {
        this.weather = weather;
    }

    public Date getDate() {
        if (dt_date == null)
            dt_date = new Date(date * 1000);
        return dt_date;
    }

    public OWMResponse_Wind getWind() {
        return wind;
    }
}
