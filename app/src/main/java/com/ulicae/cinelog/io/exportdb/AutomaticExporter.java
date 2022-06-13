package com.ulicae.cinelog.io.exportdb;

import android.app.Application;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.io.exportdb.exporter.ExporterFactory;
import com.ulicae.cinelog.utils.BusinessPreferenceGetter;

import java.io.File;
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
public class AutomaticExporter {

    private final ExportTreeManager exportTreeManager;
    private final BusinessPreferenceGetter businessPreferenceGetter;
    private final ExporterFactory csvExporterFactory;

    public AutomaticExporter(Application application, ExporterFactory exporterFactory, String subDir) {this(
                new ExportTreeManager(application.getExternalMediaDirs()[0], subDir),
                new BusinessPreferenceGetter(application),
                exporterFactory
        );
    }

    AutomaticExporter(ExportTreeManager exportTreeManager, BusinessPreferenceGetter businessPreferenceGetter, ExporterFactory exporterFactory) {
        this.exportTreeManager = exportTreeManager;
        this.businessPreferenceGetter = businessPreferenceGetter;
        this.csvExporterFactory = exporterFactory;
    }

    public boolean tryExport() throws AutomaticExportException {
        if (businessPreferenceGetter.getAutomaticExport()) {
            exportTreeManager.prepareTree();

            if (exportTreeManager.isExportNeeded()) {
                FileWriter nextExportFile;
                try {
                    nextExportFile = exportTreeManager.getNextExportFile();
                } catch (IOException e) {
                    throw new AutomaticExportException(e, R.string.automatic_export_cant_get_next_export);
                }

                try {
                    csvExporterFactory.makeCsvExporter(nextExportFile).export();

                    exportTreeManager.clean();
                } catch (IOException e) {
                    throw new AutomaticExportException(e, R.string.automatic_export_cant_export);
                }

                return true;
            }
        }
        return false;
    }
}
