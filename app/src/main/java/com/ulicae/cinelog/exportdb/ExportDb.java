package com.ulicae.cinelog.exportdb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ulicae.cinelog.KinoApplication;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_db);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.export_db_button)
    public void onClick(View view) {
        Toast.makeText(getApplicationContext(), "Export launched !", Toast.LENGTH_LONG).show();

        CsvExporter csvExporter;
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriterGetter().get("export.csv");
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "An error occured while trying to get export file !", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            csvExporter = new CsvExporter(
                    new LocalKinoRepository(((KinoApplication) getApplication()).getDaoSession()),
                    fileWriter
            );
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "An error occured while creating export file !", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            csvExporter.export();

            Toast.makeText(getApplicationContext(), "Export succeeded !", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "An error occured while exporting !", Toast.LENGTH_LONG).show();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        out.println("Gone !");
    }


}


