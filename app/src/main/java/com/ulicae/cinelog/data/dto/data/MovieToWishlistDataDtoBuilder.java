package com.ulicae.cinelog.data.dto.data;

import android.support.annotation.Nullable;

import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.BaseTvShow;
import com.uwetrottmann.tmdb2.entities.Movie;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieToWishlistDataDtoBuilder {

    public WishlistDataDto build(BaseMovie movie) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
            String date = new SimpleDateFormat("yyyy").format(firstAirDate);
            return date != null ? Integer.parseInt(date) : 0;
        }
        return 0;
    }
}
