package com.ulicae.cinelog.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

/**
 * CineLog Copyright 2024 Pierre Rognon
 * <p>
 * <p>
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 */
@Entity
public class Review {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "item_entity_type")
    public ItemEntityType itemEntityType;

    public String title;

    @ColumnInfo(name = "review_date")
    public Date reviewDate;

    public String review;

    public Float rating;

    @ColumnInfo(name = "max_rating")
    public Integer maxRating;

    public Review(long id, ItemEntityType itemEntityType, String title, Date reviewDate, String review, Float rating, Integer maxRating) {
        this.id = id;
        this.itemEntityType = itemEntityType;
        this.title = title;
        this.reviewDate = reviewDate;
        this.review = review;
        this.rating = rating;
        this.maxRating = maxRating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review1 = (Review) o;
        return id == review1.id && itemEntityType == review1.itemEntityType && Objects.equals(title, review1.title) && Objects.equals(reviewDate, review1.reviewDate) && Objects.equals(review, review1.review) && Objects.equals(rating, review1.rating) && Objects.equals(maxRating, review1.maxRating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemEntityType, title, reviewDate, review, rating, maxRating);
    }
}

