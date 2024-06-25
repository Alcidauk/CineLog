package com.ulicae.cinelog.room.entities;

import androidx.room.Entity;

import java.util.Objects;

@Entity(primaryKeys = {"wishlistId", "tmdbId"})
public class WishlistTmdbCrossRef {

    public long wishlistId;
    public long tmdbId;

    public WishlistTmdbCrossRef(long wishlistId, long tmdbId) {
        this.wishlistId = wishlistId;
        this.tmdbId = tmdbId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishlistTmdbCrossRef that = (WishlistTmdbCrossRef) o;
        return wishlistId == that.wishlistId && tmdbId == that.tmdbId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(wishlistId, tmdbId);
    }
}
