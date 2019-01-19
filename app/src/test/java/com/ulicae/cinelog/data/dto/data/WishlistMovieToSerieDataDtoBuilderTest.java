package com.ulicae.cinelog.data.dto.data;

import com.ulicae.cinelog.data.dao.TmdbKino;
import com.ulicae.cinelog.data.dao.WishlistMovie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class WishlistMovieToSerieDataDtoBuilderTest {

    @Mock
    private WishlistMovie wishlistMovie;

    @Mock
    private TmdbKino tmdbMovie;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void build() {
        doReturn(456L).when(wishlistMovie).getWishlist_movie_id();
        doReturn("Elle").when(wishlistMovie).getTitle();
        doReturn(tmdbMovie).when(wishlistMovie).getMovie();

        doReturn(456456456L).when(tmdbMovie).getMovie_id();
        doReturn(2015).when(tmdbMovie).getYear();
        doReturn("An overview").when(tmdbMovie).getOverview();
        doReturn("/poster/path").when(tmdbMovie).getPoster_path();
        doReturn("a releaseDate").when(tmdbMovie).getRelease_date();

        assertEquals(
                new WishlistDataDto(
                        456L,
                        456456456,
                        "Elle",
                        "/poster/path",
                        "An overview",
                        2015,
                        "a releaseDate",
                        WishlistItemType.MOVIE
                ),
                new WishlistMovieToSerieDataDtoBuilder().build(wishlistMovie)
        );
    }

}