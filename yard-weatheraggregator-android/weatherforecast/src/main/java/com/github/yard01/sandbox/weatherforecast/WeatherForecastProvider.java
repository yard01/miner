package com.github.yard01.sandbox.weatherforecast;

import android.content.Context;

import com.github.yard01.sandbox.weatherforecast.viewmodel.WeatherForecastViewModel;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public interface WeatherForecastProvider <WeatherForecast> {
    public WeatherForecast provide();

    public Disposable subscribe(Consumer<WeatherForecast> consumer);

    public Observable observe();

    public Disposable subscribeSingle(Consumer<WeatherForecast> consumer);

    public WeatherForecastViewModel getModel();

    public String getName();

    public String getId();
}
