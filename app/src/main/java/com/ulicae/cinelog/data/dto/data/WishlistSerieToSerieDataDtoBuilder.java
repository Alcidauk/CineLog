package com.ulicae.cinelog.data.dto.data;

import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dao.WishlistSerie;

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
public class WishlistSerieToSerieDataDtoBuilder {

    public WishlistDataDto build(WishlistSerie wishlistSerie) {
        TmdbSerie serie = wishlistSerie.getSerie();
        return new WishlistDataDto(
                wishlistSerie.getWishlist_serie_id(),
                serie != null && serie.getSerie_id() != null ? serie.getSerie_id().intValue() : null,
                wishlistSerie.getTitle(),
                serie != null ? serie.getPoster_path() : null,
                serie != null ? serie.getOverview() : null,
                serie != null ? serie.getYear() : 0,
                serie != null ? serie.getRelease_date() : null,
                WishlistItemType.SERIE
        );
    }
}
