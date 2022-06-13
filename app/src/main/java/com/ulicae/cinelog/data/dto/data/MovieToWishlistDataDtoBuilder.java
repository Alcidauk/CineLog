package com.ulicae.cinelog.data.dto.data;

import android.annotation.SuppressLint;
import androidx.annotation.Nullable;

import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * CineLog Copyright 2019 Pierre Rognon
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
public class MovieToWishlistDataDtoBuilder {

    public WishlistDataDto build(BaseMovie movie) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return new WishlistDataDto(
                null,
                movie.id,
                movie.title,
                movie.poster_path,
                movie.overview,
                getYear(movie.release_date),
                movie.release_date != null ? dateFormat.format(movie.release_date) : null,
                WishlistItemType.MOVIE
        );
    }

    @Nullable
    private int getYear(Date firstAirDate) {
        if(firstAirDate != null) {
            @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("yyyy").format(firstAirDate);
            return date != null ? Integer.parseInt(date) : 0;
        }
        return 0;
    }
}
