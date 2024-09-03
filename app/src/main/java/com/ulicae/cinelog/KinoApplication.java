package com.ulicae.cinelog;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.room.Room;

import com.ulicae.cinelog.data.ProdOpenHelper;
import com.ulicae.cinelog.data.dao.DaoMaster;
import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.io.exportdb.AutomaticExportException;
import com.ulicae.cinelog.io.exportdb.AutomaticExporter;
import com.ulicae.cinelog.io.exportdb.exporter.ReviewCsvExporterFactory;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.utils.ThemeWrapper;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

/**
 * CineLog Copyright 2018 Pierre Rognon
 * kinolog Copyright (C) 2017  ryan rigby
 * <p>
 * <p>
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 */
public class KinoApplication extends Application {

    ProdOpenHelper helper;
    SQLiteDatabase db;
    DaoMaster daoMaster;
    DaoSession daoSession;
    AppDatabase appDb;

    @Override
    public void onCreate() {
        super.onCreate();
        new ThemeWrapper().setThemeWithPreferences(this);

        helper = new ProdOpenHelper(this, "notes-db");
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        this.appDb = Room
                .databaseBuilder(getApplicationContext(), AppDatabase.class, "database-cinelog")
                .build();
        verifyAutomaticSave();
    }

    private void verifyAutomaticSave() {
        try {
            new AutomaticExporter(this, new ReviewCsvExporterFactory(this, ItemEntityType.MOVIE), "movie")
                    .tryExport()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (success) -> {
                                Toast.makeText(getApplicationContext(), getString(R.string.automatic_export_movie_toast), Toast.LENGTH_SHORT).show();
                            },
                            e -> {
                                Toast.makeText(getApplicationContext(), getString(R.string.automatic_export_cant_export_movie), Toast.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(), getString(((AutomaticExportException) e).getStringCode()), Toast.LENGTH_LONG).show();
                            }
                    );
        } catch (AutomaticExportException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.automatic_export_cant_export_movie), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), getString(e.getStringCode()), Toast.LENGTH_LONG).show();
        }

        try {
            new AutomaticExporter(this, new ReviewCsvExporterFactory(this, ItemEntityType.SERIE), "serie")
                    .tryExport()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (success) -> {
                                Toast.makeText(getApplicationContext(), getString(R.string.automatic_export_serie_toast), Toast.LENGTH_SHORT).show();
                            },
                            e -> {
                                Toast.makeText(getApplicationContext(), getString(R.string.automatic_export_cant_export_serie), Toast.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(), getString(((AutomaticExportException) e).getStringCode()), Toast.LENGTH_LONG).show();
                            }
                    );
            ;
        } catch (AutomaticExportException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.automatic_export_cant_export_serie), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), getString(e.getStringCode()), Toast.LENGTH_LONG).show();
        }
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public AppDatabase getDb() {
        return appDb;
    }
}