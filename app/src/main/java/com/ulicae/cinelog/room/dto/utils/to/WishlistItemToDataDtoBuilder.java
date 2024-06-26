package com.ulicae.cinelog.room.dto.utils.to;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Tmdb;
import com.ulicae.cinelog.room.entities.WishlistItem;

/**
 * CineLog Copyright 2019 Pierre Rognon
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
public class WishlistItemToDataDtoBuilder {

    public WishlistDataDto build(WishlistItem wishlistItem, Tmdb tmdbItem) {
        if(tmdbItem != null) {
            return new WishlistDataDto(
                    wishlistItem.getId(),
                    tmdbItem.getTmdbId(),
                    wishlistItem.getTitle(),
                    tmdbItem.getPosterPath(),
                    tmdbItem.getOverview(),
                    tmdbItem.getYear(),
                    tmdbItem.getReleaseDate(),
                    wishlistItem.getItemEntityType() == ItemEntityType.MOVIE ? WishlistItemType.MOVIE : WishlistItemType.SERIE
            );
        }

        return new WishlistDataDto(
                wishlistItem.getId(),
                null,
                wishlistItem.getTitle(),
                null,
                null,
                0,
                null,
                WishlistItemType.MOVIE
        );
    }
}
