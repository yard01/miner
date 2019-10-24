package com.github.yard01.sandbox.lib_openweathermap.pojo;

import com.google.gson.annotations.SerializedName;

public class OWMResponse_Wind {

    private float speed;

    @SerializedName("deg")
    private float degree;


    public float getSpeed() {
        return speed;
    }

    public float getDegree() {
        return degree;
    }

}