package com.ulicae.cinelog.io.exportdb;

import android.annotation.SuppressLint;

import com.ulicae.cinelog.data.dto.KinoDto;

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
public class CsvExportWriter {


    public enum Headers {
        id,
        movie_id,
        title,
        overview,
        year,
        poster_path,
        rating,
        release_date,
        review,
        review_date,
        max_rating
    }

    private CSVPrinterWrapper csvPrinterWrapper;

    public CsvExportWriter(Appendable out) throws IOException {
        this(new CSVPrinterWrapper(out, Headers.class));
    }

    CsvExportWriter(CSVPrinterWrapper csvPrinterWrapper) {
        this.csvPrinterWrapper = csvPrinterWrapper;
    }

    public void write(KinoDto kinoDto) throws IOException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        csvPrinterWrapper.printRecord(
                kinoDto.getKinoId(),
                kinoDto.getTmdbKinoId(),
                kinoDto.getTitle(),
                kinoDto.getOverview(),
                kinoDto.getYear(),
                kinoDto.getPosterPath(),
                kinoDto.getRating(),
                kinoDto.getReleaseDate(),
                kinoDto.getReview(),
                kinoDto.getReview_date() != null ? simpleDateFormat.format(kinoDto.getReview_date()) : null,
                kinoDto.getMaxRating()
        );
    }

    public void endWriting() throws IOException {
        csvPrinterWrapper.flush();
        csvPrinterWrapper.close();
    }
}
