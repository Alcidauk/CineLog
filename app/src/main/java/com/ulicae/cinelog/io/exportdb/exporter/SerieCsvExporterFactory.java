package com.ulicae.cinelog.io.exportdb.exporter;

import android.app.Application;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.data.services.reviews.SerieService;
import com.ulicae.cinelog.io.exportdb.writer.SerieCsvExportWriter;

import java.io.FileWriter;
import java.io.IOException;

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
public class SerieCsvExporterFactory implements ExporterFactory{
    private SerieService serieService;

    public SerieCsvExporterFactory(Application application) {
        this(new SerieService(((KinoApplication) application).getDaoSession(), application.getBaseContext()));
    }

    private SerieCsvExporterFactory(SerieService serieService) {
        this.serieService = serieService;
    }

    public CsvExporter makeCsvExporter(FileWriter fileWriter) throws IOException {
        return new CsvExporter<>(serieService, new SerieCsvExportWriter(fileWriter));
    }
}
