package com.ulicae.cinelog.room.entities;

import androidx.room.Entity;

@Entity(primaryKeys = {"wishlistId", "tmdbId"})
public class WishlistTmdbCrossRef {

    public long wishlistId;
    public long tmdbId;

    public WishlistTmdbCrossRef(long wishlistId, long tmdbId) {
        this.wishlistId = wishlistId;
        this.tmdbId = tmdbId;
    }
}
