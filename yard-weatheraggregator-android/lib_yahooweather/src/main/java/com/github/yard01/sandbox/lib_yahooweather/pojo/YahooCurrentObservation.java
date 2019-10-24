package com.github.yard01.sandbox.lib_yahooweather.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class YahooCurrentObservation {
    @SerializedName("pubDate")
    private long date;
    private Date dt_date;

    @SerializedName("wind")
    @Expose
    private YahooWind wind;

    @SerializedName("atmosphere")
    @Expose
    private YahooAtmosphere atmosphere;

    @SerializedName("condition")
    @Expose
    private YahooCondition condition;

    public YahooAtmosphere getAtmosphere() {
        return atmosphere;
    }

    public YahooWind getWind() {
        return wind;
    }

    public YahooCondition getCondition() {
        return condition;
    }

    public Date getDate() {
        if (dt_date == null)
            dt_date = new Date(date * 1000);
        return dt_date;
    }
}
