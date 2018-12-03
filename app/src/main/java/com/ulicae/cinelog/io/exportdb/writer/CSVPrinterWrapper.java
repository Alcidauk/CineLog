package com.ulicae.cinelog.io.exportdb.writer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;

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
