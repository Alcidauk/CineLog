package com.ulicae.cinelog.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

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
@Entity(primaryKeys = {"review_id", "movie_id"})
public class ReviewTmdbCrossRef {

    @ColumnInfo(name = "review_id")
    public long reviewId;

    @ColumnInfo(name = "movie_id")
    public long movieId;

    public ReviewTmdbCrossRef(long reviewId, long movieId) {
        this.reviewId = reviewId;
        this.movieId = movieId;
    }
}
