package com.ulicae.cinelog.utils.room;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.room.dao.WishlistTmdbCrossRefDao;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.WishlistTmdbCrossRef;

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
public class WishlistTmdbCrossRefFromDtoCreator extends EntityFromDtoCreator<WishlistTmdbCrossRef, WishlistTmdbCrossRefDao, WishlistDataDto> {

    private final ItemEntityType itemEntityType;

    private final int biggestMovieReviewId;

    public WishlistTmdbCrossRefFromDtoCreator(WishlistTmdbCrossRefDao dao, ItemEntityType itemEntityType, int biggestMovieReviewId) {
        super(dao);
        this.itemEntityType = itemEntityType;
        this.biggestMovieReviewId = biggestMovieReviewId;
    }

    @Override
    public WishlistTmdbCrossRef createRoomInstanceFromDto(WishlistDataDto itemDto) {
        return itemDto.getTmdbKinoId() != null ?
                new WishlistTmdbCrossRef(
                        Math.toIntExact(itemEntityType == ItemEntityType.SERIE ? (biggestMovieReviewId + itemDto.getId()) : itemDto.getId()),
                        Math.toIntExact(itemDto.getTmdbKinoId())
                ) : null;
    }
}
