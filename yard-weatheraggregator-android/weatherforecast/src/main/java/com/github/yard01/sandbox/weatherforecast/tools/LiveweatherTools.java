package com.github.yard01.sandbox.weatherforecast.tools;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.yard01.sandbox.weatherforecast.R;
import com.github.yard01.sandbox.weatherforecast.persistence.converters.WeatherConverter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LiveweatherTools {

    private static final int READ_BUFFER_SIZE = 1024;
    private static final Calendar calendar = Calendar.getInstance();
    private static final String cels = "°C";
    private static final String fahr = "°F";

    public static SimpleDateFormat formatHH_mm = new SimpleDateFormat("HH:mm");

    public static String getTemperatureString(float temperature) {
        return Float.toString(temperature);
    }

    private static String getSignedTemperatureString(float temperature) {
        if (temperature <= 0) return Float.toString(temperature); else return "+" + temperature;
    }

    public static String getTemperatureString(float min_temperature, float max_temperature) {
        StringBuilder sb = new StringBuilder();
        sb.append(getSignedTemperatureString(min_temperature));
        sb.append(" ... ");
        sb.append(getSignedTemperatureString(max_temperature));
        return sb.toString();
    }

    public static String getTemperatureDegString(Context context, float min_temperature, float max_temperature) {
        return getTemperatureString(min_temperature, max_temperature) + context.getString(R.string.celsius_deg);
    }

    public static String getTemperatureDegString(Context context, float temperature) {
        return getSignedTemperatureString(temperature) +  context.getString(R.string.celsius_deg);
    }

    public static String getPressureString(Context context, float pressure) {
        return String.valueOf(pressure) + " " + context.getString(R.string.pressure_unit);
    }

    public static String getDayMonthText(Date date) {
        calendar.setTime(date);
        String result = calendar.get(Calendar.DAY_OF_MONTH) + " " +
                        calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        //Month m = Month.of(calendar.get(Calendar.MONTH));

        return result;
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static void saveFile(Context context, InputStream inputStream, String fileName) throws IOException {
        OutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        byte[] buffer = new byte[READ_BUFFER_SIZE];
        int readed = 0;
        while ((readed = inputStream.read(buffer)) != -1 ) {
            outputStream.write(buffer, 0, readed);
        }
        outputStream.close();

    }

    public static boolean fileExists(Context context, String fileName) {
        return context.getFileStreamPath(fileName).exists();
    }

    public static String getWindString(Context context, float speed, float degree) {

        return WeatherConverter.getWindDirectionString(context, degree) +", " + speed +" " +context.getString(R.string.speed_unit);
    }

    public static String getWindString(Context context, float speed, String direction) {

        return direction + ", " + speed +" " +context.getString(R.string.speed_unit);
    }

    public static String getWindString(Context context, float min_speed, float max_speed, String direction) {
        if (max_speed == min_speed)
            return getWindString(context, max_speed, direction);
        return direction + ", " + min_speed +" ... " + max_speed + " " + context.getString(R.string.speed_unit);
    }

    public static long dateSubtractionInMinutes(Date date1, Date date2) {
        if (date1 == null || date2 == null) return 0l;
        long time_result = Math.abs(date1.getTime() - date2.getTime());
        return (long) time_result/60000;

    }


}


