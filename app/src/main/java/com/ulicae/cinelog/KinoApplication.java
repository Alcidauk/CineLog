package com.ulicae.cinelog;

import android.app.Application;
import android.widget.Toast;

import androidx.room.Room;

import com.ulicae.cinelog.io.exportdb.AutomaticExportException;
import com.ulicae.cinelog.io.exportdb.AutomaticExporter;
import com.ulicae.cinelog.io.exportdb.exporter.ReviewCsvExporterFactory;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.utils.ThemeWrapper;

import org.acra.ACRA;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.DialogConfigurationBuilder;
import org.acra.config.MailSenderConfigurationBuilder;
import org.acra.data.StringFormat;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * CineLog Copyright 2024 Pierre Rognon
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

    AppDatabase appDb;

    private Map<String, Disposable> disposableMap;

    /**
     * Just add this quick snippet to the build method to get queries in app log
     *   .setQueryCallback(
     *        (s, list) -> System.out.println("SQL Query: "+ s + " SQL Args: " + list),
     *        Executors.newSingleThreadExecutor()
     *   )
     */
    @Override
    public void onCreate() {
        super.onCreate();
        new ThemeWrapper().setThemeWithPreferences(this);

        this.disposableMap = new HashMap<>();
        this.appDb = Room
                .databaseBuilder(getApplicationContext(), AppDatabase.class, "database-cinelog")
                .build();

        ACRA.init(this, new CoreConfigurationBuilder()
                .withBuildConfigClass(BuildConfig.class)
                .withReportFormat(StringFormat.JSON)
                .withPluginConfigurations(
                        new DialogConfigurationBuilder()
                                .withText(getString(R.string.crash_dialog_text))
                                .withTitle(getString(R.string.crash_dialog_title))
                                .withPositiveButtonText(getString(R.string.crash_dialog_positive))
                                .withNegativeButtonText(getString(R.string.dialog_dialog_negative))
                                .build(),
                        new MailSenderConfigurationBuilder()
                                .withMailTo("cinelog@ulicae.com")
                                .withReportAsFile(true)
                                .withReportFileName("Crash.txt")
                                .withSubject(getString(R.string.crash_mail_subject))
                                .withBody(getString(R.string.crash_mail_body))
                                .build()
                )
        );

        verifyAutomaticSave();
    }

    private void verifyAutomaticSave() {
        try {
            this.disposableMap.put(
                    "movie",
                    new AutomaticExporter<>(this, new ReviewCsvExporterFactory(this, ItemEntityType.MOVIE), "movie")
                            .tryExport()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    (success) -> {
                                        Toast.makeText(getApplicationContext(), getString(R.string.automatic_export_movie_toast), Toast.LENGTH_SHORT).show();
                                        this.clearDisposable("movie");
                                    },

                                    e -> {
                                        Toast.makeText(getApplicationContext(), getString(R.string.automatic_export_cant_export_movie), Toast.LENGTH_LONG).show();
                                        Toast.makeText(getApplicationContext(), getString(((AutomaticExportException) e).getStringCode()), Toast.LENGTH_LONG).show();
                                    }
                            )
            );
        } catch (AutomaticExportException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.automatic_export_cant_export_movie), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), getString(e.getStringCode()), Toast.LENGTH_LONG).show();
        }

        try {
            this.disposableMap.put(
                    "serie",
                    new AutomaticExporter<>(this, new ReviewCsvExporterFactory(this, ItemEntityType.SERIE), "serie")
                            .tryExport()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    (success) -> {
                                        Toast.makeText(getApplicationContext(), getString(R.string.automatic_export_serie_toast), Toast.LENGTH_SHORT).show();
                                        this.clearDisposable("serie");
                                    },
                                    e -> {
                                        Toast.makeText(getApplicationContext(), getString(R.string.automatic_export_cant_export_serie), Toast.LENGTH_LONG).show();
                                        Toast.makeText(getApplicationContext(), getString(((AutomaticExportException) e).getStringCode()), Toast.LENGTH_LONG).show();
                                    }
                            )
            );
        } catch (AutomaticExportException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.automatic_export_cant_export_serie), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), getString(e.getStringCode()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        this.disposableMap.clear();
    }

    /**
     * Remove disposable with the name given from the referencing map,
     * after disposing the disposable
     * @param name the name in the map
     */
    private void clearDisposable(String name) {
        Disposable disposable = this.disposableMap.get(name);
        if(disposable != null) {
            disposable.dispose();
        }
        this.disposableMap.remove(name);
    }

    public AppDatabase getDb() {
        return appDb;
    }
}