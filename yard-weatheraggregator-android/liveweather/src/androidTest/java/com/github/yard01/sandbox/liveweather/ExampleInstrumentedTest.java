package com.github.yard01.sandbox.liveweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.github.yard01.sandbox.weatherforecast.WeatherForecastProvider;
import com.github.yard01.sandbox.weatherforecast.WeatherForecastProviderFactory;
import com.github.yard01.sandbox.weatherforecast.persistence.LocalWeatherDatasource;
import com.github.yard01.sandbox.weatherforecast.persistence.WeatherForecastDatabase;
import com.github.yard01.sandbox.weatherforecast.tools.LiveweatherTools;
import com.github.yard01.sandbox.weatherforecast.viewmodel.WeatherForecastViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private void testModel(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        //prefs.edit().clear().commit();

        String factoryClassName = prefs.getString(WeatherForecastProviderFactory.FORECASTFACTORY_PREFERENCE, "");

        WeatherForecastViewModel model = WeatherForecastViewModel.getInstance(context);

        WeatherForecastDatabase db = WeatherForecastDatabase.getInstance(context);
        LocalWeatherDatasource datasource = new LocalWeatherDatasource(db.getWeatherForecastDao());
        model.setDatasource(datasource);

        WeatherForecastProviderFactory factory = WeatherForecastProviderFactory.buildFactory(context, factoryClassName);
            //currentWeatherForecastProvider = factory.getWeatherForecastProvider();
        System.out.println("weather_test " +  "*" + factory); //, "*" + factory);
        CompositeDisposable mDisposable = new CompositeDisposable();
        try {
            WeatherForecastProvider currentWeatherForecastProvider = factory.getWeatherForecastProvider();
            Log.d("weather_engine", "begin");
            mDisposable.add(
                    currentWeatherForecastProvider.subscribe(s -> { //подписались на получение прогноза погоды

                if (model.getWeather() != null)
                    mDisposable.add(
                    model
                            .getWeather()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(c -> {
                                System.out.println("Temperature = " + c.getMaxTemperature() +", icon = " + c.getIconName());

                                //temperatureTextView.setText(getTemperatureString(c.getMaxTemperature()));
                                //progressBar.setVisibility(ProgressBar.INVISIBLE);
                            }));

            }));
            Thread.sleep(20000);
            mDisposable.dispose();
            //);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        String fname = "ttt.png";

        //appContext.getDrawable(R.drawable.example_appwidget_preview).;
        try {
            InputStream is = appContext.getAssets().open("test.png");
            LiveweatherTools.saveFile(appContext,is, fname);
        } catch (IOException e) {
            e.printStackTrace();
        }

        testModel(appContext);
        System.out.println("Does " + fname+ " exists?: " + LiveweatherTools.fileExists(appContext, fname));

        assertEquals("com.github.yard01.sandbox.liveweather", appContext.getPackageName());
    }
}
