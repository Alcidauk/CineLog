package com.alcidauk.cinelog.importdb;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;

class CSVFormatWrapper {

    Iterable<CSVRecord> parse(FileReader fileReader) throws IOException {
        return CSVFormat.DEFAULT.withDelimiter('§').withFirstRecordAsHeader().parse(fileReader);
    }
}
