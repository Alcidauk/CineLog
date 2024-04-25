package com.ulicae.cinelog.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public abstract class Media {

    @PrimaryKey
    @ColumnInfo
    public int id;

    @ColumnInfo
    public String title;

    public Media(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
