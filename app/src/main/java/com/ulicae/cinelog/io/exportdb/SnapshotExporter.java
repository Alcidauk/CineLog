package com.ulicae.cinelog.io.exportdb;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.io.exportdb.exporter.CsvExporter;
import com.ulicae.cinelog.io.exportdb.exporter.ExporterFactory;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class SnapshotExporter {
    private final ExporterFactory exporterFactory;
    private final Application application;

    private final ContentResolver contentResolver;

    SnapshotExporter(ExporterFactory exporterFactory, Application application,
                          ContentResolver contentResolver) {
        this.exporterFactory = exporterFactory;
        this.application = application;
        this.contentResolver = contentResolver;
    }

    public void export(Uri exportFilename) {
        showToast(R.string.export_start_toast);

        try {
            ParcelFileDescriptor parcelFileDescriptor = contentResolver.openFileDescriptor(exportFilename, "w");
            if(parcelFileDescriptor == null) {
                throw new IOException();
            }

            FileDescriptor fd = parcelFileDescriptor.getFileDescriptor();
            FileWriter fileWriter = new FileWriter(fd);

            CsvExporter csvExporter = exporterFactory.makeCsvExporter(fileWriter);
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
