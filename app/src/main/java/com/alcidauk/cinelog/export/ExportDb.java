package com.alcidauk.cinelog.export;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.alcidauk.cinelog.KinoApplication;
import com.alcidauk.cinelog.R;
import com.alcidauk.cinelog.db.LocalKinoRepository;

import java.io.IOException;
import java.io.StringWriter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        Toast.makeText(getApplicationContext(), "Clicked !", Toast.LENGTH_LONG).show();

        CsvExporter csvExporter;
        StringWriter out = new StringWriter();
        try {
            csvExporter = new CsvExporter(
                    new LocalKinoRepository(((KinoApplication) getApplication()).getDaoSession()),
                    out
            );
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "An error occured while creating export file !", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            csvExporter.export();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "An error occured while exporting !", Toast.LENGTH_LONG).show();
        }

        System.out.println(out.toString());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("Gone !");
    }


}


