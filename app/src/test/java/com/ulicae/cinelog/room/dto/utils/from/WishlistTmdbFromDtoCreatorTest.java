package com.ulicae.cinelog.room.dto.utils.from;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Tmdb;

import junit.framework.TestCase;

import org.mockito.Mock;

public class WishlistTmdbFromDtoCreatorTest extends TestCase {

    @Mock
    private TmdbDao tmdbDao;

    public void testCreateRoomInstanceFromMovieDto() {
        assertEquals(
                new Tmdb(
                        23L,
                        ItemEntityType.MOVIE,
                        "/aposterpath.jpg",
                        "A wishlist overview",
                        2000,
                        "12/12/2024"
                ),
                new WishlistTmdbFromDtoCreator(tmdbDao)
                        .createRoomInstanceFromDto(
                                new WishlistDataDto(
                                        453L,
                                        23L,
                                        "a wishlist movie item",
                                        "/aposterpath.jpg",
                                        "A wishlist overview",
                                        2000,
                                        "12/12/2024",
                                        WishlistItemType.MOVIE
                                )
                        )
        );
    }


    public void testCreateRoomInstanceFromMovieDtoNoTmdb() {
        assertNull(
                new WishlistTmdbFromDtoCreator(tmdbDao)
                        .createRoomInstanceFromDto(
                                new WishlistDataDto(
                                        453L,
                                        0L,
                                        "a wishlist movie item",
                                        "/aposterpath.jpg",
                                        "A wishlist overview",
                                        2000,
                                        "12/12/2024",
                                        WishlistItemType.MOVIE
                                )
                        )
        );
    }

    public void testCreateRoomInstanceFromSerieDto() {
        assertEquals(
                new Tmdb(
                        23L,
                        ItemEntityType.SERIE,
                        "/aposterpath.jpg",
                        "A wishlist overview",
                        2000,
                        "12/12/2024"
                ),
                new WishlistTmdbFromDtoCreator(tmdbDao)
                        .createRoomInstanceFromDto(
                                new WishlistDataDto(
                                        453L,
                                        23L,
                                        "a wishlist movie item",
                                        "/aposterpath.jpg",
                                        "A wishlist overview",
                                        2000,
                                        "12/12/2024",
                                        WishlistItemType.SERIE
                                )
                        )
        );
    }

    public void testCreateRoomInstanceFromSerieDtoNoTmdb() {
        assertNull(
                new WishlistTmdbFromDtoCreator(tmdbDao)
                        .createRoomInstanceFromDto(
                                new WishlistDataDto(
                                        453L,
                                        0L,
                                        "a wishlist movie item",
                                        "/aposterpath.jpg",
                                        "A wishlist overview",
                                        2000,
                                        "12/12/2024",
                                        WishlistItemType.SERIE
                                )
                        )
        );
    }

}