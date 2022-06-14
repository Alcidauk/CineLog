package com.ulicae.cinelog.io.importdb.builder;

import android.annotation.SuppressLint;
import android.content.Context;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.io.importdb.ImportException;
import com.ulicae.cinelog.utils.PreferencesWrapper;

import org.apache.commons.csv.CSVRecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * CineLog Copyright 2022 Pierre Rognon
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
public abstract class DtoFromRecordBuilder<Dto> {

    PreferencesWrapper preferencesWrapper;
    Context context;

    DtoFromRecordBuilder(PreferencesWrapper preferencesWrapper, Context context) {
        this.preferencesWrapper = preferencesWrapper;
        this.context = context;
    }

    public Dto build(CSVRecord csvRecord) throws ImportException {
        try {
            return doBuild(csvRecord);
        } catch (ParseException e) {
            throw new ImportException(context.getString(R.string.import_parsing_line_error_toast, getLineTitle(csvRecord)), e);
        } catch (IllegalArgumentException e) {
            throw new ImportException(context.getString(R.string.import_parsing_line_error_missing_column_toast), e);
        }
    }

    public abstract Dto doBuild(CSVRecord csvRecord) throws ParseException, IllegalArgumentException;

    public abstract String getLineTitle(CSVRecord csvRecord);

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

    protected boolean formatBoolean(String booleanToFormat) {
        return booleanToFormat != null && booleanToFormat.equals("true");
    }

    @SuppressLint("SimpleDateFormat")
    protected Date formatDate(String dateToFormat) throws ParseException {
        return dateToFormat != null && !dateToFormat.isEmpty() ? new SimpleDateFormat().parse(dateToFormat) : null;
    }

    protected float formatFloat(String rating) {
        return rating != null && !rating.isEmpty() ? Float.parseFloat(rating.replace(",", ".")) : 0f;
    }
}
