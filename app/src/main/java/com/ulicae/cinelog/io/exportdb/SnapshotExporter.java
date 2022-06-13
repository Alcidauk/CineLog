package com.ulicae.cinelog.io.exportdb;

import android.app.Application;
import android.widget.Toast;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.io.exportdb.exporter.CsvExporter;
import com.ulicae.cinelog.io.exportdb.exporter.ExporterFactory;

import java.io.FileWriter;
import java.io.IOException;

public class SnapshotExporter {

    private ExporterFactory exporterFactory;
    private Application application;

    SnapshotExporter(ExporterFactory exporterFactory, Application application) {
        this.exporterFactory = exporterFactory;
        this.application = application;
    }

    public void export(String exportFilename) {
        showToast(R.string.export_start_toast);

        CsvExporter csvExporter;
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriterGetter(application)
                    .get(exportFilename);
        } catch (IOException e) {
            showToast(R.string.export_io_error_toast);
            return;
        }

        try {
            csvExporter = exporterFactory.makeCsvExporter(fileWriter);
        } catch (IOException e) {
            showToast(R.string.export_io_error_toast);
            return;
        }

        try {
            csvExporter.export();
            showToast(R.string.export_succeeded_toast);
        } catch (IOException e) {
            showToast(R.string.export_io_error_toast);
        }
    }

    private void showToast(int messageId) {
        Toast.makeText(application.getApplicationContext(), application.getString(messageId), Toast.LENGTH_LONG).show();
    }

}
