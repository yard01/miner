package com.github.yard01.sandbox.lib_openweathermap;

import android.content.Context;
import android.util.Log;

import com.github.yard01.sandbox.lib_openweathermap.controller.OWMController;
import com.github.yard01.sandbox.lib_openweathermap.pojo.OWMWeatherResponse;
import com.github.yard01.sandbox.weatherforecast.Locator;
import com.github.yard01.sandbox.weatherforecast.WeatherForecast;
import com.github.yard01.sandbox.weatherforecast.WeatherForecastProvider;
import com.github.yard01.sandbox.weatherforecast.WeatherForecastProviderFactory;
import com.github.yard01.sandbox.weatherforecast.viewmodel.WeatherForecastViewModel;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OWMProvider implements WeatherForecastProvider {
    private static final String[] API_KEYS = {"key1", "key2", "key3"};

    private final static String PROVIDER_ID = "owm";
    private static long MINUTE_COUNTER = 0;
    private Context context;
    private WeatherForecastViewModel model = null;
    private int weatherRefreshCount = 0;
    private int randomAPIKeyIndex = 0;

    public OWMProvider(Context context) {

        this.context = context;
        model = WeatherForecastViewModel.getInstance(context);
        model.setCurrentProviderId(PROVIDER_ID);
        model.setWeatherDecorator(new OWMWeatherDecorator(context));

        OWMController controller = OWMController.getInstance(context);
        randomAPIKeyIndex = (new Random()).nextInt(API_KEYS.length);

        observeWeather(controller);
        observeForecast(controller);

    }

    @Override
    public WeatherForecast provide() {

//        //делаем запрос к серверу погоды
//        //получаем ответ в JSON
//        Log.d("weather_tula", "start");
//        OWMController controller = OWMController.getInstance(context);
//        //RxJava2CallAdapterFactory fr; //.create();
//
//        controller.get(54.19f, 37.62f, API_KEYS[0], "metric",
//                new Callback<OWMWeatherResponse>() {
//                    @Override
//                    public void onResponse(Call<OWMWeatherResponse> call, Response<OWMWeatherResponse> response) {
//                        OWMWeatherResponse weather = response.body();
//                        response.toString();
//                    }
//
//                    @Override
//                    public void onFailure(Call<OWMWeatherResponse> call, Throwable t) {
//
//                        Log.e("weather_tula", t.toString()); //"temp = " + weather.getTemp());
//                    }
//                }
//        );
//
//        //RX///////////////////////////////////////
//        Consumer<OWMWeatherResponse> consumer = response -> {
//            Log.d("weather_tula_rx", "rx_temp = " + response.getMain().getTemp());
//        };
//
//        controller.getRXWeather(54.19f, 37.62f, API_KEYS[0], "metric", consumer);
//        ///////////////////////////////////////////
//
//        OWMWeatherResponse pojoResponse = null;
//
//        //возвращаем обобщенную структуру
//        WeatherForecast result = new OWMForecast(null, null, null);

        return null; //result;
    }

    private void observeWeatherInLocation(OWMController controller) {
        Locator locator = Locator.getInstance(context);
        observeWeather(controller);
        observeForecast(controller);
    }

    private void observeWeather(OWMController controller) {
        model.setWeather(
                controller.getWeatherObservable(API_KEYS[randomAPIKeyIndex], "metric")
        );
    }

    private void observeForecast(OWMController controller) {
        model.setForecast(
                controller.getForecastObservable(API_KEYS[randomAPIKeyIndex], "metric")
        );
    }

    private void reloadForecast(OWMController controller) {
        //если время обновления прогноза превысило установленный предел, то обновляем прогноз
        if (weatherRefreshCount == 0 || weatherRefreshCount * WeatherForecastProviderFactory.WEATHER_REFRESH_TIME_MIN > WeatherForecastProviderFactory.FORECAST_REFRESH_TIME_MIN) {
            model.reloadForecastDatabase();
            weatherRefreshCount = 1;
        } else weatherRefreshCount++;

    }

    @Override
    public Disposable subscribeSingle(Consumer consumer) {
        OWMController controller = OWMController.getInstance(context);
        return
                Observable.just(0)
                        .subscribeOn(Schedulers.io())
                        .doOnNext(index -> {
                            reloadForecast(controller);
                        })
                        .subscribe(consumer);
    }

    @Override
    public WeatherForecastViewModel getModel() {
        return model;
    }

    @Override
    public String getName() {
        return context.getString(R.string.openweathermap_server_name);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }


    @Override
    public Disposable subscribe(Consumer consumer) {
        return observe().subscribe(consumer);

    }

    @Override
    public Observable observe() {
        OWMController controller = OWMController.getInstance(context);
        return Observable.interval(0, WeatherForecastProviderFactory.WEATHER_REFRESH_TIME_MIN, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .doOnNext(index -> {
                    reloadForecast(controller);  //загрузка данных прогноза в БД
                });
    }


}
