package com.github.yard01.sandbox.weatherforecast.persistence;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.github.yard01.sandbox.weatherforecast.persistence.converters.DateConverter;
import com.github.yard01.sandbox.weatherforecast.persistence.entities.WeatherForecastEntity;

@Database(entities = {WeatherForecastEntity.class}, version = 4)
@TypeConverters(DateConverter.class)
public abstract class WeatherForecastDatabase extends RoomDatabase {

    private static volatile WeatherForecastDatabase INSTANCE;

    public abstract WeatherForecastDao getWeatherForecastDao();

    public static WeatherForecastDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (WeatherForecastDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WeatherForecastDatabase.class, "weatherforecast.db")
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'forecast' ADD COLUMN 'ICON' TEXT");

        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'forecast' ADD COLUMN 'WINDDIRTEXT' TEXT");

        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'forecast' ADD COLUMN 'PROVIDER' TEXT");

        }
    };

}
