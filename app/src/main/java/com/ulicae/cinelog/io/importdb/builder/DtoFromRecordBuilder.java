package com.ulicae.cinelog.io.importdb.builder;

import android.annotation.SuppressLint;
import android.content.Context;

import com.ulicae.cinelog.io.importdb.ImportException;
import com.ulicae.cinelog.utils.PreferencesWrapper;

import org.apache.commons.csv.CSVRecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DtoFromRecordBuilder<Dto> {

    PreferencesWrapper preferencesWrapper;
    Context context;

    DtoFromRecordBuilder(PreferencesWrapper preferencesWrapper, Context context) {
        this.preferencesWrapper = preferencesWrapper;
        this.context = context;
    }

    public abstract Dto build(CSVRecord csvRecord) throws ImportException;

    protected String getId(CSVRecord csvRecord) {
        return csvRecord.isMapped("id") ? csvRecord.get("id") : null;
    }

    protected int getMaxRating(CSVRecord csvRecord) {
        return csvRecord.isMapped("max_rating") ?
                formatInteger(csvRecord.get("max_rating")) :
                formatInteger(preferencesWrapper.getStringPreference(context, "default_max_rate_value", "5"));
    }

    protected int formatInteger(String integerToFormat) {
        return integerToFormat != null && !integerToFormat.isEmpty() ? Integer.parseInt(integerToFormat) : 0;
    }

    protected long formatLong(String longToFormat) {
        return longToFormat != null && !longToFormat.isEmpty() ? Long.parseLong(longToFormat) : 0;
    }

    @SuppressLint("SimpleDateFormat")
    protected Date formatDate(String dateToFormat) throws ParseException {
        return dateToFormat != null && !dateToFormat.isEmpty() ? new SimpleDateFormat().parse(dateToFormat) : null;
    }

    protected float formatFloat(String rating) {
        return rating != null && !rating.isEmpty() ? Float.parseFloat(rating.replace(",", ".")) : 0f;
    }
}
