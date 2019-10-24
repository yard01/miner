package com.github.yard01.sandbox.liveweather.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import com.github.yard01.sandbox.liveweather.PreferenceController;
import com.github.yard01.sandbox.liveweather.R;
import com.github.yard01.sandbox.liveweather.ui.ForecastActivity;
import com.github.yard01.sandbox.weatherforecast.Locator;
import com.github.yard01.sandbox.weatherforecast.tools.LiveweatherTools;
import com.github.yard01.sandbox.weatherforecast.Weather;
import com.github.yard01.sandbox.weatherforecast.WeatherForecastProvider;
import com.github.yard01.sandbox.weatherforecast.WeatherForecastProviderFactory;
import com.github.yard01.sandbox.weatherforecast.viewmodel.WeatherForecastViewModel;

import java.util.Date;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherWidget extends AppWidgetProvider {

    private static final String REFRESH_OnClick = "com.github.yard01.sandbox.liveweather.widget.refresh.OnClick";
    private static final String FORECAST_OnClick = "com.github.yard01.sandbox.liveweather.widget.forecast.OnClick";

    private static volatile Disposable weatherDisposable;

    private final static int refreshWeatherInterval = 30; //minute
    private final static int refreshForecastInterval = 360; //minute - 6 hours

    private static volatile Date currentWeatherDate = null;
    private static volatile Date currentForecastDate = null;
    private static volatile Weather receivedWeather = null;

    private static boolean isRefreshing = false;

    public static String lastStatus = "";

    public static class WeatherUpdateService extends Service {


        private static final String ACTION_UPDATE ="com.github.yard01.sandbox.liveweather.widget.action.UPDATE";
        private final static IntentFilter intentFilter;

        static {
            intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_TIME_TICK); //каждую минуту
            intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
            intentFilter.addAction(ACTION_UPDATE);
        }

        /**
         * BroadcastReceiver receiving the updates.
         */
        private final BroadcastReceiver clockChangedReceiver = new
                BroadcastReceiver() {
                    /**
                     * {@inheritDoc}
                     */
                    public void onReceive(Context context, Intent intent) {
                        update(context);
                    }
                };

        /**
         * {@inheritDoc}
         */
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public void onCreate() {
            super.onCreate();
            registerReceiver(clockChangedReceiver, intentFilter);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public void onDestroy() {
            //disposeWeatherProvider();
            unregisterReceiver(clockChangedReceiver);
            super.onDestroy();
        }
        /**
         * {@inheritDoc}
         */


        @Override
        public void onStart(Intent intent, int startId) {
            if (intent != null && intent.getAction() != null) {
                if (intent.getAction().equals(ACTION_UPDATE)) {
                    update(this);
                }
            }
        }

    } /////////////////////////////////////////////////////////////////////////////////////

    private static void disposeWeather() {
        if (weatherDisposable != null) weatherDisposable.dispose();
    }

    protected void startService(Context context) {
        try {

            Intent srvs = new Intent(context, WeatherWidget.WeatherUpdateService.class);
            srvs.setAction(WeatherWidget.WeatherUpdateService.ACTION_UPDATE);
            context.startService(srvs);
        } catch (Exception e) {
            lastStatus = e.getMessage();
            e.printStackTrace();
        }
    }

    protected void stopService(Context context) {
        try {
            Intent srvs = new Intent(context, WeatherWidget.WeatherUpdateService.class);
            context.stopService(srvs);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void onReceiveWeather(Weather weather) {
        receivedWeather = weather;
        currentWeatherDate = new Date();
    }

    protected static void fillWidgetViews(Context context, RemoteViews views) {
        if (currentWeatherDate != null) views.setViewVisibility(R.id.weather_ProgressBar, ProgressBar.INVISIBLE);
        if (receivedWeather == null) return;
        try {
            WeatherForecastViewModel model = WeatherForecastViewModel.getInstance(context);
            views.setTextViewText(R.id.temperature_TextView, LiveweatherTools.getTemperatureDegString(context, receivedWeather.getMaxTemperature()));
            views.setTextViewText(R.id.weather_location_TextView, receivedWeather.getLocationName());
            views.setTextViewText(R.id.wind_TextView, LiveweatherTools.getWindString(context, receivedWeather.getMaxWindSpeed(), receivedWeather.getWindDirectionString()));

            Bitmap bitmap = model.getWeatherDecorator().getBitmap(receivedWeather.getIconName());
            if (bitmap != null) {
                views.setViewVisibility(R.id.weather_icon_ImageView, View.VISIBLE);
                views.setViewVisibility(R.id.weather_icon_vector_ImageView, View.INVISIBLE);
                views.setImageViewBitmap(R.id.weather_icon_ImageView, bitmap);
            }
            else {
                int iconId = model.getWeatherDecorator().getDrawableId(receivedWeather.getIconName());
                if (iconId != 0 && iconId != -1) {
                    views.setViewVisibility(R.id.weather_icon_ImageView, View.INVISIBLE);
                    views.setViewVisibility(R.id.weather_icon_vector_ImageView, View.VISIBLE);
                    views.setImageViewResource(R.id.weather_icon_vector_ImageView, iconId);
                }
            }
            views.setTextViewText(R.id.updateTime_TextView,
                    context.getString(R.string.upd_time) + " " + LiveweatherTools.dateToString(currentWeatherDate, "HH:mm"));


        } catch (Exception e) {
            views.setTextViewText(R.id.weather_location_TextView, e.getMessage());
            views.setViewVisibility(R.id.weather_ProgressBar, ProgressBar.INVISIBLE);
        }
    }

    protected static void update(Context context) {
        if (Locator.isPermissionGranted(context)) {
            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);

            ComponentName widget = new ComponentName(context, WeatherWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            for (int appWidgetId : appWidgetManager.getAppWidgetIds(widget)) {
                views.setOnClickPendingIntent(R.id.open_forecast_Button, getPendingSelfIntent(context, FORECAST_OnClick, appWidgetId));
                views.setOnClickPendingIntent(R.id.refresh_weather_Button, getPendingSelfIntent(context, REFRESH_OnClick, appWidgetId));
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }

            fillWidgetViews(context, views);
            Date currentDate = new Date();
            views.setTextViewText(R.id.time_TextView, LiveweatherTools.dateToString(currentDate, context.getString(R.string.time_format)));

            if (currentWeatherDate == null || !isRefreshing && LiveweatherTools.dateSubtractionInMinutes(currentWeatherDate, currentDate) >= (long) refreshWeatherInterval) {
                isRefreshing = true;
                backgroundRefreshWeather(context);
                views.setTextViewText(R.id.time_TextView, LiveweatherTools.dateToString(currentDate, context.getString(R.string.time_format)) + "*");
            }
            if (currentForecastDate == null || LiveweatherTools.dateSubtractionInMinutes(currentForecastDate, currentDate) >= (long) refreshForecastInterval) {
                backgroundRefreshForecast(context);
            }

            appWidgetManager.updateAppWidget(widget, views);
        }
    }

    protected static PendingIntent getPendingSelfIntent(Context context, String action, int appWidgetId) {
        Intent intent = new Intent(context, WeatherWidget.class);
        intent.setAction(action);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        return PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
    }

    public Disposable forceRefreshWeather(Context context) {

        WeatherForecastViewModel model = PreferenceController.createWeatherForecastProvider(context).getModel(); // WeatherForecastViewModel.getInstance(context) ;
        if (model.getWeather() != null) {
            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            ComponentName widget = new ComponentName(context, WeatherWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            views.setViewVisibility(R.id.weather_ProgressBar, ProgressBar.VISIBLE);
            appWidgetManager.updateAppWidget(widget, views);
            //disposeWeather();
            return weatherDisposable = model
                    .getWeather()
                    .doOnNext(w -> {
                        onReceiveWeather(w);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(c -> {
                        update(context);
                        }, throwable -> {
                        Log.e(LOG_TAG, throwable.getMessage());
                        }, () -> {
                            views.setViewVisibility(R.id.weather_ProgressBar, ProgressBar.INVISIBLE);
                            appWidgetManager.updateAppWidget(widget, views);
                            startService(context);
                    }
                    );
        }
        return null;
    }

    public static void backgroundRefreshForecast(Context context) {
        WeatherForecastViewModel model = PreferenceController.createWeatherForecastProvider(context).getModel();//WeatherForecastViewModel.getInstance(context) ;
        if (model.getForecast() != null) {
            model.reloadForecastDatabase();
            currentForecastDate = new Date();
        }
    }

    public static Disposable backgroundRefreshWeather(Context context) {
        WeatherForecastViewModel model = PreferenceController.createWeatherForecastProvider(context).getModel(); //WeatherForecastViewModel.getInstance(context) ;
        if (model.getWeather() != null) {
            return weatherDisposable = model
                    .getWeather()
                    .subscribe(w -> {
                                onReceiveWeather(w);

                            }, throwable -> {

                                Log.e(LOG_TAG, throwable.getMessage());

                            }, () -> {
                                isRefreshing = false;
                                currentWeatherDate = new Date();
                                update(context);
                            }
                    );
        };
        return null;
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        startService(context);
        forceRefreshWeather(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
            startService(context);
            if (!Locator.isPermissionGranted(context))

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openForecastActivity(context);
                    }
                }, 5000); //Ergonomic delay before widjet placing

    }

    @Override
    public void onDisabled(Context context) {
        disposeWeather();
        stopService(context);

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    private static void openForecastActivity(Context context) {
        Intent i = new Intent(context, ForecastActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (       REFRESH_OnClick.equals(intent.getAction())
                || FORECAST_OnClick.equals(intent.getAction())
                || AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED.equals(intent.getAction())
         ){
            int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

            //Получение ID из Bundle/////////////////////////
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mAppWidgetId = extras.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
            }

            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

                if (REFRESH_OnClick.equals(intent.getAction())) {
                    disposeWeather();
                    forceRefreshWeather(context);
                }
                else if (FORECAST_OnClick.equals(intent.getAction())) {
                    //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,  new Intent(context, ForecastActivity.class), 0);
                    openForecastActivity(context);
                } else if (AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED.equals(intent.getAction())) {
                    update(context);
                }

            }

            /////////////////////////////////////////////////
            //альтернативный способ обновления виджета через onUpdate
            //if (extras != null) {
            //    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            //    ComponentName thisAppWidget = new ComponentName(context.getPackageName(), WeatherWidget.class.getName());
            //    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            //    onUpdate(context, appWidgetManager, appWidgetIds);
            //}
        }
    }

    private static final String LOG_TAG = "WeatherWidjet";
}

