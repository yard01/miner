package com.github.yard01.sandbox.weatherforecast.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.github.yard01.sandbox.weatherforecast.Weather;
import com.github.yard01.sandbox.weatherforecast.WeatherDatasource;
import com.github.yard01.sandbox.weatherforecast.WeatherDecorator;
import com.github.yard01.sandbox.weatherforecast.WeatherForecast;
import com.github.yard01.sandbox.weatherforecast.persistence.LocalWeatherDatasource;
import com.github.yard01.sandbox.weatherforecast.persistence.WeatherForecastDatabase;
import com.github.yard01.sandbox.weatherforecast.persistence.converters.WeatherConverter;
import com.github.yard01.sandbox.weatherforecast.persistence.entities.WeatherForecastEntity;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WeatherForecastViewModel extends ViewModel {

    private static volatile WeatherForecastViewModel instance;

    private WeatherDatasource datasource;

    private Observable<WeatherForecast> forecast;

    private Observable<Weather> weather;

    private WeatherDecorator weatherDecorator;

    private LiveData<PagedList<WeatherForecastEntity>> weatherEntities; // набор данных для постраничного просмотра

    private String currentProviderId;

    private Context context;

    public WeatherForecastViewModel(Context context) {
        this.context = context;
    }

    public void setDatasource(WeatherDatasource weatherDatasource) {
        this.datasource = weatherDatasource;
        DataSource.Factory<Integer, WeatherForecastEntity> factory=this.datasource.getPagedForecast(currentProviderId);
        LivePagedListBuilder<Integer, WeatherForecastEntity> pagedListBuilder= new LivePagedListBuilder<>(factory, 5);
        weatherEntities = pagedListBuilder.build();
    }

    public void setWeather(Observable<Weather> weather) {
        this.weather = weather;
    }

    public void deleteOldRecords() {
        Date d = new Date();
        this.datasource.deleteOldRecords(d).subscribe();
    }

    public void reloadForecastDatabase(Action action) {
        if (this.datasource != null)
            forecast
                    .observeOn(Schedulers.io())
                    .flatMap(weatherForecast -> {
                                deleteOldRecords();
                                return Observable.fromArray(weatherForecast.getForecast());
                            }
                    ).subscribe(weather -> {
                datasource
                        .insertWeatherForecast(WeatherConverter.fromWeather(weather), currentProviderId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe( action,
                                throwable   -> Log.e("weather", "Unable to insert weather", throwable));
            }, throwable -> {Log.e("weather", throwable.getMessage());});

    }

    public void reloadForecastDatabase() {
        reloadForecastDatabase(()->{;});
    }

    public void setForecast(Observable<WeatherForecast> forecast) {
        this.forecast = forecast;
    }

    public Flowable<WeatherForecastEntity> loadWeathers() {
        return this.datasource.getForecast();
    }


    public Observable<WeatherForecast> getForecast() {
        return forecast;
    }

    public Observable<Weather> getWeather() {
        return weather;
    }


    public static WeatherForecastViewModel getInstance(Context context) {
        if (instance == null) {
            instance = new WeatherForecastViewModel(context);
        }
        return instance;
    }

    public WeatherDecorator getWeatherDecorator() {
        return weatherDecorator;
    }

    public void setWeatherDecorator(WeatherDecorator weatherDecorator) {
        this.weatherDecorator = weatherDecorator;
    }

    public LiveData<PagedList<WeatherForecastEntity>>  getWeatherEntities() {
        return weatherEntities;
    }

    public Flowable<WeatherForecastEntity> getOfflineWeather() {
        return datasource.getAvgForecast(new Date(), currentProviderId);
    }

    public void setCurrentProviderId(String providerId) {
        this.currentProviderId = providerId;
        WeatherForecastDatabase db = WeatherForecastDatabase.getInstance(context);
        LocalWeatherDatasource datasource = new LocalWeatherDatasource(db.getWeatherForecastDao());
        instance.setDatasource(datasource);

    }

    public String getCurrentProviderId() {
        return currentProviderId;
    }
}
