package com.ulicae.cinelog.io.importdb;

import android.annotation.SuppressLint;
import android.content.Context;

import com.ulicae.cinelog.utils.PreferencesWrapper;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.KinoDto;

import org.apache.commons.csv.CSVRecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * CineLog Copyright 2018 Pierre Rognon
 * <p>
 * <p>
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 */
class LocalKinoBuilder {

    private Context context;
    private PreferencesWrapper preferencesWrapper;

    public LocalKinoBuilder(Context context) {
        this(context, new PreferencesWrapper());
    }

    public LocalKinoBuilder(Context context, PreferencesWrapper preferencesWrapper) {
        this.context = context;
        this.preferencesWrapper = preferencesWrapper;
    }

    KinoDto build(CSVRecord csvRecord) throws ImportException {
        try {
            return new KinoDto(
                    formatLong(getId(csvRecord)),
                    formatLong(csvRecord.get("movie_id")),
                    csvRecord.get("title"),
                    formatDate(csvRecord.get("review_date")),
                    csvRecord.get("review"),
                    formatFloat(csvRecord.get("rating")),
                    getMaxRating(csvRecord),
                    csvRecord.get("poster_path"),
                    csvRecord.get("overview"),
                    formatInteger(csvRecord.get("year")),
                    csvRecord.get("release_date")
            );
        } catch (ParseException e) {
            throw new ImportException(context.getString(R.string.import_parsing_line_error_toast, csvRecord.get("title")), e);
        }
    }

    private String getId(CSVRecord csvRecord) {
        return csvRecord.isMapped("id") ? csvRecord.get("id") : null;
    }

    private int getMaxRating(CSVRecord csvRecord) {
        return csvRecord.isMapped("max_rating") ?
                formatInteger(csvRecord.get("max_rating")) :
                formatInteger(preferencesWrapper.getStringPreference(context, "default_max_rate_value", "5"));
    }

    private int formatInteger(String integerToFormat) {
        return integerToFormat != null && !integerToFormat.isEmpty() ? Integer.parseInt(integerToFormat) : 0;
    }

    private long formatLong(String longToFormat) {
        return longToFormat != null && !longToFormat.isEmpty() ? Long.parseLong(longToFormat) : 0;
    }

    @SuppressLint("SimpleDateFormat")
    private Date formatDate(String dateToFormat) throws ParseException {
        return dateToFormat != null && !dateToFormat.isEmpty() ? new SimpleDateFormat().parse(dateToFormat) : null;
    }

    private float formatFloat(String rating) {
        return rating != null && !rating.isEmpty() ? Float.parseFloat(rating.replace(",", ".")) : 0f;
    }
}
