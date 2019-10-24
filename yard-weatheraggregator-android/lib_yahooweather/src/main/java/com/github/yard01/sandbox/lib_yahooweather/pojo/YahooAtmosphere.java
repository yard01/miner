package com.github.yard01.sandbox.lib_yahooweather.pojo;

import com.google.gson.annotations.SerializedName;

public class YahooAtmosphere {
    @SerializedName("humidity")
    private float humidity;

    @SerializedName("visibility")
    private float visibility;

    @SerializedName("pressure")
    private float pressure;

    @SerializedName("rising")
    private float rising;

    public float getHumidity() {
        return humidity;
    }

    public float getVisibility() {
        return visibility;
    }

    public float getPressure() {
        return pressure;
    }

    public float getRising() {
        return rising;
    }
}
