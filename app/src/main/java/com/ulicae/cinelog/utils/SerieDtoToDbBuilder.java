package com.ulicae.cinelog.utils;

import com.ulicae.cinelog.data.dao.Review;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dto.SerieDto;

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
public class SerieDtoToDbBuilder {
    public SerieReview build(SerieDto serieDto) {
        //noinspection UnnecessaryUnboxing
        Review review = new Review(
                serieDto.getReviewId() != null ? serieDto.getReviewId().longValue() : 0L,
                serieDto.getTitle(),
                serieDto.getReview_date(),
                serieDto.getReview(),
                serieDto.getRating(),
                serieDto.getMaxRating()
        );

        TmdbSerie tmdbSerie = null;
        if (serieDto.getTmdbKinoId() != null) {
            tmdbSerie = new TmdbSerie(
                    serieDto.getTmdbKinoId(),
                    serieDto.getTmdbKinoId() != null ? (int) serieDto.getTmdbKinoId().longValue() : null,
                    serieDto.getTitle(),
                    serieDto.getPosterPath(),
                    serieDto.getOverview(),
                    serieDto.getYear(),
                    serieDto.getReleaseDate()
            );
        }

        return new SerieReview(
                serieDto.getKinoId(),
                tmdbSerie,
                review
        );
    }
}
