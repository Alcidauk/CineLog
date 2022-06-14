package com.ulicae.cinelog.io.importdb.builder;

import android.content.Context;

import androidx.annotation.NonNull;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.io.importdb.ImportException;
import com.ulicae.cinelog.utils.PreferencesWrapper;

import org.apache.commons.csv.CSVRecord;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
public class KinoDtoFromRecordBuilder extends DtoFromRecordBuilder<KinoDto> {

    public KinoDtoFromRecordBuilder(Context context) {
        super(new PreferencesWrapper(), context);
    }

    KinoDtoFromRecordBuilder(Context context, PreferencesWrapper preferencesWrapper) {
        super(preferencesWrapper, context);
    }

    public KinoDto doBuild(CSVRecord csvRecord) throws ParseException, IllegalArgumentException {
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
                csvRecord.get("release_date"),
                getTagDtoWithIds(csvRecord)
        );
    }

    @Override
    public String getLineTitle(CSVRecord csvRecord) {
        return csvRecord.get("title");
    }

    @NonNull
    private List<TagDto> getTagDtoWithIds(CSVRecord csvRecord) {
        String[] splittedTagsAsString = getSplittedTagIds(csvRecord);
        return Arrays.stream(splittedTagsAsString)
                .map(tagId ->
                        new TagDto(Long.parseLong(tagId), null, null, false, false))
                .collect(Collectors.toList());
    }

    @NonNull
    private String[] getSplittedTagIds(CSVRecord csvRecord) {
        String tagsAsString;
        try {
            tagsAsString = csvRecord.get("tags");
        } catch (IllegalArgumentException e) {
            tagsAsString = null;
        }
        return tagsAsString != null ? tagsAsString.split(",") : new String[]{};
    }
}
