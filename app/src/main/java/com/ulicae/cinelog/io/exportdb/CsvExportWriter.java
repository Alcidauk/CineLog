package com.ulicae.cinelog.io.exportdb;

import android.annotation.SuppressLint;

import com.ulicae.cinelog.data.dao.LocalKino;
import com.ulicae.cinelog.data.dao.TmdbKino;

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

    public void write(LocalKino localKino) throws IOException {
        TmdbKino tmdbKino = localKino.getKino();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        csvPrinterWrapper.printRecord(
                tmdbKino != null ? tmdbKino.getMovie_id() : null,
                localKino.getTitle(),
                tmdbKino != null ? tmdbKino.getOverview() : null,
                tmdbKino != null ? tmdbKino.getYear() : null,
                tmdbKino != null ? tmdbKino.getPoster_path() : null,
                localKino.getRating(),
                tmdbKino != null ? tmdbKino.getRelease_date() : null,
                localKino.getReview(),
                localKino.getReview_date() != null ? simpleDateFormat.format(localKino.getReview_date()) : null,
                localKino.getMaxRating()
        );
    }

    public void endWriting() throws IOException {
        csvPrinterWrapper.flush();
        csvPrinterWrapper.close();
    }
}
