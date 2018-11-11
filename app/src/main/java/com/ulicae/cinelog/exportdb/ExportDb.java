package com.ulicae.cinelog.exportdb;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.MainActivity;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.db.LocalKinoRepository;

import java.io.FileWriter;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.System.out;

/**
 * CineLog Copyright 2018 Pierre Rognon
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
public class ExportDb extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Boolean writeStoragePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(ExportDb.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        setContentView(R.layout.activity_export_db);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.export_db_button)
    public void onClick(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(writeStoragePermission != null && writeStoragePermission) {
            Toast.makeText(getApplicationContext(), prefs.getString("export_start_toast", null), Toast.LENGTH_LONG).show();

            CsvExporter csvExporter;
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriterGetter().get("export.csv");
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), prefs.getString("export_io_error_toast", null), Toast.LENGTH_LONG).show();
                return;
            }
            try {
                csvExporter = new CsvExporter(
                        new LocalKinoRepository(((KinoApplication) getApplication()).getDaoSession()),
                        fileWriter
                );
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), prefs.getString("export_io_error_toast", null), Toast.LENGTH_LONG).show();
                return;
            }

            try {
                csvExporter.export();

                Toast.makeText(getApplicationContext(), prefs.getString("export_succeeded_toast", null), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), prefs.getString("export_io_error_toast", null), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), prefs.getString("export_permission_error_toast", null), Toast.LENGTH_LONG).show();
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




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        out.println("Gone !");
    }


}


