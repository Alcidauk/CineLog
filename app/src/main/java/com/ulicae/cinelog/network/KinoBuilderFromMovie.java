package com.ulicae.cinelog.network;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * CineLog Copyright 2018 Pierre Rognon
 *
 *
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 *
 */
public class KinoBuilderFromMovie implements DtoBuilderFromTmdbObject<BaseMovie> {

    public KinoDto build(BaseMovie movie) {
        // TODO take care of locale
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);

        String yearAsString = "";
        int yearAsInt = 0;
        if (movie.release_date != null) {
            yearAsString = sdf.format(movie.release_date);
            yearAsInt = Integer.parseInt(yearAsString);
        }

        return new KinoDto(
                null,
                movie.id.longValue(),
                movie.title,
                null,
                null,
                null,
                null,
                movie.poster_path,
                movie.overview,
                yearAsInt,
                yearAsString,
                new ArrayList<>()
        );
    }
}
