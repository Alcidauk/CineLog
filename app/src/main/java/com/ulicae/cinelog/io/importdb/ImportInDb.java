package com.ulicae.cinelog.io.importdb;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.services.reviews.KinoService;
import com.ulicae.cinelog.data.services.reviews.SerieService;
import com.ulicae.cinelog.io.importdb.builder.KinoDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.SerieDtoFromRecordBuilder;
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
            new SnapshotImporter(getApplication()).importFiles(
                    "import_movies.csv",
                    new CsvImporter<>(
                            new FileReaderGetter(),
                            new KinoImportCreator<>(this, new KinoDtoFromRecordBuilder(this)),
                            new KinoService(((KinoApplication) getApplication()).getDaoSession()),
                            this)
            );

            new SnapshotImporter(getApplication()).importFiles(
                    "import_series.csv",
                    new CsvImporter<>(
                            new FileReaderGetter(),
                            new KinoImportCreator<>(this, new SerieDtoFromRecordBuilder(this)),
                            new SerieService(((KinoApplication) getApplication()).getDaoSession(), this),
                            this)
            );
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.import_permission_error_toast), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @SuppressWarnings("NullableProblems") String permissions[],
                                           @SuppressWarnings("NullableProblems") int[] grantResults) {
        switch (requestCode) {
            case 1: {
                writeStoragePermission = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            }
        }
    }

}


