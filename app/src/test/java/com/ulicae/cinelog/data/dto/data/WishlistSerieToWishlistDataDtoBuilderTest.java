package com.ulicae.cinelog.data.dto.data;

import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dao.WishlistSerie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class WishlistSerieToWishlistDataDtoBuilderTest {

    @Mock
    private WishlistSerie wishlistSerie;

    @Mock
    private TmdbSerie tmdbSerie;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void build() {
        doReturn(456L).when(wishlistSerie).getWishlist_serie_id();
        doReturn("Versailles").when(wishlistSerie).getTitle();
        doReturn(tmdbSerie).when(wishlistSerie).getSerie();

        doReturn(456456456L).when(tmdbSerie).getSerie_id();
        doReturn(2015).when(tmdbSerie).getYear();
        doReturn("An overview").when(tmdbSerie).getOverview();
        doReturn("/poster/path").when(tmdbSerie).getPoster_path();
        doReturn("a releaseDate").when(tmdbSerie).getRelease_date();

        assertEquals(
                new WishlistDataDto(
                        456L,
                        456456456,
                        "Versailles",
                        "/poster/path",
                        "An overview",
                        2015,
                        "a releaseDate",
                        WishlistItemType.SERIE
                ),
                new WishlistSerieToSerieDataDtoBuilder().build(wishlistSerie)
        );
    }
}