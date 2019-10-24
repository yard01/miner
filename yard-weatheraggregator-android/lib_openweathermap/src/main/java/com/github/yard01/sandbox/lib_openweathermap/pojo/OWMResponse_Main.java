package com.github.yard01.sandbox.lib_openweathermap.pojo;

import com.google.gson.annotations.SerializedName;

public class OWMResponse_Main {

    private float temp;

    private float pressure;

    private int humidity;

    @SerializedName("temp_min")
    private float minTemp;

    @SerializedName("temp_max")
    private float maxTemp;

    public float getTemp() {
        return temp;
    }

    public float getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

}