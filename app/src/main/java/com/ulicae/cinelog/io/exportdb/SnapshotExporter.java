package com.ulicae.cinelog.io.exportdb;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.ulicae.cinelog.room.dto.ItemDto;
import com.ulicae.cinelog.io.exportdb.exporter.CsvExporter;
import com.ulicae.cinelog.io.exportdb.exporter.ExporterFactory;

import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class SnapshotExporter<T extends ItemDto> {
    private final ExporterFactory<T> exporterFactory;
    private final ContentResolver contentResolver;

    SnapshotExporter(ExporterFactory<T> exporterFactory,
                     ContentResolver contentResolver) {
        this.exporterFactory = exporterFactory;
        this.contentResolver = contentResolver;
    }

    public Flowable<List<T>> export(Uri exportFilename) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = contentResolver.openFileDescriptor(exportFilename, "w");
        if (parcelFileDescriptor == null) {
            throw new IOException();
        }

        FileDescriptor fd = parcelFileDescriptor.getFileDescriptor();
        FileWriter fileWriter = new FileWriter(fd);

        CsvExporter<T> csvExporter = exporterFactory.makeCsvExporter(fileWriter);
        return csvExporter.export();
    }


}
