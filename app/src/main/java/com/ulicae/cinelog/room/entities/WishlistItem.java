package com.ulicae.cinelog.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class WishlistItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "item_entity_type")
    public ItemEntityType itemEntityType;

    public String title;
    
    

    public WishlistItem(int id, ItemEntityType itemEntityType, String title) {
        this.id = id;
        this.itemEntityType = itemEntityType;
        this.title = title;
    }
}
