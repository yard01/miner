package com.github.yard01.sandbox.weatherforecast;

import androidx.paging.DataSource;

import com.github.yard01.sandbox.weatherforecast.persistence.entities.WeatherForecastEntity;

import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface WeatherDatasource {
    Flowable<WeatherForecastEntity> getForecast();

    Completable insertWeatherForecast(WeatherForecastEntity forecast);

    Completable insertWeatherForecast(WeatherForecastEntity forecast, String providerId);

    Completable deleteOldRecords(Date date);

    Completable deleteAll();

    DataSource.Factory<Integer, WeatherForecastEntity> getPagedForecast();

    DataSource.Factory<Integer, WeatherForecastEntity> getPagedForecast(String providerId);

    Flowable<WeatherForecastEntity> getAvgForecast(Date date);

    Flowable<WeatherForecastEntity> getAvgForecast(Date date, String providerId);

}
