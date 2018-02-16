package com.alcidauk.cinelog.export;

import com.alcidauk.cinelog.dao.LocalKino;

import java.io.IOException;

/**
 * Created by alcidauk on 16/02/18.
 */
class CsvExportWriter {

    private enum Headers {
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
        csvPrinterWrapper.printRecord(
                localKino.getMovie_id(),
                localKino.getTitle(),
                localKino.getOverview(),
                localKino.getYear(),
                localKino.getPoster_path(),
                localKino.getRating(),
                localKino.getRelease_date(),
                localKino.getReview(),
                localKino.getReview_date()
        );
    }

    public void endWriting() throws IOException {
        csvPrinterWrapper.flush();
        csvPrinterWrapper.close();
    }
}
