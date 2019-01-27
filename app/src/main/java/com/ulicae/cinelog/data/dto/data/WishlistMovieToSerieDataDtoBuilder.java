package com.ulicae.cinelog.data.dto.data;

import com.ulicae.cinelog.data.dao.TmdbKino;
import com.ulicae.cinelog.data.dao.WishlistMovie;

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
public class WishlistMovieToSerieDataDtoBuilder {

    public WishlistDataDto build(WishlistMovie wishlistMovie) {
        TmdbKino movie = wishlistMovie.getMovie();
        return new WishlistDataDto(
                wishlistMovie.getWishlist_movie_id(),
                movie != null && movie.getMovie_id() != null ? movie.getMovie_id().intValue() : null,
                wishlistMovie.getTitle(),
                movie != null ? movie.getPoster_path() : null,
                movie != null ? movie.getOverview() : null,
                movie != null ? movie.getYear() : 0,
                movie != null ? movie.getRelease_date() : null,
                WishlistItemType.MOVIE
        );
    }
}
