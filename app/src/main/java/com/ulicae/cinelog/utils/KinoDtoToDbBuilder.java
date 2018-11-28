package com.ulicae.cinelog.utils;

import com.ulicae.cinelog.data.dao.LocalKino;
import com.ulicae.cinelog.data.dao.TmdbKino;
import com.ulicae.cinelog.data.dto.KinoDto;

public class KinoDtoToDbBuilder {
    public LocalKino build(KinoDto kinoDto) {
        //noinspection UnnecessaryUnboxing
        LocalKino localKino = new LocalKino(
                kinoDto.getKinoId(),
                kinoDto.getTmdbKinoId() != null ? kinoDto.getTmdbKinoId().longValue() : 0L,
                kinoDto.getTitle(),
                kinoDto.getReview_date(),
                kinoDto.getReview(),
                kinoDto.getRating(),
                kinoDto.getMaxRating()
        );

        if (kinoDto.getTmdbKinoId() != null) {
            TmdbKino tmdbKino = new TmdbKino(
                    kinoDto.getTmdbKinoId(),
                    kinoDto.getPosterPath(),
                    kinoDto.getOverview(),
                    kinoDto.getYear(),
                    kinoDto.getReleaseDate()
            );
            localKino.setKino(tmdbKino);
        }

        return localKino;
    }
}
