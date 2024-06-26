package com.ulicae.cinelog.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;


@Entity
public class WishlistItem {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "item_entity_type")
    public ItemEntityType itemEntityType;

    public String title;

    @Embedded
    public Tmdb tmdb;

    public WishlistItem(long id, ItemEntityType itemEntityType, String title, Tmdb tmdb) {
        this.id = id;
        this.itemEntityType = itemEntityType;
        this.title = title;
        this.tmdb = tmdb;
    }

    public long getId() {
        return id;
    }

    public ItemEntityType getItemEntityType() {
        return itemEntityType;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishlistItem item = (WishlistItem) o;
        return id == item.id && itemEntityType == item.itemEntityType && Objects.equals(title, item.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemEntityType, title);
    }
}
