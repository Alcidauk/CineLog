package com.alcidauk.cinelog.importdb;

import android.annotation.SuppressLint;

import com.alcidauk.cinelog.dao.LocalKino;

import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class KinoImportCreator {
    private CSVFormatWrapper csvFormatWrapper;
    private LocalKinoBuilder localKinoBuilder;

    KinoImportCreator() {
        this(new CSVFormatWrapper(), new LocalKinoBuilder());
    }

    KinoImportCreator(CSVFormatWrapper csvFormatWrapper, LocalKinoBuilder localKinoBuilder) {
        this.csvFormatWrapper = csvFormatWrapper;
        this.localKinoBuilder = localKinoBuilder;
    }

    List<LocalKino> getKinos(FileReader fileReader) throws ImportException {
        Iterable<CSVRecord> csvRecords;
        try {
            csvRecords = csvFormatWrapper.parse(fileReader);
        } catch (IOException e) {
            throw new ImportException("Error while parsing CSV file. Please verify it.", e);
        }

        List<LocalKino> kinos = new ArrayList<>();
        for (CSVRecord csvRecord : csvRecords) {
                kinos.add(localKinoBuilder.build(csvRecord));
        }

        return kinos;
    }

}
