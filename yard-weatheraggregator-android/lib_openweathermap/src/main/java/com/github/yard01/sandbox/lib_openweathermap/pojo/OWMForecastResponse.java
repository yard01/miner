package com.github.yard01.sandbox.lib_openweathermap.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OWMForecastResponse {
    @SerializedName("list")
    @Expose
    private OWMResponse_Item[] list;

    public OWMResponse_Item[] getList() {
        return list;
    }

    public void setList(OWMResponse_Item[] list) {
        this.list = list;
    }
}
