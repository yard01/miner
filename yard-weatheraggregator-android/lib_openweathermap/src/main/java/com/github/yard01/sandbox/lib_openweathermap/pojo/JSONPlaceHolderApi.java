package com.github.yard01.sandbox.lib_openweathermap.pojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JSONPlaceHolderApi {
    //@GET("/posts/{id}")
    //public Call<OWMWeatherResponse> getPostWithID(@Path("id") int id);
    @GET("weather")
    Call<OWMWeatherResponse> getCurrentWeather(
            @Query("lat") double latitude,
            @Query("lon") double longitude,

            @Query("appid") String apiKey,
            @Query("units") String units
    );
}
