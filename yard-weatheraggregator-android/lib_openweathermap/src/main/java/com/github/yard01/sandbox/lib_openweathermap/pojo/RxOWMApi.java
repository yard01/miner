package com.github.yard01.sandbox.lib_openweathermap.pojo;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RxOWMApi {
    @GET("weather")
    Observable<OWMWeatherResponse> getWeather(
            @Query("lat") double latitude,
            @Query("lon") double longitude,

            @Query("appid") String apiKey,
            @Query("units") String units
    );

    @GET("forecast")
    Observable<OWMForecastResponse> getForecast(
            @Query("lat") double latitude,
            @Query("lon") double longitude,

            @Query("appid") String apiKey,
            @Query("units") String units
    );

    @GET("img/wn/{name}")
    Observable<ResponseBody> getImage(
            @Path("name") String name);
}
