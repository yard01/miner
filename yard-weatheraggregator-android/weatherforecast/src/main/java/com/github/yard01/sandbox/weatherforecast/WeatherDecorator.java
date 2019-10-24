package com.github.yard01.sandbox.weatherforecast;

import android.content.Context;
import android.graphics.Bitmap;

public interface WeatherDecorator {
    public String getIconFileName(String iconName);
    public String getIconFilePath(String iconName);
    public Boolean isIconExists(String iconName);
    public Bitmap getBitmap(String iconName);
    public int getDrawableId(String iconName);
}
