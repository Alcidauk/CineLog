package com.ulicae.cinelog.room.dto.utils.from;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.room.dao.SyncWishlistItemDao;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Tmdb;
import com.ulicae.cinelog.room.entities.WishlistItem;

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
public class WishlistFromDtoCreator extends SyncEntityFromDtoCreator<WishlistItem, SyncWishlistItemDao, WishlistDataDto> {

    public WishlistFromDtoCreator(SyncWishlistItemDao wishlistItemDao) {
        super(wishlistItemDao);
    }


    @Override
    public WishlistItem createRoomInstanceFromDto(WishlistDataDto dto) {
        return new WishlistItem(
                dto.getId(),
                dto.getWishlistItemType() == WishlistItemType.MOVIE ? ItemEntityType.MOVIE : ItemEntityType.SERIE,
                dto.getTitle(),
                new Tmdb(
                        dto.getTmdbId(),
                        dto.getPosterPath(),
                        dto.getOverview(),
                        dto.getFirstYear(),
                        dto.getReleaseDate()
                )
        );
    }
}
