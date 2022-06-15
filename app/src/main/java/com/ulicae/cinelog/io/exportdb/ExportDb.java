package com.ulicae.cinelog.io.exportdb;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.services.wishlist.MovieWishlistService;
import com.ulicae.cinelog.data.services.wishlist.SerieWishlistService;
import com.ulicae.cinelog.io.exportdb.exporter.MovieCsvExporterFactory;
import com.ulicae.cinelog.io.exportdb.exporter.SerieCsvExporterFactory;
import com.ulicae.cinelog.io.exportdb.exporter.TagCsvExporterFactory;
import com.ulicae.cinelog.io.exportdb.exporter.WishlistCsvExporterFactory;
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
public class ExportDb extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Boolean writeStoragePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        ActivityCompat.requestPermissions(ExportDb.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        setContentView(R.layout.activity_export_db);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.export_db_button)
    public void onClick(View view) {
        if (writeStoragePermission != null && writeStoragePermission) {
            new SnapshotExporter(new TagCsvExporterFactory(getApplication()), getApplication()).export("export_tags.csv");
            new SnapshotExporter(new MovieCsvExporterFactory(getApplication()), getApplication()).export("export_movies.csv");
            new SnapshotExporter(new SerieCsvExporterFactory(getApplication()), getApplication()).export("export_series.csv");
            new SnapshotExporter(new WishlistCsvExporterFactory(
                    new MovieWishlistService(((KinoApplication) getApplication()).getDaoSession())),
                    getApplication()).export("export_wishlist_movies.csv");
            new SnapshotExporter(new WishlistCsvExporterFactory(
                    new SerieWishlistService(((KinoApplication) getApplication()).getDaoSession())),
                    getApplication()).export("export_wishlist_series.csv");
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.export_permission_error_toast), Toast.LENGTH_LONG).show();
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


