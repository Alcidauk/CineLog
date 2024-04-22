package com.ulicae.cinelog.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"review_id", "tag_id"})
public class ReviewTagCrossRef {

    @ColumnInfo(name = "review_id")
    public int reviewId;

    @ColumnInfo(name = "tag_id")
    public int tagId;

    public ReviewTagCrossRef(int reviewId, int tagId) {
        this.reviewId = reviewId;
        this.tagId = tagId;
    }

}
