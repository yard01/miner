package com.github.yard01.sandbox.lib_openweathermap.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.github.yard01.sandbox.lib_openweathermap.OWMForecast;
import com.github.yard01.sandbox.lib_openweathermap.OWMWeatherDecorator;
import com.github.yard01.sandbox.lib_openweathermap.R;
import com.github.yard01.sandbox.lib_openweathermap.pojo.JSONPlaceHolderApi;
import com.github.yard01.sandbox.lib_openweathermap.pojo.OWMWeatherResponse;
import com.github.yard01.sandbox.lib_openweathermap.pojo.RxOWMApi;
import com.github.yard01.sandbox.weatherforecast.Locator;
import com.github.yard01.sandbox.weatherforecast.Weather;
import com.github.yard01.sandbox.weatherforecast.WeatherForecast;
import com.github.yard01.sandbox.weatherforecast.tools.LiveweatherTools;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class OWMController {
    private static OWMController instance;
    private Retrofit retrofitWeatherForecast;
    private Retrofit retrofitIcons;
    private Context context;
    private SharedPreferences preferences;

    private OWMController(Context context) {
        this.context = context;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);

        retrofitWeatherForecast = new Retrofit.Builder()
                .baseUrl(urlBuilder())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitIcons = new Retrofit.Builder()
                .baseUrl(urlIcons())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static OWMController getInstance(Context context) {
        if (instance == null) {
            instance = new OWMController(context);
        }
        return instance;
    }

    private String urlBuilder() {
        String url = context.getString(R.string.openweathermap_server_addr);
        return url;
    }

    private String urlIcons() {
        String url = context.getString(R.string.openweathermap_server_icons);
        return url;
    }

    public void get(float latitude, float longitude, String apikey, String metric, Callback<OWMWeatherResponse> callback) {
        retrofitWeatherForecast.create(JSONPlaceHolderApi.class)
                .getCurrentWeather(latitude, longitude, apikey, metric)
                .enqueue(callback);
    }

    public Disposable getRXWeather(float latitude, float longitude, String apikey, String metric, Consumer consumer) {
        return retrofitWeatherForecast.create(RxOWMApi.class)
                .getWeather(
                        latitude,
                        longitude,
                        preferences.getString(context.getString(R.string.openweathermap_apikey_textview), apikey), //apikey,
                        metric)
                .subscribeOn(Schedulers.io())
                .subscribe(consumer, throwable -> {
                            Log.e(LOG_TAG, throwable.getMessage());
                        }
                );

    }

    public Observable<WeatherForecast> getForecastObservable(String apikey, String metric) {
        return
                Observable.just(1)
                        .subscribeOn(Schedulers.io())
                        .map(i -> {
                            Locator locator = Locator.getInstance(context);
                            return locator.getEnabledLocation();
                        }).flatMap(location -> {
                            return
                                    retrofitWeatherForecast.create(RxOWMApi.class)
                                            .getForecast(
                                                    location.getLatitude(),
                                                    location.getLongitude(),
                                                    preferences.getString(context.getString(R.string.openweathermap_apikey_textview), apikey),
                                                    metric);
                        }
                )
                        .map(response -> {
                            return new OWMForecast(context, response, this::downloadIcon);

                        });
    }


    public Observable<Weather> getWeatherObservable(String apikey, String metric) {
        return Observable.just(1)
                .subscribeOn(Schedulers.io())
                .map(i -> {
                    Locator locator = Locator.getInstance(context);
                    return locator.getEnabledLocation();
                }).flatMap(location -> {
                            return
                                    retrofitWeatherForecast.create(RxOWMApi.class)
                                            .getWeather(
                                                    location.getLatitude(),
                                                    location.getLongitude(),
                                                    preferences.getString(context.getString(R.string.openweathermap_apikey_textview), apikey),//apikey,
                                                    metric);
                        }
                )
                .map(response -> {
                    Thread.sleep(2000); //Service delay 2 seconds
                    //load the icon file
                    if (OWMWeatherDecorator.isIconExists(context, response.getWeather()[0].getIcon()))
                        downloadIcon(response.getWeather()[0].getIcon());
                    Weather result = OWMForecast.getWeather(context, response);
                    return result;

                });
    }


    public void downloadIcon(String iconName) {
        final String path = OWMWeatherDecorator.getOWMIconServerPath(iconName);
        final String fileName = OWMWeatherDecorator.getOWMIconFileName(iconName);

        Single.just(fileName)
                .subscribeOn(Schedulers.io())
                .subscribe(f -> {
                    if (!LiveweatherTools.fileExists(context, fileName))
                        retrofitIcons.create(RxOWMApi.class)
                                .getImage(path)
                                .subscribeOn(Schedulers.computation())
                                .subscribe(responseBody -> {
                                    LiveweatherTools.saveFile(context, responseBody.byteStream(), fileName);
                                });
                });

    }

    private static final String LOG_TAG = "OWMController";

}
