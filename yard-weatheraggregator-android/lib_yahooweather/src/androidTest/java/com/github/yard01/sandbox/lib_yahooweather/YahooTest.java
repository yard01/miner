package com.github.yard01.sandbox.lib_yahooweather;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.AndroidJUnit4;

import com.github.yard01.sandbox.lib_yahooweather.controller.YahooController;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class YahooTest {

    @SuppressLint("MissingPermission")
    private void debug(Context conext) {
        LocationManager locationManager = (LocationManager) conext.getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        });
    }


        @Test
    public void useAppContext() throws UnsupportedEncodingException {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        System.out.println("Start Test!");
        Log.d("yahoo", "start_test");
        debug(appContext);
        //Base64 b = null;

        //System.out.println(Base64.class.getCanonicalName());

        YahooController c= YahooController.getInstance(appContext);
        //c.getForecastObservable("", "").subscribe(f -> System.out.println("forecast = " +f) );
        //c.getAuthBuild();
        //c.getJson();
        //Map<String, String> map = c.getHeaders();

        //System.out.println(" _Authorization:" + map.get("Authorization"));
        System.out.println(" _URL:" + c.getUrl(37.41, 51.76, "json", "c"));

//        c.getJson();

        c.getForecastObservable( "c").subscribe(f -> System.out.println("forecast = " +f) );

        assertEquals("com.github.yard01.sandbox.lib_yahooweather.test", appContext.getPackageName());

        System.out.println("Stop Test!");

    }
}
