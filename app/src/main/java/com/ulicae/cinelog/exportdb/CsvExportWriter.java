package com.ulicae.cinelog.exportdb;

import android.annotation.SuppressLint;

import com.ulicae.cinelog.dao.LocalKino;
import com.ulicae.cinelog.dao.TmdbKino;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by alcidauk on 16/02/18.
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
        review_date
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
                localKino.getReview_date() != null ? simpleDateFormat.format(localKino.getReview_date()) : null
        );
    }

    public void endWriting() throws IOException {
        csvPrinterWrapper.flush();
        csvPrinterWrapper.close();
    }
}
