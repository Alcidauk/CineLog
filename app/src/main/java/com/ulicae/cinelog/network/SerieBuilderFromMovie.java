package com.ulicae.cinelog.network;

import com.ulicae.cinelog.data.dto.SerieDto;
import com.uwetrottmann.tmdb2.entities.BaseTvShow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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
public class SerieBuilderFromMovie implements DtoBuilderFromTmdbObject<BaseTvShow> {

    public SerieDto build(BaseTvShow tvShow) {
        // TODO take care of locale
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);

        String yearAsString = "";
        int yearAsInt = 0;
        if (tvShow.first_air_date != null) {
            yearAsString = sdf.format(tvShow.first_air_date);
            yearAsInt = Integer.parseInt(yearAsString);
        }

        return new SerieDto(
                null,
                tvShow.id.longValue(),
                null,
                tvShow.name,
                null,
                null,
                null,
                null,
                tvShow.poster_path,
                tvShow.overview,
                yearAsInt,
                yearAsString,
                new ArrayList<>()
        );
    }
}
