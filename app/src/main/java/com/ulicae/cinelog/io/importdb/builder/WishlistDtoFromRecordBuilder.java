package com.ulicae.cinelog.io.importdb.builder;

import android.content.Context;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.io.importdb.ImportException;
import com.ulicae.cinelog.utils.PreferencesWrapper;

import org.apache.commons.csv.CSVRecord;

import java.text.ParseException;

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
public class WishlistDtoFromRecordBuilder extends DtoFromRecordBuilder<WishlistDataDto> {

    public WishlistDtoFromRecordBuilder(Context context) {
        super(new PreferencesWrapper(), context);
    }

    WishlistDtoFromRecordBuilder(Context context, PreferencesWrapper preferencesWrapper) {
        super(preferencesWrapper, context);
    }

    public WishlistDataDto build(CSVRecord csvRecord) throws ImportException {
        try {
            return new WishlistDataDto(
                    formatLong(getId(csvRecord)),
                    formatInteger(csvRecord.get("movie_id")),
                    csvRecord.get("title"),
                    csvRecord.get("poster_path"),
                    csvRecord.get("overview"),
                    formatInteger(csvRecord.get("firstYear")),
                    csvRecord.get("release_date"),
                    getWishlistItemTypeFromString(csvRecord.get("wishlistItemType"))
            );
        } catch (ParseException e) {
            throw new ImportException(context.getString(R.string.import_parsing_line_error_toast, csvRecord.get("title")), e);
        }
    }

    private WishlistItemType getWishlistItemTypeFromString(String typeAsString) throws ParseException {
        if(typeAsString == null || (!typeAsString.equals("MOVIE") && !typeAsString.equals("SERIE"))) {
            throw new ParseException("Wishlist item type must be either MOVIE or SERIE", 0);
        }

        return typeAsString.equals("MOVIE") ? WishlistItemType.MOVIE : WishlistItemType.SERIE;
    }

}
