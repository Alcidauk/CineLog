package com.alcidauk.cinelog.importdb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.alcidauk.cinelog.KinoApplication;
import com.alcidauk.cinelog.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImportInDb extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_db);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.import_db_button)
    public void onClick(View view) {
        try {
            new CsvImporter(this, ((KinoApplication) getApplication()).getDaoSession()).importCsvFile();
        } catch (ImportException e) {
            Toast.makeText(getApplicationContext(), "An error occured while importing movies !", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("Gone !");
    }

}


