package com.github.yard01.sandbox.lib_yahooweather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;

import androidx.core.content.ContextCompat;

import com.github.yard01.sandbox.weatherforecast.WeatherDecorator;

public class YahooDecorator implements WeatherDecorator {
    private Context context;

    public YahooDecorator(Context context) {
        this.context = context;
    }

    @Override
    public String getIconFileName(String iconName) {
        return null;
    }

    @Override
    public String getIconFilePath(String iconName) {

        return null;
    }

    @Override
    public Boolean isIconExists(String iconName) {

        return true;
    }

    @Override
    public Bitmap getBitmap(String iconName) {
        return null;
    }

    @Override
    public int getDrawableId(String iconName) {
        int icon = -1;
        switch(iconName) {
            case "0":  icon  = R.drawable.ic_wi_tornado;
                break;
            case "1": icon  = R.drawable.ic_wi_storm_showers;
                break;
            case "2": icon  = R.drawable.ic_wi_tornado;
                break;
            case "3": icon  = R.drawable.ic_wi_thunderstorm;
                break;
            case "4": icon  = R.drawable.ic_wi_thunderstorm;
                break;
            case "5": icon  = R.drawable.ic_wi_snow;
                break;
            case "6": icon  = R.drawable.ic_wi_rain_mix;
                break;
            case "7": icon  = R.drawable.ic_wi_rain_mix;
                break;
            case "8": icon  = R.drawable.ic_wi_sprinkle;
                break;
            case "9": icon  = R.drawable.ic_wi_sprinkle;
                break;
            case "10": icon  = R.drawable.ic_wi_hail;
                break;
            case "11": icon  = R.drawable.ic_wi_showers;
                break;
            case "12": icon  = R.drawable.ic_wi_showers;
                break;
            case "13": icon  = R.drawable.ic_wi_snow;
                break;
            case "14": icon  = R.drawable.ic_wi_storm_showers;
                break;
            case "15": icon  = R.drawable.ic_wi_snow;
                break;
            case "16": icon  = R.drawable.ic_wi_snow;
                break;
            case "17": icon  = R.drawable.ic_wi_hail;
                break;
            case "18": icon  = R.drawable.ic_wi_hail;
                break;
            case "19": icon  = R.drawable.ic_wi_cloudy_gusts;
                break;
            case "20": icon  = R.drawable.ic_wi_fog;
                break;
            case "21": icon  = R.drawable.ic_wi_fog;
                break;
            case "22": icon  = R.drawable.ic_wi_fog;
                break;
            case "23": icon  = R.drawable.ic_wi_cloudy_gusts;
                break;
            case "24": icon  = R.drawable.ic_wi_cloudy_windy;
                break;
            case "25": icon  = R.drawable.ic_wi_thermometer;
                break;
            case "26": icon  = R.drawable.ic_wi_cloudy;
                break;
            case "27": icon  = R.drawable.ic_wi_night_cloudy;
                break;
            case "28": icon  = R.drawable.ic_wi_day_cloudy;
                break;
            case "29": icon  = R.drawable.ic_wi_night_cloudy;
                break;
            case "30": icon  = R.drawable.ic_wi_day_cloudy;
                break;
            case "31": icon  = R.drawable.ic_wi_night_clear;
                break;
            case "32": icon  = R.drawable.ic_wi_day_sunny;
                break;
            case "33": icon  = R.drawable.ic_wi_night_clear;
                break;
            case "34": icon  = R.drawable.ic_wi_day_sunny_overcast;
                break;
            case "35": icon  = R.drawable.ic_wi_hail;
                break;
            case "36": icon  = R.drawable.ic_wi_day_sunny;
                break;
            case "37": icon  = R.drawable.ic_wi_thunderstorm;
                break;
            case "38": icon  = R.drawable.ic_wi_thunderstorm;
                break;
            case "39": icon  = R.drawable.ic_wi_thunderstorm;
                break;
            case "40": icon  = R.drawable.ic_wi_storm_showers;
                break;
            case "41": icon  = R.drawable.ic_wi_snow;
                break;
            case "42": icon  = R.drawable.ic_wi_snow;
                break;
            case "43": icon  = R.drawable.ic_wi_snow;
                break;
            case "44": icon  = R.drawable.ic_wi_cloudy;
                break;
            case "45": icon  = R.drawable.ic_wi_lightning;
                break;
            case "46": icon  = R.drawable.ic_wi_snow;
                break;
            case "47": icon  = R.drawable.ic_wi_thunderstorm;
                break;
            case "3200": icon  =  R.drawable.ic_wi_cloud;
                break;
            default: icon  =  R.drawable.ic_wi_cloud;
                break;
        }
        return icon;
    }
/*
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
   VectorDrawable vectorDrawable = (VectorDrawable) drawable;
} else {
   BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
}
    public static Bitmap getBitmapFromVectorDrawable(Context context, int getDrawableId) {

        Drawable drawable = ContextCompat.getDrawable(context, getDrawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }*/
}
