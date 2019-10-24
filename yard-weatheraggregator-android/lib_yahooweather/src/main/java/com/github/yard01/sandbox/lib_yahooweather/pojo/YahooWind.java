package com.github.yard01.sandbox.lib_yahooweather.pojo;

import com.google.gson.annotations.SerializedName;

public class YahooWind {
    @SerializedName("chill")
    float chill;

    @SerializedName("direction")
    float direction;

    @SerializedName("speed")
    float speed;

    public float getChill() {
        return chill;
    }

    public float getDirection() {
        return direction;
    }

    public float getSpeed() {
        return speed;
    }
}
