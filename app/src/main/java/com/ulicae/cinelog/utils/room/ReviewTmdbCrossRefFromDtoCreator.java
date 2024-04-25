package com.ulicae.cinelog.utils.room;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.room.dao.ReviewTmdbCrossRefDao;
import com.ulicae.cinelog.room.entities.ReviewTmdbCrossRef;

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
public class ReviewTmdbCrossRefFromDtoCreator extends EntityFromDtoCreator<ReviewTmdbCrossRef, ReviewTmdbCrossRefDao, KinoDto> {

    public ReviewTmdbCrossRefFromDtoCreator(ReviewTmdbCrossRefDao dao) {
        super(dao);
    }

    @Override
    public ReviewTmdbCrossRef createRoomInstanceFromDto(KinoDto kinoDto) {
        return kinoDto.getTmdbKinoId() != null ?
                new ReviewTmdbCrossRef(
                        Math.toIntExact(kinoDto.getId()),
                        Math.toIntExact(kinoDto.getTmdbKinoId())
                ) : null;
    }
}
