package com.ulicae.cinelog.utils;

import com.ulicae.cinelog.data.dao.Review;
import com.ulicae.cinelog.data.dao.SerieEpisode;
import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.SerieEpisodeDto;

/**
 * CineLog Copyright 2018 Pierre Rognon
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
public class SerieDtoToDbBuilder {

    public Review buildReview(SerieDto serieDto) {
        return new Review(
                serieDto.getReviewId(),
                serieDto.getTitle(),
                serieDto.getReview_date(),
                serieDto.getReview(),
                serieDto.getRating(),
                serieDto.getMaxRating()
        );
    }

    public TmdbSerie buildTmdbSerie(SerieDto serieDto) {
        if (serieDto.getTmdbKinoId() != null) {
            return new TmdbSerie(
                    serieDto.getTmdbKinoId(),
                    serieDto.getPosterPath(),
                    serieDto.getOverview(),
                    serieDto.getYear(),
                    serieDto.getReleaseDate()
            );
        } else {
            return null;
        }
    }

    public SerieEpisode buildSerieEpisode(SerieEpisodeDto serieEpisodeDto) {
        return new SerieEpisode(
                serieEpisodeDto.getEpisodeId(),
                serieEpisodeDto.getTmdbEpisodeId(),
                serieEpisodeDto.getTmdbSerieId(),
                serieEpisodeDto.getWatchingDate()
        );
    }
}
