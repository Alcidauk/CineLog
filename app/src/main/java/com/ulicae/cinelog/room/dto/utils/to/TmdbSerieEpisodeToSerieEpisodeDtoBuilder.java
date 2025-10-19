package com.ulicae.cinelog.room.dto.utils.to;

import com.ulicae.cinelog.room.dto.SerieEpisodeDto;
import com.ulicae.cinelog.room.entities.TmdbSerieEpisode;
import com.uwetrottmann.tmdb2.entities.TvEpisode;

/**
 * CineLog Copyright 2024 Pierre Rognon
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
public class TmdbSerieEpisodeToSerieEpisodeDtoBuilder {

    public SerieEpisodeDto buildFromTvAndDb(TmdbSerieEpisode serieEpisode, TvEpisode tvEpisode){
        SerieEpisodeDto episodeAsDto = build(serieEpisode);
        episodeAsDto.setName(tvEpisode.name);
        episodeAsDto.setAirDate(tvEpisode.air_date);
        episodeAsDto.setSeasonNumber(tvEpisode.season_number);
        episodeAsDto.setEpisodeNumber(tvEpisode.episode_number);

        return episodeAsDto;
    }

    public SerieEpisodeDto build(TvEpisode tvEpisode, Long tmdbSerieId, Long reviewId) {
        return new SerieEpisodeDto(
                tvEpisode.id,
                tmdbSerieId,
                reviewId,
                null,
                tvEpisode.name,
                tvEpisode.air_date,
                tvEpisode.season_number,
                tvEpisode.episode_number
        );
    }

    public SerieEpisodeDto build(TmdbSerieEpisode serieEpisode) {
        return new SerieEpisodeDto(
                (int) serieEpisode.getTmdbEpisodeId(),
                null,
                null,
                serieEpisode.getWatchingDate(),
                null,
                null,
                null,
                null
        );
    }
}
