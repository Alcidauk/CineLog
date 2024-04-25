package com.ulicae.cinelog.utils.room;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.room.dao.TmdbDao;
import com.ulicae.cinelog.room.entities.Tmdb;

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
public class TmdbFromDtoCreator extends EntityFromDtoCreator<Tmdb, TmdbDao, KinoDto> {

    public TmdbFromDtoCreator(TmdbDao dao) {
        super(dao);
    }

    @Override
    public Tmdb createRoomInstanceFromDto(KinoDto kinoDto) {
        return kinoDto.getTmdbKinoId() != null ?
                new Tmdb(
                        Math.toIntExact(kinoDto.getKinoId()),
                        kinoDto.getTitle(),
                        kinoDto.getTmdbKinoId(),
                        kinoDto.getPosterPath(),
                        kinoDto.getOverview(),
                        kinoDto.getYear(),
                        kinoDto.getReleaseDate()) : null;
    }
}
