package com.alcidauk.cinelog.export;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;

/**
 * Created by alcidauk on 16/02/18.
 */

class CSVPrinterWrapper {

    private CSVPrinter csvPrinter;

    private enum Headers {
        title,
        note;
    }

    public CSVPrinterWrapper(Appendable out) throws IOException {
        this.csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(Headers.class));
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
