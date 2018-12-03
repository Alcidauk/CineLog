package com.ulicae.cinelog.io.exportdb.exporter;

import com.ulicae.cinelog.data.DataService;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.io.exportdb.writer.CsvExportWriter;

import java.io.IOException;
import java.util.List;

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
public class CsvExporter<T extends KinoDto> {
    private DataService<T> service;
    private CsvExportWriter<T> csvExportWriter;

    CsvExporter(DataService<T> service, CsvExportWriter<T> csvExportWriter) {
        this.service = service;
        this.csvExportWriter = csvExportWriter;
    }

    public void export() throws IOException {
        List<T> localKinoList = service.getAll();

        for (T dto : localKinoList) {
            csvExportWriter.write(dto);
        }

        csvExportWriter.endWriting();
    }
}
