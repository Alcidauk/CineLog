package com.ulicae.cinelog.io.exportdb.writer;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class MovieCsvExportWriter extends CsvExportWriter<KinoDto> {

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
        max_rating,
        tags
    }

    public MovieCsvExportWriter(Appendable out) throws IOException {
        super(out, Headers.class);
    }

    MovieCsvExportWriter(CSVPrinterWrapper csvPrinterWrapper) {
        super(csvPrinterWrapper);
    }

    public void write(KinoDto kinoDto) throws IOException {
        List<String> tagIds = getTagIdsAsString(kinoDto);

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
                kinoDto.getMaxRating(),
                String.join(",", tagIds)
        );
    }

    @NonNull
    private List<String> getTagIdsAsString(KinoDto kinoDto) {
        List<String> tagIds = new ArrayList<>();
        if (kinoDto.getTags() != null) {
            tagIds = kinoDto.getTags().stream()
                    .map(tagDto -> tagDto.getId().toString())
                    .collect(Collectors.toList());
        }
        return tagIds;
    }
}
