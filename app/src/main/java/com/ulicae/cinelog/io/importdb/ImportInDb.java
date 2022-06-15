package com.ulicae.cinelog.io.importdb;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.services.reviews.KinoService;
import com.ulicae.cinelog.data.services.reviews.SerieService;
import com.ulicae.cinelog.data.services.tags.TagService;
import com.ulicae.cinelog.data.services.wishlist.MovieWishlistService;
import com.ulicae.cinelog.data.services.wishlist.SerieWishlistService;
import com.ulicae.cinelog.io.importdb.builder.KinoDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.SerieDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.TagDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.WishlistDtoFromRecordBuilder;
import com.ulicae.cinelog.utils.ThemeWrapper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * CineLog Copyright 2018 Pierre Rognon
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
public class ImportInDb extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.import_tags_status_waiting)
    TextView tag_status;
    @BindView(R.id.import_movies_status_waiting)
    TextView movie_status;
    @BindView(R.id.import_series_status_waiting)
    TextView serie_status;
    @BindView(R.id.import_wishlist_movies_status_waiting)
    TextView wishlist_movie_status;
    @BindView(R.id.import_wishlist_series_status_waiting)
    TextView wishlist_serie_status;

    @BindView(R.id.import_tags_error_message)
    TextView tag_error;
    @BindView(R.id.import_movies_error_message)
    TextView movie_error;
    @BindView(R.id.import_series_error_message)
    TextView serie_error;
    @BindView(R.id.import_wishlist_movies_error_message)
    TextView wishlist_movie_error;
    @BindView(R.id.import_wishlist_series_error_message)
    TextView wishlist_serie_error;

    private Boolean writeStoragePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        ActivityCompat.requestPermissions(ImportInDb.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        setContentView(R.layout.activity_import_db);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.import_db_button)
    public void onClick(View view) {
        if (writeStoragePermission != null && writeStoragePermission) {
            Toast.makeText(getApplicationContext(), getString(R.string.import_starting_toast), Toast.LENGTH_SHORT).show();

            try {
                new CsvImporter<>(
                        new FileReaderGetter(getApplication()),
                        new DtoImportCreator<>(this, new TagDtoFromRecordBuilder(this)),
                        new TagService(((KinoApplication) getApplication()).getDaoSession()),
                        this).importCsvFile("import_tags.csv");

                tag_status.setText(R.string.import_status_success);
            } catch (ImportException e) {
                Toast.makeText(getApplication().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                tag_status.setText(R.string.import_status_failed);
                tag_error.setText(e.getMessage());
            }


            try {
                new CsvImporter<>(
                        new FileReaderGetter(getApplication()),
                        new DtoImportCreator<>(this, new KinoDtoFromRecordBuilder(this)),
                        new KinoService(((KinoApplication) getApplication()).getDaoSession()),
                        this).importCsvFile("import_movies.csv");

                movie_status.setText(R.string.import_status_success);
            } catch (ImportException e) {
                Toast.makeText(getApplication().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                movie_status.setText(R.string.import_status_failed);
                movie_error.setText(e.getMessage());
            }

            try {
                new CsvImporter<>(
                        new FileReaderGetter(getApplication()),
                        new DtoImportCreator<>(this, new SerieDtoFromRecordBuilder(this)),
                        new SerieService(((KinoApplication) getApplication()).getDaoSession(), this),
                        this).importCsvFile("import_series.csv");

                serie_status.setText(R.string.import_status_success);
            } catch (ImportException e) {
                Toast.makeText(getApplication().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                serie_status.setText(R.string.import_status_failed);
                serie_error.setText(e.getMessage());

            }

            try {
                new CsvImporter<>(
                        new FileReaderGetter(getApplication()),
                        new DtoImportCreator<>(this, new WishlistDtoFromRecordBuilder(this)),
                        new MovieWishlistService(((KinoApplication) getApplication()).getDaoSession()),
                        this).importCsvFile("import_wishlist_movies.csv");

                wishlist_serie_status.setText(R.string.import_status_success);
            } catch (ImportException e) {
                Toast.makeText(getApplication().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                wishlist_serie_status.setText(R.string.import_status_failed);
                wishlist_serie_error.setText(e.getMessage());

            }

            try {
                new CsvImporter<>(
                        new FileReaderGetter(getApplication()),
                        new DtoImportCreator<>(this, new WishlistDtoFromRecordBuilder(this)),
                        new SerieWishlistService(((KinoApplication) getApplication()).getDaoSession()),
                        this).importCsvFile("import_wishlist_series.csv");

                wishlist_movie_status.setText(R.string.import_status_success);
            } catch (ImportException e) {
                Toast.makeText(getApplication().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                wishlist_movie_status.setText(R.string.import_status_failed);
                wishlist_movie_error.setText(e.getMessage());
            }

            Toast.makeText(getApplicationContext(), getString(R.string.import_ending_toast), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.import_permission_error_toast), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @SuppressWarnings("NullableProblems") String permissions[],
                                           @SuppressWarnings("NullableProblems") int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                writeStoragePermission = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            }
        }
    }

}


