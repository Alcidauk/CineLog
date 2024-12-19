package com.ulicae.cinelog.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.util.Objects;

@Entity(
        primaryKeys = {"review_id", "tag_id"},
        foreignKeys = {
        @ForeignKey(
                entity = Review.class,
                parentColumns = "id",
                childColumns = "review_id"
        ),
        @ForeignKey(
                entity = Tag.class,
                parentColumns = "id",
                childColumns = "tag_id"
        )
})
public class ReviewTagCrossRef {

    @ColumnInfo(name = "review_id")
    public long reviewId;

    @ColumnInfo(name = "tag_id")
    public long tagId;

    public ReviewTagCrossRef(long reviewId, long tagId) {
        this.reviewId = reviewId;
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewTagCrossRef that = (ReviewTagCrossRef) o;
        return reviewId == that.reviewId && tagId == that.tagId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId, tagId);
    }
}
