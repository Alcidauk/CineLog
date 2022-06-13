package com.ulicae.cinelog.io.exportdb.writer;

import android.annotation.SuppressLint;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.TagDto;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * CineLog Copyright 2018 Pierre Rognon
 *
 *
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 *
 */
public class TagCsvExportWriter extends CsvExportWriter<TagDto> {

    public enum Headers {
        id,
        name,
        color,
        forMovies,
        forSeries
    }

    public TagCsvExportWriter(Appendable out) throws IOException {
        super(out, Headers.class);
    }

    TagCsvExportWriter(CSVPrinterWrapper csvPrinterWrapper) {
        super(csvPrinterWrapper);
    }

    public void write(TagDto tagDto) throws IOException {
        csvPrinterWrapper.printRecord(
                tagDto.getId(),
                tagDto.getName(),
                tagDto.getColor(),
                tagDto.isForMovies(),
                tagDto.isForSeries()
        );
    }
}
