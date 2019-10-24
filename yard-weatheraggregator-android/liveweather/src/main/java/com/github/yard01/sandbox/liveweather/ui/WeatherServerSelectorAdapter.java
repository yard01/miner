package com.github.yard01.sandbox.liveweather.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.github.yard01.sandbox.liveweather.R;
import com.github.yard01.sandbox.weatherforecast.WeatherForecastProviderFactory;

import java.util.ArrayList;
import java.util.List;

public class WeatherServerSelectorAdapter implements SpinnerAdapter {

    WeatherForecastProviderFactory[] factories;

    public WeatherServerSelectorAdapter(Context context) {
        factories = new WeatherForecastProviderFactory[context.getResources().getStringArray(R.array.forecastfactoryclasses).length];
        int i = 0;
        for (String className : context.getResources().getStringArray(R.array.forecastfactoryclasses)) {
            factories[i] = WeatherForecastProviderFactory.buildFactory(context, className);
            i++;
        };
    }

    private View getSelectorView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()) //в родительское окно встраивается View с LinearLayout, на котором лежат визуальные элементы строки
                .inflate(R.layout.weather_server_selector, parent, false);
        TextView tv = view.findViewById(R.id.weatherServerName_textView);
        tv.setText(factories[position].getFactoryName());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getSelectorView(position, parent);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return factories.length;
    }

    @Override
    public Object getItem(int position) {
        return factories[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()) //в родительское окно встраивается View с LinearLayout, на котором лежат визуальные элементы строки
                .inflate(R.layout.weather_server_selector, parent, false);
        TextView tv = view.findViewById(R.id.weatherServerName_textView);
        tv.setText(factories[position].getFactoryName());
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public WeatherForecastProviderFactory getData(int position) {
        return factories[position];
    }

    public int getPosition(String key) {
        for (int i = 0; i < factories.length; i++) if (factories[i].getClass().getName().equals(key)) return i;

        return -1;
    }

}
