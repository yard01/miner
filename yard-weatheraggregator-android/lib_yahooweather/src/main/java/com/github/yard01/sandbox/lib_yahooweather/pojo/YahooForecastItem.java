package com.github.yard01.sandbox.lib_yahooweather.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class YahooForecastItem {
    @SerializedName("day")
    private String day;

    @SerializedName("date")
    private long date;
    private Date dt_date;

    @SerializedName("low")
    private float low;

    @SerializedName("high")
    private float high;

    @SerializedName("text")
    private String text;

    @SerializedName("code")
    private int code;

    public String getDay() {
        return day;
    }

    public Date getDate() {
        if (dt_date == null)
            dt_date = new Date(date * 1000);
        return dt_date;
    }

    public float getLow() {
        return low;
    }

    public float getHigh() {
        return high;
    }

    public String getText() {
        return text;
    }

    public int getCode() {
        return code;
    }


}
