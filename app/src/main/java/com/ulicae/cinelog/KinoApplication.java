package com.ulicae.cinelog;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.ulicae.cinelog.data.ProdOpenHelper;
import com.ulicae.cinelog.data.dao.DaoMaster;
import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.io.exportdb.AutomaticExportException;
import com.ulicae.cinelog.io.exportdb.AutomaticExporter;
import com.ulicae.cinelog.io.exportdb.exporter.MovieCsvExporterFactory;
import com.ulicae.cinelog.io.exportdb.exporter.SerieCsvExporterFactory;
import com.ulicae.cinelog.utils.ThemeWrapper;

/**
 * CineLog Copyright 2018 Pierre Rognon
 * kinolog Copyright (C) 2017  ryan rigby
 *
 *
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 *
 */
public class KinoApplication extends Application {

    ProdOpenHelper helper;
    SQLiteDatabase db;
    DaoMaster daoMaster;
    DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        new ThemeWrapper().setThemeWithPreferences(this);

        helper = new ProdOpenHelper(this, "notes-db");
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        verifyAutomaticSave();
    }

    private void verifyAutomaticSave() {
        try {
            if(new AutomaticExporter(this, new MovieCsvExporterFactory(this), "movie").tryExport()){
                Toast.makeText(getApplicationContext(), getString(R.string.automatic_export_movie_toast), Toast.LENGTH_LONG).show();
            }
        } catch (AutomaticExportException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.automatic_export_cant_export_movie), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), getString(e.getStringCode()), Toast.LENGTH_LONG).show();
        }

        try {
            if(new AutomaticExporter(this, new SerieCsvExporterFactory(this), "serie").tryExport()){
                Toast.makeText(getApplicationContext(), getString(R.string.automatic_export_serie_toast), Toast.LENGTH_LONG).show();
            }
        } catch (AutomaticExportException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.automatic_export_cant_export_serie), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), getString(e.getStringCode()), Toast.LENGTH_LONG).show();
        }
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}