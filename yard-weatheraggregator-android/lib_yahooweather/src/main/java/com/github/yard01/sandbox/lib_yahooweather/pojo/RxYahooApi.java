package com.github.yard01.sandbox.lib_yahooweather.pojo;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface RxYahooApi {
    @GET("forecastrss")
    Observable<YahooResponse> getResponse(
            @Header("X-Yahoo-App-Id") String appId,
            @Header("Content-Type") String content_type,
            @Header("Authorization") String authorization,
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("format") String format,
            @Query("u") String metrics
    );

    Observable<YahooResponse> getResponse(
            @Header("X-Yahoo-App-Id") String appId,
            @Header("Content-Type") String content_type,
            @Header("Authorization") String authorization,
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("format") String format
    );

    @GET("forecastrss")
    Observable<ResponseBody> getResponseBody(
            @Header("X-Yahoo-App-Id") String appId,
            @Header("Content-Type") String content_type,
            @Header("Authorization") String authorization,
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("format") String format,
            @Query("u") String metrics
    );

}
