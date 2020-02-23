package com.ulicae.cinelog.data.services.wishlist;

import com.ulicae.cinelog.data.TmdbKinoRepository;
import com.ulicae.cinelog.data.WishlistMovieRepository;
import com.ulicae.cinelog.data.dao.TmdbKino;
import com.ulicae.cinelog.data.dao.WishlistMovie;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.data.dto.data.WishlistMovieToSerieDataDtoBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;
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
public class MovieWishlistServiceTest {

    @Mock
    private WishlistMovieRepository wishlistMovieRepository;

    @Mock
    private TmdbKinoRepository tmdbKinoRepository;

    @Mock
    private WishlistMovieToSerieDataDtoBuilder wishlistMovieToSerieDataDtoBuilder;

    @Test
    public void createMovieData() {
        new MovieWishlistService(wishlistMovieRepository, tmdbKinoRepository, wishlistMovieToSerieDataDtoBuilder).createMovieData(
                new WishlistDataDto(24L, 264564, "A movie", "2125", "an overview", 2015, "A release date", WishlistItemType.MOVIE)
        );

        TmdbKino tmdbKino = new TmdbKino(264564L, "2125", "an overview", 2015, "A release date");
        WishlistMovie wishlistMovie = new WishlistMovie(24L, tmdbKino, "A movie", null);

        verify(tmdbKinoRepository).createOrUpdate(tmdbKino);
        verify(wishlistMovieRepository).createOrUpdate(wishlistMovie);
    }

    @Test
    public void createMovieData_noTmdb() {
        new MovieWishlistService(wishlistMovieRepository, tmdbKinoRepository, wishlistMovieToSerieDataDtoBuilder).createMovieData(
                new WishlistDataDto(24L, null, "A movie", "2125", "an overview", 2015, "A release date", WishlistItemType.MOVIE)
        );

        WishlistMovie wishlistMovie = new WishlistMovie(24L, null, "A movie", null);
        verify(wishlistMovieRepository).createOrUpdate(wishlistMovie);
    }

    @Test
    public void getAll() {
        final WishlistMovie wishlistMovie = mock(WishlistMovie.class);
        doReturn(new ArrayList<WishlistMovie>(){{add(wishlistMovie);}}).when(wishlistMovieRepository).findAll();

        final WishlistDataDto wishlistDataDto = mock(WishlistDataDto.class);
        doReturn(wishlistDataDto).when(wishlistMovieToSerieDataDtoBuilder).build(wishlistMovie);

        assertEquals(
                new ArrayList<WishlistDataDto>() {{ add(wishlistDataDto); }},
                new MovieWishlistService(wishlistMovieRepository, tmdbKinoRepository, wishlistMovieToSerieDataDtoBuilder).getAll()
        );
    }

    @Test
    public void getByTmdbId() {
        final WishlistMovie wishlistMovie = mock(WishlistMovie.class);
        doReturn(wishlistMovie).when(wishlistMovieRepository).findByTmdbId(34455L);

        final WishlistDataDto wishlistDataDto = mock(WishlistDataDto.class);
        doReturn(wishlistDataDto).when(wishlistMovieToSerieDataDtoBuilder).build(wishlistMovie);

        assertEquals(
                wishlistDataDto,
                new MovieWishlistService(wishlistMovieRepository, tmdbKinoRepository, wishlistMovieToSerieDataDtoBuilder).getByTmdbId(34455)
        );
    }

    @Test
    public void getByTmdbId_noMovie() {
        doReturn(null).when(wishlistMovieRepository).findByTmdbId(34455L);
        assertNull( new MovieWishlistService(wishlistMovieRepository, tmdbKinoRepository, wishlistMovieToSerieDataDtoBuilder).getByTmdbId(34455));
    }

    @Test
    public void getById() {
        final WishlistMovie wishlistMovie = mock(WishlistMovie.class);
        doReturn(wishlistMovie).when(wishlistMovieRepository).find(34455L);

        final WishlistDataDto wishlistDataDto = mock(WishlistDataDto.class);
        doReturn(wishlistDataDto).when(wishlistMovieToSerieDataDtoBuilder).build(wishlistMovie);

        assertEquals(
                wishlistDataDto,
                new MovieWishlistService(wishlistMovieRepository, tmdbKinoRepository, wishlistMovieToSerieDataDtoBuilder).getById(34455L)
        );
    }

    @Test
    public void delete() {
        // TODO remove the tmdb id at the same time if not linked to another entity
        final WishlistDataDto wishlistDataDto = mock(WishlistDataDto.class);
        doReturn(345L).when(wishlistDataDto).getId();

        new MovieWishlistService(wishlistMovieRepository, tmdbKinoRepository, wishlistMovieToSerieDataDtoBuilder).delete(wishlistDataDto);

        verify(wishlistMovieRepository).delete(345L);
    }
}