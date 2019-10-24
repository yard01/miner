package com.github.yard01.sandbox.weatherforecast.persistence;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.github.yard01.sandbox.weatherforecast.persistence.entities.WeatherForecastEntity;

import java.util.Date;


import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface WeatherForecastDao {

    @Query("SELECT * FROM forecast")
    Flowable<WeatherForecastEntity> getForecast();

    @Query("SELECT * FROM forecast ORDER BY dt")
    DataSource.Factory<Integer, WeatherForecastEntity> getPagedForecast();

    @Query("SELECT * FROM forecast WHERE provider = :providerId ORDER BY dt")
    DataSource.Factory<Integer, WeatherForecastEntity> getPagedForecast(String providerId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertForecast(WeatherForecastEntity forecast);

    @Query("DELETE FROM forecast")
    Completable deleteAll();

    //@Query("DELETE FROM forecast")
    //void deleteAll();
    @Query("DELETE FROM forecast WHERE DT < :forecastDate")
    Completable deleteOldRecords(Date forecastDate);

    @Query("with near_dates as (\n" +
            "    select max(f.dt) dt from forecast f where dt <= :forecastDate and provider = :providerId \n" +
            "    union all\n" +
            "    select min(f.dt) dt from forecast f where dt >= :forecastDate and provider = :providerId \n" +
            ")\n" +
            "select \n" +
            "    :forecastDate dt, \n" +
            "    round(avg(f.HUMIDITY),1) HUMIDITY,\n" +
            "    max(ICON) icon,\n" +
            "    round(avg(MAXT),1) MAXT,\n" +
            "    round(avg(MINT),1) MINT,\n" +
            "    round(avg(MAXWINDSPEED),1) MAXWINDSPEED,\n" +
            "    round(avg(MINWINDSPEED),1) MINWINDSPEED,\n" +
            "    round(avg(PRESSURE),1) PRESSURE,\n" +
            "    round(avg(WINDDIR),1) WINDDIR,\n" +
            "    max(WINDDIRTEXT) WINDDIRTEXT \n" +
            "from forecast f inner join near_dates nd on f.DT = nd.dt; \n")
    Flowable<WeatherForecastEntity> getAvgForecast(Date forecastDate, String providerId);


    @Query("with near_dates as (\n" +
            "    select max(f.dt) dt from forecast f where dt <= :forecastDate  \n" +
            "    union all\n" +
            "    select min(f.dt) dt from forecast f where dt >= :forecastDate  \n" +
            ")\n" +
            "select \n" +
            "    :forecastDate dt, \n" +
            "    round(avg(f.HUMIDITY),1) HUMIDITY,\n" +
            "    max(ICON) icon,\n" +
            "    round(avg(MAXT),1) MAXT,\n" +
            "    round(avg(MINT),1) MINT,\n" +
            "    round(avg(MAXWINDSPEED),1) MAXWINDSPEED,\n" +
            "    round(avg(MINWINDSPEED),1) MINWINDSPEED,\n" +
            "    round(avg(PRESSURE),1) PRESSURE,\n" +
            "    round(avg(WINDDIR),1) WINDDIR,\n" +
            "    max(WINDDIRTEXT) WINDDIRTEXT \n" +
            "from forecast f inner join near_dates nd on f.DT = nd.dt; \n")
    Flowable<WeatherForecastEntity> getAvgForecast(Date forecastDate);

}
