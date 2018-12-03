package com.ulicae.cinelog.io.importdb.builder;

import android.annotation.SuppressLint;
import android.content.Context;

import com.ulicae.cinelog.io.importdb.ImportException;
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
public class KinoDtoFromRecordBuilder extends DtoFromRecordBuilder<KinoDto> {

    public KinoDtoFromRecordBuilder(Context context) {
        super(new PreferencesWrapper(), context);
    }

    KinoDtoFromRecordBuilder(Context context, PreferencesWrapper preferencesWrapper) {
        super(preferencesWrapper, context);
    }

    public KinoDto build(CSVRecord csvRecord) throws ImportException {
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
}
