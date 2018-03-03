package com.alcidauk.cinelog.importdb;

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

    public KinoImportCreator() {
        this(new CSVFormatWrapper());
    }

    KinoImportCreator(CSVFormatWrapper csvFormatWrapper) {
        this.csvFormatWrapper = csvFormatWrapper;
    }

    public List<LocalKino> getKinos(FileReader fileReader) throws ImportException {
        Iterable<CSVRecord> csvRecords;
        try {
            csvRecords = csvFormatWrapper.parse(fileReader);
        } catch (IOException e) {
            throw new ImportException("Error while parsing CSV file. Please verify it.", e);
        }

        List<LocalKino> kinos = new ArrayList<>();
        for (CSVRecord csvRecord : csvRecords) {
            try {
                kinos.add(
                        new LocalKino(
                                csvRecord.get("poster_path"),
                                formatFloat(csvRecord.get("rating")),
                                csvRecord.get("review"),
                                csvRecord.get("overview"),
                                formatInteger(csvRecord.get("year")),
                                csvRecord.get("title"),
                                csvRecord.get("release_date"),
                                formatInteger(csvRecord.get("movie_id")),
                                formatDate(csvRecord.get("review_date"))
                        )
                );
            } catch (ParseException e) {
                throw new ImportException(String.format("Can't save import movie with name %s.", csvRecord.get("title")), e);
            }
        }

        return kinos;
    }

    private int formatInteger(String integerToFormat) {
        return integerToFormat != null && !integerToFormat.isEmpty() ? Integer.parseInt(integerToFormat) : 0;
    }

    private Date formatDate(String dateToFormat) throws ParseException {
        return dateToFormat != null && !dateToFormat.isEmpty() ? new SimpleDateFormat().parse(dateToFormat) : null;
    }

    private float formatFloat(String rating) {
        return rating != null && !rating.isEmpty() ? Float.parseFloat(rating.replace(",", ".")) : 0f;
    }


}
