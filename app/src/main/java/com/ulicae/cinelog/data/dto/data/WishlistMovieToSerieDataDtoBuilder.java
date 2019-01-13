package com.ulicae.cinelog.data.dto.data;

import com.ulicae.cinelog.data.dao.TmdbKino;
import com.ulicae.cinelog.data.dao.WishlistMovie;

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
                movie != null ? movie.getRelease_date() : null
        );
    }
}
