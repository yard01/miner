package com.github.yard01.sandbox.liveweather.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.github.yard01.sandbox.liveweather.PreferenceController;
import com.github.yard01.sandbox.weatherforecast.Weather;
import com.github.yard01.sandbox.weatherforecast.WeatherForecastProvider;
import com.github.yard01.sandbox.weatherforecast.persistence.converters.WeatherConverter;
import com.github.yard01.sandbox.weatherforecast.persistence.entities.WeatherForecastEntity;
import com.github.yard01.sandbox.weatherforecast.tools.LiveweatherTools;
import com.github.yard01.sandbox.weatherforecast.viewmodel.WeatherForecastViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.paging.PositionalDataSource;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.yard01.sandbox.liveweather.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ForecastActivity extends AppCompatActivity {
    public final static int  PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private static WeatherForecastViewModel model;
    private Weather weather;
    private TextView windTextView;
    private TextView pressTextView;
    private TextView logoTextView;
    private String providerName = "";
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout;
 //////////////////////////////////////////////////////////
    public static final int SIZE = 10;

    PagedList.Config config = new PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(SIZE)
        .build();

    public static class PagedRecyclerViewAdapter extends PagedListAdapter<WeatherForecastEntity, PagedRecyclerViewAdapter.RowViewHolder> {


        protected PagedRecyclerViewAdapter(DiffUtil.ItemCallback<WeatherForecastEntity> diffUtilCallback) {
            super(diffUtilCallback);
        }

        @NonNull
        @Override
        public RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()) //в родительское окно встраивается View с LinearLayout, на котором лежат визуальные элементы строки
                    .inflate(R.layout.forecast_row, parent, false);  //
            return new RowViewHolder(view);
        }

         @Override
        public void onBindViewHolder(@NonNull RowViewHolder holder, int position) {
            WeatherForecastEntity wfe = this.getItem(position);
            if (wfe == null) {
                holder.temperaturesView.setText(null);
            } else {

                holder.temperaturesView.setText(LiveweatherTools.getTemperatureDegString(holder.temperaturesView.getContext(), wfe.getMinTemperature(), wfe.getMaxTemperature()));
                holder.timeView.setText(LiveweatherTools.formatHH_mm.format(wfe.getDate()));
                holder.daymonthView.setText(LiveweatherTools.getDayMonthText(wfe.getDate()));
                holder.windView.setText(LiveweatherTools.getWindString(holder.windView.getContext(), wfe.getMinWindSpeed(), wfe.getMaxWindSpeed(), wfe.getWindDirectionString()));

                if (model.getWeatherDecorator() != null && wfe.getIconName() != null) {

                    int iconId = model.getWeatherDecorator().getDrawableId(wfe.getIconName());
                    if (iconId != -1 && iconId != 0) { // It's using default icons
                        //holder.imageView.setTin
                        holder.imageView.setImageResource(iconId);
                        holder.imageView.setColorFilter(Color.GRAY);
                        
                    }
                    else // load custom icons
                        Single.just(wfe.getIconName())
                            .subscribeOn(Schedulers.computation())
                            .map(iconName -> {
                                Bitmap bitmap = model.getWeatherDecorator().getBitmap(iconName);
                                if (bitmap != null) return bitmap;
                                else return Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap ->

                                    holder.imageView.setImageBitmap(bitmap)

                            );

                }

            }
        }


        //"держатель" строки
        class RowViewHolder extends RecyclerView.ViewHolder {
            final TextView temperaturesView;
            final TextView timeView;
            final TextView daymonthView;
            final TextView windView;
            final ImageView imageView;
            RowViewHolder(View view) { //конструктор
                super(view);
                // ищем контролы по идентификаторам на View
                // они описаны в module_row_content.xmll, который представляет собой горизонтальный LinearLayout с двумя TextView:
                // [@+id/id_text][@+id/content]
                //mIdView = (TextView) view.findViewById(R.id.id_text);
                temperaturesView = (TextView) view.findViewById(R.id.temperatures_TextView);
                timeView = (TextView) view.findViewById(R.id.time_TextView);
                daymonthView =  (TextView) view.findViewById(R.id.daymonth_TextView);
                imageView = (ImageView) view.findViewById(R.id.forecast_icon_ImageView);
                windView = (TextView) view.findViewById(R.id.wind_TextView);
            }
            public void bind(WeatherForecastEntity wfe) {

            }
        }
    }

    class MainThreadExecutor implements Executor {
        private final Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mHandler.post(command);
        }
    }

    class WeatherDiffUtilCallback extends DiffUtil.ItemCallback<WeatherForecastEntity> {
        @Override
        public boolean areItemsTheSame(@NonNull WeatherForecastEntity oldItem, @NonNull WeatherForecastEntity newItem) {
            //id control
            return oldItem.getDate().equals(newItem.getDate());
        }

        @Override
        public boolean areContentsTheSame(@NonNull WeatherForecastEntity oldItem, @NonNull WeatherForecastEntity newItem) {
            //content control
            return oldItem.getMaxTemperature() == newItem.getMaxTemperature();
        }
    }

    WeatherDiffUtilCallback diffUtilCallback = new WeatherDiffUtilCallback();

    @SuppressLint("MissingPermission")
    private void debugLocationRequest() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override public void onLocationChanged(Location location) {}
            @Override public void onStatusChanged(String s, int i, Bundle bundle) {}
            @Override public void onProviderEnabled(String s) {}
            @Override public void onProviderDisabled(String s) {}
        });


    }

    protected void createContent() {
        toolbar = (Toolbar) findViewById(R.id.forecast_toolbar);
        toolbar.setTitle(R.string.three_dots);
        windTextView = findViewById(R.id.wind_speed_TextView);
        pressTextView = findViewById(R.id.press_TextView);
        logoTextView = findViewById(R.id.logo_name_TextView);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        final PagedRecyclerViewAdapter pagedAdapter = new PagedRecyclerViewAdapter(diffUtilCallback);
        final RecyclerView recyclerView = findViewById(R.id.forecast_RecyclerView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Animation ranim = (Animation) AnimationUtils.loadAnimation(ForecastActivity.this, R.anim.rotater);
                view.setAnimation(ranim);
                model.reloadForecastDatabase(() -> recyclerView.setAdapter(pagedAdapter));

            }
        });

        WeatherForecastProvider provider = PreferenceController.createWeatherForecastProvider(this);
        providerName = provider.getName();//
        model = provider.getModel();
        model.getWeatherEntities().observe(this, pagedAdapter::submitList);
        logoTextView.setText(provider.getName());
//////////////////////////////////////////////////
        recyclerView.setAdapter(pagedAdapter);//new SimpleItemRecyclerViewAdapter(this, ModuleList.ITEMS, mTwoPane))
        getWeather();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        permissionRequest();
    }

    private void showWeather() {
        if (weather == null) return;
        pressTextView.setText(LiveweatherTools.getPressureString(this, weather.getAtmPressure()));
        windTextView.setText(LiveweatherTools.getWindString(this, weather.getMinWindSpeed(), weather.getWindDirection()));
        String location = "";
        if (weather.getLocationName() != null && !"".equals(weather.getLocationName())) location = ", " + weather.getLocationName();
        toolbarLayout.setTitle(LiveweatherTools.getTemperatureDegString(this, weather.getMaxTemperature()) + location);
    }

    private void getWeather() {
       model.getWeather()
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(w -> {
                           weather = w;
                           showWeather();
                       },
                       trowable -> {
                            model.getOfflineWeather().observeOn(AndroidSchedulers.mainThread()).subscribe(w -> {
                                weather = WeatherConverter.toWeather(w);
                                showWeather();
                            });

                       }, () -> showWeather()

       );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.preferences);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.forecast_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return true;
    }


    private void permissionRequest() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.


            } else {
//                // No explanation needed; request the permission

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION); //MY_PERMISSIONS_REQUEST_READ_CONTACTS

//                        Toast.makeText(this, "Request Permissions", Toast.LENGTH_LONG);
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
            }
        } else {
            createContent();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    createContent();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        WeatherForecastProvider provider = PreferenceController.createWeatherForecastProvider(this);
        if (provider != null) {
            if ("".equals(providerName) || this.providerName.equals(provider.getName())) return;
            permissionRequest();
        }
    }
}
