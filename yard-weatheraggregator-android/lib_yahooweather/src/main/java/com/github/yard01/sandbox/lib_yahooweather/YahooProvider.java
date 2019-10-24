package com.github.yard01.sandbox.lib_yahooweather;

import android.content.Context;

import com.github.yard01.sandbox.lib_yahooweather.controller.YahooController;
import com.github.yard01.sandbox.weatherforecast.WeatherForecastProvider;
import com.github.yard01.sandbox.weatherforecast.WeatherForecastProviderFactory;
import com.github.yard01.sandbox.weatherforecast.viewmodel.WeatherForecastViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class YahooProvider implements WeatherForecastProvider {

    private Context context;
    public final static String PROVIDER_ID = "yahoo";

    WeatherForecastViewModel model;

    public YahooProvider(Context context) {
        this.context = context;
        model = WeatherForecastViewModel.getInstance(context);
        model.setWeatherDecorator(new YahooDecorator(context));
        model.setCurrentProviderId(PROVIDER_ID);
        YahooController controller = YahooController.getInstance(context);
        observeForecast(controller);

    }

    private void observeForecast(YahooController controller) {
        model.setForecast(controller.getForecastObservable("c"));
        model.setWeather(controller.getWetherObservable("c"));
    }

    @Override
    public Object provide() {
        return null;
    }

    @Override
    public Disposable subscribe(Consumer consumer) {
        return null;
    }

    @Override
    public Observable observe() {
        YahooController controller = YahooController.getInstance(context);
        return Observable.interval(0, WeatherForecastProviderFactory.WEATHER_REFRESH_TIME_MIN, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .doOnNext(index -> {
                    ;//reloadForecast(controller);  //загрузка данных прогноза в БД
                });

    }

    @Override
    public Disposable subscribeSingle(Consumer consumer) {
        return null;
    }

    @Override
    public WeatherForecastViewModel getModel() {
        return model;
    }

    @Override
    public String getName() {
        return context.getString(R.string.yahoo_server_name);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }


}
