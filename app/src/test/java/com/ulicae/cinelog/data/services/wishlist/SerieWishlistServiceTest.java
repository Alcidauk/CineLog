package com.ulicae.cinelog.data.services.wishlist;

import com.ulicae.cinelog.data.TmdbSerieRepository;
import com.ulicae.cinelog.data.WishlistSerieRepository;
import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dao.WishlistSerie;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.data.dto.data.WishlistSerieToSerieDataDtoBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
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
public class SerieWishlistServiceTest {

    @Mock
    private WishlistSerieRepository wishlistSerieRepository;

    @Mock
    private TmdbSerieRepository tmdbSerieRepository;

    @Mock
    private WishlistSerieToSerieDataDtoBuilder wishlistSerieToSerieDataDtoBuilder;

    @Test
    public void createSerieData() {
        new SerieWishlistService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).createSerieData(
                new WishlistDataDto(24L, 264564, "A movie", "2125", "an overview", 2015, "A release date", WishlistItemType.SERIE)
        );

        TmdbSerie tmdbSerie = new TmdbSerie(264564L, "2125", "an overview", 2015, "A release date");
        WishlistSerie wishlistSerie = new WishlistSerie(24L, tmdbSerie, "A movie", null);

        verify(tmdbSerieRepository).createOrUpdate(tmdbSerie);
        verify(wishlistSerieRepository).createOrUpdate(wishlistSerie);
    }

    @Test
    public void createSerieData_noTmdb() {
        new SerieWishlistService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).createSerieData(
                new WishlistDataDto(24L, null, "A movie", "2125", "an overview", 2015, "A release date", WishlistItemType.SERIE)
        );

        WishlistSerie wishlistSerie = new WishlistSerie(24L, null, "A movie", null);
        verify(wishlistSerieRepository).createOrUpdate(wishlistSerie);
    }

    @Test
    public void getAll() {
        final WishlistSerie wishlistSerie = mock(WishlistSerie.class);
        doReturn(new ArrayList<WishlistSerie>(){{add(wishlistSerie);}}).when(wishlistSerieRepository).findAll();

        final WishlistDataDto wishlistDataDto = mock(WishlistDataDto.class);
        doReturn(wishlistDataDto).when(wishlistSerieToSerieDataDtoBuilder).build(wishlistSerie);

        assertEquals(
                new ArrayList<WishlistDataDto>() {{ add(wishlistDataDto); }},
                new SerieWishlistService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).getAll()
        );
    }

    @Test
    public void getByTmdbId() {
        final WishlistSerie wishlistSerie = mock(WishlistSerie.class);
        doReturn(wishlistSerie).when(wishlistSerieRepository).findByTmdbId(34455L);

        final WishlistDataDto wishlistDataDto = mock(WishlistDataDto.class);
        doReturn(wishlistDataDto).when(wishlistSerieToSerieDataDtoBuilder).build(wishlistSerie);

        assertEquals(
                wishlistDataDto,
                new SerieWishlistService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).getByTmdbId(34455)
        );
    }

    @Test
    public void getById() {
        final WishlistSerie wishlistSerie = mock(WishlistSerie.class);
        doReturn(wishlistSerie).when(wishlistSerieRepository).find(34455L);

        final WishlistDataDto wishlistDataDto = mock(WishlistDataDto.class);
        doReturn(wishlistDataDto).when(wishlistSerieToSerieDataDtoBuilder).build(wishlistSerie);

        assertEquals(
                wishlistDataDto,
                new SerieWishlistService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).getById(34455L)
        );
    }

    @Test
    public void getByTmdbId_noSerie() {
        doReturn(null).when(wishlistSerieRepository).findByTmdbId(34455L);
        assertNull( new SerieWishlistService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).getByTmdbId(34455));
    }

    @Test
    public void delete() {
        // TODO remove the tmdb id at the same time if not linked to another entity
        final WishlistDataDto wishlistDataDto = mock(WishlistDataDto.class);
        doReturn(345L).when(wishlistDataDto).getId();

        new SerieWishlistService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).delete(wishlistDataDto);

        verify(wishlistSerieRepository).delete(345L);
    }
}