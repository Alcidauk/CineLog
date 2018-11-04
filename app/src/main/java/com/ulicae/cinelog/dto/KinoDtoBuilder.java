package com.ulicae.cinelog.dto;

import com.ulicae.cinelog.dao.LocalKino;
import com.ulicae.cinelog.dao.TmdbKino;

/**
 * Created by alcidauk on 28/03/18.
 */
public class KinoDtoBuilder {

    public KinoDto build(LocalKino localKino) {
        Long tmdbId = null;
        String posterPath = null;
        String overview = null;
        int year = 0;
        String releaseDate = null;

        TmdbKino kino = localKino.getKino();
        if (kino != null) {
            tmdbId = kino.getMovie_id();
            posterPath = kino.getPoster_path();
            overview = kino.getOverview();
            year = kino.getYear();
            releaseDate = kino.getRelease_date();
        }

        return new KinoDto(
                localKino.getId(),
                tmdbId,
                localKino.getTitle(),
                localKino.getReview_date(),
                localKino.getReview(),
                localKino.getRating(),
                localKino.getMaxRating(),
                posterPath,
                overview,
                year,
                releaseDate
        );
    }
}
