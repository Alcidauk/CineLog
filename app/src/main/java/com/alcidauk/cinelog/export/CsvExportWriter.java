package com.alcidauk.cinelog.export;

import com.alcidauk.cinelog.dao.LocalKino;

import java.io.IOException;

/**
 * Created by alcidauk on 16/02/18.
 */
class CsvExportWriter {
    private CSVPrinterWrapper csvPrinterWrapper;

    public CsvExportWriter(Appendable out) throws IOException {
        this(new CSVPrinterWrapper(out));
    }

    CsvExportWriter(CSVPrinterWrapper csvPrinterWrapper) {
        this.csvPrinterWrapper = csvPrinterWrapper;
    }

    public void write(LocalKino localKino) throws IOException {
        csvPrinterWrapper.printRecord(localKino.getId(), localKino.getMovie_id(), localKino.getOverview());
    }

    public void endWriting() throws IOException {
        csvPrinterWrapper.flush();
        csvPrinterWrapper.close();
    }
}
