package com.github.yard01.sandbox.lib_openweathermap.pojo;

import com.google.gson.annotations.SerializedName;

public class OWMResponse_Weather {
    @SerializedName("id")
    int id;

    @SerializedName("icon")
    String icon;

    public int getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }
}
