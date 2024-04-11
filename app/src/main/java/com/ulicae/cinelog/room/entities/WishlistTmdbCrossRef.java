package com.ulicae.cinelog.room.entities;

import androidx.room.Entity;

@Entity(primaryKeys = {"wishlistId", "tmdbId"})
public class WishlistTmdbCrossRef {

    public int wishlistId;
    public int tmdbId;

    public WishlistTmdbCrossRef(int wishlistId, int tmdbId) {
        this.wishlistId = wishlistId;
        this.tmdbId = tmdbId;
    }
}
