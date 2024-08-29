package com.ulicae.cinelog.io.exportdb;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.io.exportdb.exporter.CsvExporter;
import com.ulicae.cinelog.io.exportdb.exporter.ExporterFactory;
import com.ulicae.cinelog.utils.ToasterWrapper;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class SnapshotExporter {
    private final ExporterFactory exporterFactory;
    private final ToasterWrapper toasterWrapper;
    private final ContentResolver contentResolver;

    SnapshotExporter(ExporterFactory exporterFactory, ToasterWrapper toasterWrapper,
                          ContentResolver contentResolver) {
        this.exporterFactory = exporterFactory;
        this.toasterWrapper = toasterWrapper;
        this.contentResolver = contentResolver;
    }

    public void export(Uri exportFilename) {
        toasterWrapper.toast(R.string.export_start_toast, ToasterWrapper.ToasterDuration.SHORT);

        try {
            ParcelFileDescriptor parcelFileDescriptor = contentResolver.openFileDescriptor(exportFilename, "w");
            if(parcelFileDescriptor == null) {
                throw new IOException();
            }

            FileDescriptor fd = parcelFileDescriptor.getFileDescriptor();
            FileWriter fileWriter = new FileWriter(fd);

            CsvExporter csvExporter = exporterFactory.makeCsvExporter(fileWriter);
            csvExporter.export();

            toasterWrapper.toast(R.string.export_succeeded_toast, ToasterWrapper.ToasterDuration.SHORT);
        } catch (IOException e) {
            toasterWrapper.toast(R.string.export_io_error_toast, ToasterWrapper.ToasterDuration.LONG);
        }
    }



}
