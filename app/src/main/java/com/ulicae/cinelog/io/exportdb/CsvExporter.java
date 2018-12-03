package com.ulicae.cinelog.io.exportdb;

import com.ulicae.cinelog.data.KinoService;
import com.ulicae.cinelog.data.dto.KinoDto;

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
public class CsvExporter {
    private KinoService kinoService;
    private final CsvExportWriter csvExportWriter;

    public CsvExporter(KinoService kinoService, Appendable out) throws IOException {
        this(kinoService, new CsvExportWriter(out));
    }

    CsvExporter(KinoService kinoService, CsvExportWriter csvExportWriter) {
        this.kinoService = kinoService;
        this.csvExportWriter = csvExportWriter;
    }

    public void export() throws IOException {
        List<KinoDto> localKinoList = kinoService.getAll();

        for (KinoDto kinoDto : localKinoList) {
            csvExportWriter.write(kinoDto);
        }

        csvExportWriter.endWriting();
    }
}
