package com.ulicae.cinelog.io.exportdb;

import android.content.ContentResolver;

import com.ulicae.cinelog.room.dto.ItemDto;
import com.ulicae.cinelog.io.exportdb.exporter.ExporterFactory;
import com.ulicae.cinelog.utils.ToasterWrapper;

public class SnapshotExporterFactory {

    private final ToasterWrapper toasterWrapper;
    private final ContentResolver contentResolver;

    public SnapshotExporterFactory(ToasterWrapper toasterWrapper,
                                   ContentResolver contentResolver) {
        this.toasterWrapper = toasterWrapper;
        this.contentResolver = contentResolver;
    }

    public SnapshotExporter<? extends ItemDto> makeSnapshotExporter(ExporterFactory<? extends ItemDto> exporterFactory) {
        return new SnapshotExporter<>(exporterFactory, contentResolver);
    }

}
