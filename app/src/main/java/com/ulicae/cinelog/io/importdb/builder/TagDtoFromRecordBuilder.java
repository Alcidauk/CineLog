package com.ulicae.cinelog.io.importdb.builder;

import android.content.Context;

import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.utils.PreferencesWrapper;

import org.apache.commons.csv.CSVRecord;

import java.text.ParseException;

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
public class TagDtoFromRecordBuilder extends DtoFromRecordBuilder<TagDto> {

    public TagDtoFromRecordBuilder(Context context) {
        super(new PreferencesWrapper(), context);
    }

    TagDtoFromRecordBuilder(Context context, PreferencesWrapper preferencesWrapper) {
        super(preferencesWrapper, context);
    }

    public TagDto doBuild(CSVRecord csvRecord) throws ParseException, IllegalArgumentException {
        return new TagDto(
                formatLong(getId(csvRecord)),
                csvRecord.get("name"),
                csvRecord.get("color").replace("`", ""),
                formatBoolean(csvRecord.get("forMovies")),
                formatBoolean(csvRecord.get("forSeries"))
        );
    }

    @Override
    public String getLineTitle(CSVRecord csvRecord) {
        return csvRecord.get("name");
    }

}
