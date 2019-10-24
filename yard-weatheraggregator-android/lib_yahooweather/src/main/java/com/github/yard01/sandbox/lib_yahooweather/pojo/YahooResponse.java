package com.github.yard01.sandbox.lib_yahooweather.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YahooResponse {
    @SerializedName("location")
    @Expose
    private YahooLocation location;

    @SerializedName("current_observation")
    @Expose
    private YahooCurrentObservation observation;

    @SerializedName("forecasts")
    private YahooForecastItem[] forecasts;

    public YahooCurrentObservation getObservation() {
        return observation;
    }

    public YahooForecastItem[] getForecasts() {
        return forecasts;
    }

    public YahooLocation getLocation() {
        return location;
    }
}
