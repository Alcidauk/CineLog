package com.ulicae.cinelog.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

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
    public int id;

    @ColumnInfo(name = "item_entity_type")
    public ItemEntityType itemEntityType;

    public String title;

    @ColumnInfo(name = "review_date")
    public Date reviewDate;

    public String review;

    public Float rating;

    @ColumnInfo(name = "max_rating")
    public Integer maxRating;

    public Review(int id, ItemEntityType itemEntityType, String title, Date reviewDate, String review, Float rating, Integer maxRating) {
        this.id = id;
        this.itemEntityType = itemEntityType;
        this.title = title;
        this.reviewDate = reviewDate;
        this.review = review;
        this.rating = rating;
        this.maxRating = maxRating;
    }

}

