package com.ulicae.cinelog.room.dto.utils.from;

import com.ulicae.cinelog.room.dto.KinoDto;
import com.ulicae.cinelog.room.dto.SerieDto;
import com.ulicae.cinelog.room.dto.SerieEpisodeDto;
import com.ulicae.cinelog.room.dao.SyncTmdbSerieEpisodeDao;
import com.ulicae.cinelog.room.entities.TmdbSerieEpisode;

import java.util.ArrayList;
import java.util.List;

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
public class SerieEpisodeFromDtoCreator extends SyncEntityFromDtoCreator<TmdbSerieEpisode, SyncTmdbSerieEpisodeDao, SerieEpisodeDto> {
    private final List<KinoDto> serieDtos;

    public SerieEpisodeFromDtoCreator(SyncTmdbSerieEpisodeDao episodeDao, List<KinoDto> serieDtos) {
        super(episodeDao);
        this.serieDtos = serieDtos;
    }

    @Override
    TmdbSerieEpisode createRoomInstanceFromDto(SerieEpisodeDto itemDto) {
        SerieDto serieDto = (SerieDto) this.serieDtos.stream().filter((dto) -> dto.getTmdbKinoId().equals(itemDto.getTmdbSerieId())).findFirst().get();
        return new TmdbSerieEpisode(
                itemDto.getTmdbEpisodeId(),
                serieDto.getReviewId(),
                itemDto.getWatchingDate()
        );
    }

    @Override
    public Long[] insertAll(List<SerieEpisodeDto> items) {
        List<TmdbSerieEpisode> episodes = new ArrayList<>();
        for (SerieEpisodeDto episodeDto : items) {
            episodes.add(this.createRoomInstanceFromDto(episodeDto));
        }

        dao.insertAll(episodes);

        return null;
    }
}
