package com.alcidauk.cinelog.exportdb;

import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.dao.TmdbKino;

import java.io.IOException;

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
        review_date;
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

        csvPrinterWrapper.printRecord(
                tmdbKino.getMovie_id(),
                localKino.getTitle(),
                tmdbKino.getOverview(),
                tmdbKino.getYear(),
                tmdbKino.getPoster_path(),
                localKino.getRating(),
                tmdbKino.getRelease_date(),
                localKino.getReview(),
                localKino.getReview_date()
        );
    }

    public void endWriting() throws IOException {
        csvPrinterWrapper.flush();
        csvPrinterWrapper.close();
    }
}
