package com.ulicae.cinelog.android.activities;

import android.content.Context;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.services.wishlist.MovieWishlistService;
import com.ulicae.cinelog.data.services.wishlist.SerieWishlistService;

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
class WishlistItemDeleter {

    private SerieWishlistService serieWishlistService;
    private MovieWishlistService movieWishlistService;

    public WishlistItemDeleter(Context context) {
        this.serieWishlistService = new SerieWishlistService(((KinoApplication) context.getApplicationContext()).getDaoSession());
        this.movieWishlistService = new MovieWishlistService(((KinoApplication) context.getApplicationContext()).getDaoSession());
    }

    WishlistItemDeleter(SerieWishlistService serieWishlistService, MovieWishlistService movieWishlistService) {
        this.serieWishlistService = serieWishlistService;
        this.movieWishlistService = movieWishlistService;
    }

    public void deleteWishlistItem(Long tmdbKinoId, String type) {
        if("kino".equals(type)){
            WishlistDataDto byTmdbId = movieWishlistService.getById(tmdbKinoId);
            movieWishlistService.delete(byTmdbId);
        } else {
            WishlistDataDto byTmdbId = serieWishlistService.getById(tmdbKinoId);
            serieWishlistService.delete(byTmdbId);
        }

    }
}
