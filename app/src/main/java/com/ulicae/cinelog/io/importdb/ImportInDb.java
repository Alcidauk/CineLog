package com.ulicae.cinelog.io.importdb;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.services.reviews.KinoService;
import com.ulicae.cinelog.data.services.reviews.SerieService;
import com.ulicae.cinelog.data.services.tags.TagService;
import com.ulicae.cinelog.data.services.wishlist.MovieWishlistService;
import com.ulicae.cinelog.data.services.wishlist.SerieWishlistService;
import com.ulicae.cinelog.databinding.ActivityImportDbBinding;
import com.ulicae.cinelog.io.importdb.builder.KinoDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.SerieDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.TagDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.WishlistDtoFromRecordBuilder;
import com.ulicae.cinelog.utils.ThemeWrapper;

/**
 * CineLog Copyright 2022 Pierre Rognon
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

    private ActivityImportDbBinding binding;

    private Boolean writeStoragePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        ActivityCompat.requestPermissions(ImportInDb.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        binding = ActivityImportDbBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.importInDbToolbar.toolbar);

        binding.importInDbContent.importDbButton.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        if (writeStoragePermission != null && writeStoragePermission) {
            Toast.makeText(getApplicationContext(), getString(R.string.import_starting_toast), Toast.LENGTH_SHORT).show();

            // TODO improve this code
            try {
                new CsvImporter<>(
                        new FileReaderGetter(getApplication()),
                        new DtoImportCreator<>(this, new TagDtoFromRecordBuilder(this)),
                        new TagService(((KinoApplication) getApplication()).getDaoSession()),
                        this).importCsvFile("import_tags.csv");

                binding.importInDbContent.importTagsStatusWaiting.setText(R.string.import_status_success);
            } catch (ImportException e) {
                Toast.makeText(getApplication().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                binding.importInDbContent.importTagsStatusWaiting.setText(R.string.import_status_failed);
                binding.importInDbContent.importTagsErrorMessage.setText(e.getMessage());
            }

            try {
                new CsvImporter<>(
                        new FileReaderGetter(getApplication()),
                        new DtoImportCreator<>(this, new KinoDtoFromRecordBuilder(this)),
                        new KinoService(((KinoApplication) getApplication()).getDaoSession()),
                        this).importCsvFile("import_movies.csv");

                binding.importInDbContent.importMoviesStatusWaiting.setText(R.string.import_status_success);
            } catch (ImportException e) {
                Toast.makeText(getApplication().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                binding.importInDbContent.importMoviesStatusWaiting.setText(R.string.import_status_failed);
                binding.importInDbContent.importMoviesErrorMessage.setText(e.getMessage());
            }

            try {
                new CsvImporter<>(
                        new FileReaderGetter(getApplication()),
                        new DtoImportCreator<>(this, new SerieDtoFromRecordBuilder(this)),
                        new SerieService(((KinoApplication) getApplication()).getDaoSession(), this),
                        this).importCsvFile("import_series.csv");

                binding.importInDbContent.importSeriesStatusWaiting.setText(R.string.import_status_success);
            } catch (ImportException e) {
                Toast.makeText(getApplication().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                binding.importInDbContent.importSeriesStatusWaiting.setText(R.string.import_status_failed);
                binding.importInDbContent.importSeriesErrorMessage.setText(e.getMessage());
            }

            try {
                new CsvImporter<>(
                        new FileReaderGetter(getApplication()),
                        new DtoImportCreator<>(this, new WishlistDtoFromRecordBuilder(this)),
                        new MovieWishlistService(((KinoApplication) getApplication()).getDaoSession()),
                        this).importCsvFile("import_wishlist_movies.csv");

                binding.importInDbContent.importWishlistMoviesStatusWaiting.setText(R.string.import_status_success);
            } catch (ImportException e) {
                Toast.makeText(getApplication().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                binding.importInDbContent.importWishlistMoviesStatusWaiting.setText(R.string.import_status_failed);
                binding.importInDbContent.importWishlistMoviesErrorMessage.setText(e.getMessage());
            }

            try {
                new CsvImporter<>(
                        new FileReaderGetter(getApplication()),
                        new DtoImportCreator<>(this, new WishlistDtoFromRecordBuilder(this)),
                        new SerieWishlistService(((KinoApplication) getApplication()).getDaoSession()),
                        this).importCsvFile("import_wishlist_series.csv");

                binding.importInDbContent.importWishlistSeriesStatusWaiting.setText(R.string.import_status_success);
            } catch (ImportException e) {
                Toast.makeText(getApplication().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                binding.importInDbContent.importWishlistSeriesStatusWaiting.setText(R.string.import_status_failed);
                binding.importInDbContent.importWishlistSeriesErrorMessage.setText(e.getMessage());
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
        if (requestCode == 1) {
            writeStoragePermission = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

}


