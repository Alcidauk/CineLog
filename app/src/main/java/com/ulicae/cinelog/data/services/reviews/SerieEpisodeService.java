package com.ulicae.cinelog.data.services.reviews;

import com.ulicae.cinelog.data.SerieEpisodeRepository;
import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.SerieEpisode;
import com.ulicae.cinelog.data.dto.SerieEpisodeDto;
import com.ulicae.cinelog.data.dto.SerieEpisodeDtoBuilder;
import com.ulicae.cinelog.utils.SerieDtoToDbBuilder;
import com.uwetrottmann.tmdb2.entities.TvEpisode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class SerieEpisodeService {

    private final SerieEpisodeRepository serieEpisodeRepository;
    private SerieEpisodeDtoBuilder serieEpisodeDtoBuilder;

    public SerieEpisodeService(DaoSession daoSession) {
        this(new SerieEpisodeRepository(daoSession), new SerieEpisodeDtoBuilder());
    }

    public SerieEpisodeService(SerieEpisodeRepository serieEpisodeRepository,
                               SerieEpisodeDtoBuilder serieEpisodeDtoBuilder) {
        this.serieEpisodeRepository = serieEpisodeRepository;
        this.serieEpisodeDtoBuilder = serieEpisodeDtoBuilder;
    }

    public SerieEpisodeDto createOrUpdate(SerieEpisodeDto serieEpisodeDto) {
        serieEpisodeDto.setWatchingDate(new Date());
        SerieEpisode episode = new SerieDtoToDbBuilder().buildSerieEpisode(serieEpisodeDto);

        serieEpisodeRepository.createOrReplace(episode);
        return new SerieEpisodeDtoBuilder().build(episode);
    }

    public void delete(SerieEpisodeDto serieEpisodeDto) {
        serieEpisodeRepository.delete(serieEpisodeDto.getEpisodeId());
    }

    public List<SerieEpisodeDto> getDtoEpisodes(List<TvEpisode> tvEpisodes, Long tmdbSerieId) {
        List<SerieEpisode> existingEpisodes = this.serieEpisodeRepository.findByTmdbSerieId(tmdbSerieId);

        List<SerieEpisodeDto> episodeDtos = new ArrayList<>();
        for (TvEpisode tvEpisode : tvEpisodes) {
            episodeDtos.add(getEpisodeAsDto(existingEpisodes, tvEpisode, tmdbSerieId));
        }

        return episodeDtos;
    }

    private SerieEpisodeDto getEpisodeAsDto(List<SerieEpisode> existingEpisodes,
                                            TvEpisode tvEpisode,
                                            Long tmdbSerieId) {
        for (SerieEpisode episode : existingEpisodes) {
            if (episode.getTmdbEpisodeId().equals(tvEpisode.id)) {
                return serieEpisodeDtoBuilder.buildFromTvAndDb(episode, tvEpisode);
            }
        }

        return serieEpisodeDtoBuilder.build(tvEpisode, tmdbSerieId);
    }
}
