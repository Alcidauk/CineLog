package com.ulicae.cinelog.io.exportdb;

import android.app.Application;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.room.dto.ItemDto;
import com.ulicae.cinelog.io.exportdb.exporter.CsvExporter;
import com.ulicae.cinelog.io.exportdb.exporter.ExporterFactory;
import com.ulicae.cinelog.utils.BusinessPreferenceGetter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

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
public class AutomaticExporter<T extends ItemDto> {

    private final ExportTreeManager exportTreeManager;
    private final BusinessPreferenceGetter businessPreferenceGetter;
    private final ExporterFactory<T> csvExporterFactory;

    // TODO ne plus exporter dans le dossier actuel, mais dans le dossier chiosi au moment de la sélection d'import auto
    public AutomaticExporter(Application application, ExporterFactory<T> exporterFactory, String subDir) {
        this(
                new ExportTreeManager(application.getExternalMediaDirs()[0], subDir),
                new BusinessPreferenceGetter(application),
                exporterFactory
        );
    }

    AutomaticExporter(ExportTreeManager exportTreeManager, BusinessPreferenceGetter businessPreferenceGetter, ExporterFactory<T> exporterFactory) {
        this.exportTreeManager = exportTreeManager;
        this.businessPreferenceGetter = businessPreferenceGetter;
        this.csvExporterFactory = exporterFactory;
    }

    public Flowable<List<T>> tryExport() throws AutomaticExportException {
        if (businessPreferenceGetter.getAutomaticExport()) {
            exportTreeManager.prepareTree();

            if (exportTreeManager.isExportNeeded()) {
                FileWriter nextExportFile;
                try {
                    nextExportFile = exportTreeManager.getNextExportFile();
                } catch (IOException e) {
                    throw new AutomaticExportException(e, R.string.automatic_export_cant_get_next_export);
                }

                CsvExporter<T> csvExporter;
                try {
                    csvExporter = csvExporterFactory.makeCsvExporter(nextExportFile);
                } catch (IOException e) {
                    throw new AutomaticExportException(e, R.string.automatic_export_cant_export);
                }

                return csvExporter.export()
                        .doOnNext(result -> exportTreeManager.clean());
            }
        }

        return Flowable.empty();
    }
}
