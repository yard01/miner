package com.github.yard01.sandbox.weatherforecast;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

public class Locator {
    private static Locator instance;
    LocationManager locationManager;
    Context context;
    private Locator(Context context) {

        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

    }

    public static Locator getInstance(Context context) {
        if (instance == null)
            instance = new Locator(context);

        return instance;
    }


    public static boolean isPermissionGranted(Context context) {
        return (PermissionChecker.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED || PermissionChecker.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) ;
    }

    private Location getLocationByProvider(String provider) {
        //if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR)
        //        != PackageManager.PERMISSION_GRANTED) {
        //    // Permission is not granted
        //
        //}
        if ( PermissionChecker.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED || PermissionChecker.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {
            return locationManager.getLastKnownLocation(provider);
        }
        return null;

    }

    public Location getPassiveLocation() {
        return getLocationByProvider(LocationManager.PASSIVE_PROVIDER);
    }

    public Location getGpsLocation() {
        return getLocationByProvider(LocationManager.GPS_PROVIDER);
    }

    public Location getNetworkLocation() {
        return getLocationByProvider(LocationManager.NETWORK_PROVIDER);
    }

    public Location getEnabledLocation() {
        Location location =  getPassiveLocation();
        if (location != null) return location; else location = getNetworkLocation();
        if (location != null) return location; else location = getGpsLocation();
        return location;
    }

}
