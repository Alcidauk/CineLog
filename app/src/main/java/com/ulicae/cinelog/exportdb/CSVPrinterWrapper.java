package com.ulicae.cinelog.exportdb;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;

/**
 * Created by alcidauk on 16/02/18.
 */
class CSVPrinterWrapper {

    private CSVPrinter csvPrinter;

    public CSVPrinterWrapper(Appendable out, Class<? extends Enum<?>> headers) throws IOException {
        this.csvPrinter = new CSVPrinter(
                out,
                CSVFormat.DEFAULT
                        .withHeader(headers)
                        .withQuote('`')
                        .withDelimiter('§'));
    }

    public void printRecord(Object... values) throws IOException {
        csvPrinter.printRecord(values);
    }

    public void close() throws IOException {
        csvPrinter.close();
    }

    public void flush() throws IOException {
        csvPrinter.flush();
    }
}
