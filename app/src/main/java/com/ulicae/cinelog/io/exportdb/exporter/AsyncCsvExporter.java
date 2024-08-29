package com.ulicae.cinelog.io.exportdb.exporter;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.ItemDto;
import com.ulicae.cinelog.data.services.AsyncDataService;
import com.ulicae.cinelog.io.exportdb.writer.CsvExportWriter;
import com.ulicae.cinelog.utils.ToasterWrapper;

import java.io.IOException;

/**
 * CineLog Copyright 2024 Pierre Rognon
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
public class AsyncCsvExporter<T extends ItemDto> implements CsvExporter {
    private final AsyncDataService<T> service;
    private final CsvExportWriter<T> csvExportWriter;
    private final ToasterWrapper toasterWrapper;


    AsyncCsvExporter(AsyncDataService<T> service, CsvExportWriter<T> csvExportWriter, ToasterWrapper toasterWrapper) {
        this.service = service;
        this.csvExportWriter = csvExportWriter;
        this.toasterWrapper = toasterWrapper;
    }

    public void export() throws IOException {
        service
                .findAll()
                .subscribe(
                        success -> {
                            for (T dto : success) {
                                csvExportWriter.write(dto);
                            }

                            csvExportWriter.endWriting();
                        },
                        error -> {
                            this.toasterWrapper.toast(
                                    R.string.export_io_error_toast,
                                    ToasterWrapper.ToasterDuration.LONG
                            );
                        }
                );
    }
}
