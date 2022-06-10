package com.ulicae.cinelog.data.dto.data;

import android.annotation.SuppressLint;
import androidx.annotation.Nullable;

import com.uwetrottmann.tmdb2.entities.BaseTvShow;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TvShowToSerieDataDtoBuilder {

    public WishlistDataDto build(BaseTvShow tvShow) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return new WishlistDataDto(
                null,
                tvShow.id,
                tvShow.name,
                tvShow.poster_path,
                tvShow.overview,
                getYear(tvShow.first_air_date),
                tvShow.first_air_date != null ? dateFormat.format(tvShow.first_air_date) : null,
                WishlistItemType.SERIE
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
