package com.ulicae.cinelog.data.dto.data.room;

import static org.mockito.Mockito.doReturn;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Tmdb;
import com.ulicae.cinelog.room.entities.WishlistItem;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WishlistItemToDataDtoBuilderTest extends TestCase {


    @Mock
    private WishlistItem wishlistItem;

    @Mock
    private Tmdb tmdb;

    @Before
    public void setUp() {
        //noinspection ResultOfMethodCallIgnored
        doReturn(456L).when(wishlistItem).getId();
        doReturn("Elle").when(wishlistItem).getTitle();

        doReturn(456456456L).when(tmdb).getId();
        doReturn(2015).when(tmdb).getYear();
        doReturn("An overview").when(tmdb).getOverview();
        doReturn("/poster/path").when(tmdb).getPosterPath();
        doReturn("a releaseDate").when(tmdb).getReleaseDate();
    }


    @Test
    public void testBuildWishlistItemMovie() {
        doReturn(ItemEntityType.MOVIE).when(wishlistItem).getItemEntityType();

        assertEquals(
                new WishlistDataDto(
                        456L,
                        456456456L,
                        "Elle",
                        "/poster/path",
                        "An overview",
                        2015,
                        "a releaseDate",
                        WishlistItemType.MOVIE
                ),
                new WishlistItemToDataDtoBuilder().build(wishlistItem, tmdb)
        );
    }

    @Test
    public void testBuildWishlistItemSerie() {
        doReturn(ItemEntityType.SERIE).when(wishlistItem).getItemEntityType();

        assertEquals(
                new WishlistDataDto(
                        456L,
                        456456456L,
                        "Elle",
                        "/poster/path",
                        "An overview",
                        2015,
                        "a releaseDate",
                        WishlistItemType.SERIE
                ),
                new WishlistItemToDataDtoBuilder().build(wishlistItem, tmdb)
        );
    }

}