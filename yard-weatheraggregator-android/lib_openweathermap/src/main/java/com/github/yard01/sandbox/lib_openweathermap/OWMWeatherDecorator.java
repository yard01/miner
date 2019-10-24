package com.github.yard01.sandbox.lib_openweathermap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.yard01.sandbox.weatherforecast.WeatherDecorator;
import com.github.yard01.sandbox.weatherforecast.tools.LiveweatherTools;

public class OWMWeatherDecorator implements WeatherDecorator {
    private static final String ICON_FILE_PREFIX = "OWM_";
    private static final String ICON_FILE_TYPE = ".png";
    private static BitmapFactory.Options options = new BitmapFactory.Options();
    private final int ERGONOMIC_ICON_BORDER = 8;
    private Context context;

    public OWMWeatherDecorator(Context context) {

        this.context = context;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inScaled = false;
    }

    public static String getOWMIconFileName(String iconName) {
        return ICON_FILE_PREFIX + iconName + ICON_FILE_TYPE;
    }

    public static String getOWMIconServerPath(String iconName) {
        return iconName + ICON_FILE_TYPE;
    }

    public static Boolean isIconExists(Context _context, String iconName) {
        return _context.getFileStreamPath(getOWMIconFileName(iconName)).exists();
    }

    @Override
    public String getIconFileName(String iconName) {
        return getOWMIconFileName(iconName);
    }

    @Override
    public String getIconFilePath(String iconName) {
        String fname = getIconFileName(iconName);
        return context.getFileStreamPath(fname).getAbsolutePath();
    }

    @Override
    public Boolean isIconExists(String iconName) {
        String fname = getIconFileName(iconName);
        return context.getFileStreamPath(fname).exists();
    }

    private Bitmap ergonomicScale(Bitmap bitmap) {
        int newWidth = bitmap.getWidth() - ERGONOMIC_ICON_BORDER;
        int newHeight = bitmap.getHeight() - ERGONOMIC_ICON_BORDER;
        int[] pixels = new int[newWidth * newHeight];
        bitmap.getPixels(pixels, 0, newWidth, ERGONOMIC_ICON_BORDER, ERGONOMIC_ICON_BORDER, newWidth, newHeight);
        Bitmap result = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        result.setPixels(pixels, 0, newWidth, 0, 0, newWidth, newHeight);
        return Bitmap.createScaledBitmap(result, bitmap.getWidth(), bitmap.getHeight(), true);
    }

    @Override
    public Bitmap getBitmap(String iconName) {
        String fname = getIconFileName(iconName);
        if (LiveweatherTools.fileExists(context, fname)) {
            Bitmap bitmap = BitmapFactory.decodeFile(context.getFileStreamPath(fname).getAbsolutePath(), options);
            //int[] pixels = new int[halfWidth * halfHeight];
            //Bitmap.
            //bitmap = Bitmap.createBitmap(bitmap, 20, 20, bitmap.getWidth() - 20, bitmap.getHeight() - 20);
            //Bitmap.cr
            return ergonomicScale(bitmap);//Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*2), (int)(bitmap.getHeight()*2), true);
        } else return null;
    }

    @Override
    public int getDrawableId(String iconName) {
        return -1;
    }
}
