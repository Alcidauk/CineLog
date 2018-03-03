package com.alcidauk.cinelog.importdb;

import android.annotation.SuppressLint;

import com.alcidauk.cinelog.dao.LocalKino;

import org.apache.commons.csv.CSVRecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class LocalKinoBuilder {

    LocalKino build(CSVRecord csvRecord) throws ImportException {
        try {
            return new LocalKino(
                    csvRecord.get("poster_path"),
                    formatFloat(csvRecord.get("rating")),
                    csvRecord.get("review"),
                    csvRecord.get("overview"),
                    formatInteger(csvRecord.get("year")),
                    csvRecord.get("title"),
                    csvRecord.get("release_date"),
                    formatInteger(csvRecord.get("movie_id")),
                    formatDate(csvRecord.get("review_date"))
            );
        } catch (ParseException e) {
            throw new ImportException(String.format("Can't save import movie with name %s.", csvRecord.get("title")), e);
        }
    }

    private int formatInteger(String integerToFormat) {
        return integerToFormat != null && !integerToFormat.isEmpty() ? Integer.parseInt(integerToFormat) : 0;
    }

    @SuppressLint("SimpleDateFormat")
    private Date formatDate(String dateToFormat) throws ParseException {
        return dateToFormat != null && !dateToFormat.isEmpty() ? new SimpleDateFormat().parse(dateToFormat) : null;
    }

    private float formatFloat(String rating) {
        return rating != null && !rating.isEmpty() ? Float.parseFloat(rating.replace(",", ".")) : 0f;
    }
}
