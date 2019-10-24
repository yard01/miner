package com.github.yard01.sandbox.lib_yahooweather.pojo;

import com.google.gson.annotations.SerializedName;

public class YahooLocation {
    @SerializedName("woeid")
    String woeid;

    @SerializedName("city")
    String city;

    @SerializedName("region")
    String region;

    @SerializedName("country")
    String country;

    public String getWoeid() {
        return woeid;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }
}
