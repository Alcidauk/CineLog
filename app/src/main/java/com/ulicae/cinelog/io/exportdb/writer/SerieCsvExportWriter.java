package com.ulicae.cinelog.io.exportdb.writer;

import android.annotation.SuppressLint;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;

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
public class SerieCsvExportWriter extends CsvExportWriter<SerieDto> {

    public enum Headers {
        id,
        movie_id,
        review_id,
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

    public SerieCsvExportWriter(Appendable out) throws IOException {
        super(out, Headers.class);
    }

    SerieCsvExportWriter(CSVPrinterWrapper csvPrinterWrapper) {
        super(csvPrinterWrapper);
    }

    public void write(SerieDto serieDto) throws IOException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        csvPrinterWrapper.printRecord(
                serieDto.getKinoId(),
                serieDto.getTmdbKinoId(),
                serieDto.getReviewId(),
                serieDto.getTitle(),
                serieDto.getOverview(),
                serieDto.getYear(),
                serieDto.getPosterPath(),
                serieDto.getRating(),
                serieDto.getReleaseDate(),
                serieDto.getReview(),
                serieDto.getReview_date() != null ? simpleDateFormat.format(serieDto.getReview_date()) : null,
                serieDto.getMaxRating()
        );
    }
}
