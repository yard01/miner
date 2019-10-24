package com.github.yard01.sandbox.weatherforecast.persistence;

import androidx.paging.DataSource;

import com.github.yard01.sandbox.weatherforecast.WeatherDatasource;
import com.github.yard01.sandbox.weatherforecast.WeatherForecast;
import com.github.yard01.sandbox.weatherforecast.persistence.entities.WeatherForecastEntity;

import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class LocalWeatherDatasource implements WeatherDatasource {

    private final WeatherForecastDao mWeatherDao;

    public LocalWeatherDatasource (WeatherForecastDao weatherDao) {
        mWeatherDao = weatherDao;
    }

    @Override
    public Flowable<WeatherForecastEntity> getForecast() {
        return mWeatherDao.getForecast();
    }

    @Override
    public Completable insertWeatherForecast(WeatherForecastEntity forecast) {
        return mWeatherDao.insertForecast(forecast);
    }

    @Override
    public Completable insertWeatherForecast(WeatherForecastEntity forecast, String providerId) {
        forecast.setProvider(providerId);
        return mWeatherDao.insertForecast(forecast);
    }


    @Override
    public Completable deleteOldRecords(Date date) {
        return mWeatherDao.deleteOldRecords(date);
    }

    @Override
    public Completable deleteAll() {
        return mWeatherDao.deleteAll();
    }

    @Override
    public DataSource.Factory<Integer, WeatherForecastEntity> getPagedForecast() {
        return mWeatherDao.getPagedForecast();
    }

    @Override
    public DataSource.Factory<Integer, WeatherForecastEntity> getPagedForecast(String providerId) {
        return mWeatherDao.getPagedForecast(providerId);
    }

    @Override
    public Flowable<WeatherForecastEntity> getAvgForecast(Date date, String providerId) {
        return mWeatherDao.getAvgForecast(date, providerId);
    }

    @Override
    public Flowable<WeatherForecastEntity> getAvgForecast(Date date) {
        return mWeatherDao.getAvgForecast(date);
    }


}
