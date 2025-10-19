package com.ulicae.cinelog.room.dto.utils.from;

import com.ulicae.cinelog.room.dto.data.WishlistDataDto;
import com.ulicae.cinelog.room.dto.data.WishlistItemType;
import com.ulicae.cinelog.room.dao.SyncWishlistItemDao;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Tmdb;
import com.ulicae.cinelog.room.entities.WishlistItem;

import junit.framework.TestCase;

import org.mockito.Mock;

public class WishlistFromDtoCreatorTest extends TestCase {

    @Mock
    private SyncWishlistItemDao wishlistItemDao;

    public void testCreateRoomInstanceFromMovieDto() {
        assertEquals(
                new WishlistItem(
                        453L,
                        ItemEntityType.MOVIE,
                        "a wishlist movie item",
                        new Tmdb(
                                23L,
                                "/aposterpath.jpg",
                                "A wishlist overview",
                                2000,
                                "12/12/2024"
                        )
                ),
                new WishlistFromDtoCreator(wishlistItemDao)
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

    public void testCreateRoomInstanceFromSerieDto() {
        assertEquals(
                new WishlistItem(
                        453L,
                        ItemEntityType.SERIE,
                        "a wishlist movie item",
                        new Tmdb(
                                23L,
                                "/aposterpath.jpg",
                                "A wishlist overview",
                                2000,
                                "12/12/2024"
                        )
                ),
                new WishlistFromDtoCreator(wishlistItemDao)
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

}