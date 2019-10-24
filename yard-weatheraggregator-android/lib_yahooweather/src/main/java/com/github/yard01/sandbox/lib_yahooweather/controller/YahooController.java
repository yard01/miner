package com.github.yard01.sandbox.lib_yahooweather.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.github.yard01.sandbox.lib_yahooweather.R;
import com.github.yard01.sandbox.lib_yahooweather.YahooForecast;
import com.github.yard01.sandbox.lib_yahooweather.pojo.RxYahooApi;
import com.github.yard01.sandbox.lib_yahooweather.pojo.YahooResponse;
import com.github.yard01.sandbox.weatherforecast.Locator;
import com.github.yard01.sandbox.weatherforecast.Weather;
import com.github.yard01.sandbox.weatherforecast.WeatherForecast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import android.util.Base64;
import android.webkit.URLUtil;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;

public class YahooController {
//    App ID

//    Client ID (Consumer Key)

//    Client Secret (Consumer Secret)

    private static final String appId = "Your App Id";
    private static final String clientId = "Your Client Id";
    private static final String clientSecret = "Your Client Secret";

    private static final String contentType = "application/json";

    private Context context;
    private static YahooController instance;
    private Retrofit retrofitWeatherForecast;
    private String url;
    private String fullUrl;
    private Date refreshDate;
    private SharedPreferences preferences;

    public Map<String, String> getHeaders(double lat, double lon, String format, String metrics) {
        Map<String, String> headers = new HashMap<>();

        String APPID = preferences.getString(context.getString(R.string.yahoo_appid_textview), appId);
        String CONSUMER_KEY = preferences.getString(context.getString(R.string.yahoo_clientid_textview), clientId);
        String CONSUMER_SECRET = preferences.getString(context.getString(R.string.yahoo_clientsecret_textview), clientSecret);

        OAuthConsumer consumer = new OAuthConsumer(null,
                CONSUMER_KEY,
                CONSUMER_SECRET,
                null);
        consumer.setProperty(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.HMAC_SHA1);
        OAuthAccessor accessor = new OAuthAccessor(consumer);
        try {
            OAuthMessage request = accessor.newRequestMessage(OAuthMessage.GET, getUrl(lat, lon, format, metrics), null);
            String authorization = request.getAuthorizationHeader(null);
            headers.put("Authorization", authorization);
        } catch (Exception e ) {
            e.printStackTrace();
        }

        headers.put("X-Yahoo-App-Id", APPID);
        headers.put("Content-Type", "application/json");
        return headers;
    }

    public String getUrl(double lat, double lon, String format, String metrics) {
        return String.format(fullUrl, lat, lon, format, metrics) ;
    }

    private YahooController(Context context) {

        this.context = context;
        this.url = context.getString(R.string.yahoo_server_addr);
        this.fullUrl = url + context.getString(R.string.yahoo_server_app)+"?lat=%s&lon=%s&format=%s&u=%s";
        this.preferences = PreferenceManager.getDefaultSharedPreferences( context);
        retrofitWeatherForecast = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static YahooController getInstance(Context context) {
        if (instance == null) {
            instance = new YahooController(context);
        }
        return instance;
    }

    private Observable<YahooResponse> getYahooResponseObservable(String metrics) {
        return Observable.just(1)
                .subscribeOn(Schedulers.io())
                .map(i -> {
                    Locator locator = Locator.getInstance(context);
                    return locator.getEnabledLocation();
                }).flatMap(location -> {
                            String authorizationString =  getHeaders(location.getLatitude(), location.getLongitude(), "json", metrics).get("Authorization");
                            return
                                    retrofitWeatherForecast.create(RxYahooApi.class)
                                            .getResponse(appId, contentType, authorizationString, location.getLatitude(), location.getLongitude(), "json", metrics);
                        }
                );
    }

    public Observable<WeatherForecast> getForecastObservable(String metrics) {

        return getYahooResponseObservable(metrics)
                        .map(response -> {
                            return new YahooForecast(context, response);

                        });
    }

    public Observable<Weather> getWetherObservable(String metrics) {
        return getYahooResponseObservable(metrics)
                .doOnNext(yahooResponse -> {refreshDate = new Date();})
                .map(response -> {
                    return YahooForecast.getWeather(context, response);

                });
    }
}