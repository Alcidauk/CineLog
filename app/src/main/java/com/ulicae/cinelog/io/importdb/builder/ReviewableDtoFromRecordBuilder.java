package com.ulicae.cinelog.io.importdb.builder;

import android.content.Context;

import androidx.annotation.NonNull;

import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.utils.PreferencesWrapper;

import org.apache.commons.csv.CSVRecord;

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
public abstract class ReviewableDtoFromRecordBuilder<Dto> extends DtoFromRecordBuilder<Dto> {

    ReviewableDtoFromRecordBuilder(PreferencesWrapper preferencesWrapper, Context context) {
        super(preferencesWrapper, context);
    }

    @NonNull
    protected List<TagDto> getTagDtoWithIds(CSVRecord csvRecord) {
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
        if(tagsAsString != null && !tagsAsString.equals(""))  {
            return tagsAsString.split(",");
        } else {
            return new String[]{};
        }
    }

}
