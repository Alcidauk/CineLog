package com.ulicae.cinelog.android.activities;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.services.wishlist.MovieWishlistService;
import com.ulicae.cinelog.data.services.wishlist.SerieWishlistService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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
@RunWith(MockitoJUnitRunner.class)
public class WishlistItemDeleterTest {

    @Mock
    private SerieWishlistService serieWishlistService;

    @Mock
    private MovieWishlistService movieWishlistService;

    @Mock
    private WishlistDataDto wishlistDataDto;

    @Test
    public void deleteWishlistMovieItem() {
        doReturn(wishlistDataDto).when(movieWishlistService).getById(1564L);

        new WishlistItemDeleter(serieWishlistService, movieWishlistService).deleteWishlistItem(1564L, "kino");

        verify(movieWishlistService).delete(wishlistDataDto);
    }

    @Test
    public void deleteWishlistSerieItem() {
        doReturn(wishlistDataDto).when(serieWishlistService).getById(1564L);

        new WishlistItemDeleter(serieWishlistService, movieWishlistService).deleteWishlistItem(1564L, "serie");

        verify(serieWishlistService).delete(wishlistDataDto);
    }

}