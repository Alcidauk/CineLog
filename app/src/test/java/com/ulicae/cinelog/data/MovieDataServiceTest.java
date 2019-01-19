package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.TmdbKino;
import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dao.WishlistMovie;
import com.ulicae.cinelog.data.dao.WishlistSerie;
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

@RunWith(MockitoJUnitRunner.class)
public class MovieDataServiceTest {

    @Mock
    private WishlistMovieRepository wishlistMovieRepository;

    @Mock
    private TmdbKinoRepository tmdbKinoRepository;

    @Mock
    private WishlistMovieToSerieDataDtoBuilder wishlistMovieToSerieDataDtoBuilder;

    @Test
    public void createMovieData() {
        new MovieDataService(wishlistMovieRepository, tmdbKinoRepository, wishlistMovieToSerieDataDtoBuilder).createMovieData(
                new WishlistDataDto(24L, 264564, "A movie", "2125", "an overview", 2015, "A release date", WishlistItemType.MOVIE)
        );

        TmdbKino tmdbKino = new TmdbKino(264564L, "2125", "an overview", 2015, "A release date");
        WishlistMovie wishlistMovie = new WishlistMovie(24L, tmdbKino, "A movie", null);

        verify(tmdbKinoRepository).createOrUpdate(tmdbKino);
        verify(wishlistMovieRepository).createOrUpdate(wishlistMovie);
    }

    @Test
    public void createMovieData_noTmdb() {
        new MovieDataService(wishlistMovieRepository, tmdbKinoRepository, wishlistMovieToSerieDataDtoBuilder).createMovieData(
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
                new MovieDataService(wishlistMovieRepository, tmdbKinoRepository, wishlistMovieToSerieDataDtoBuilder).getAll()
        );
    }

}