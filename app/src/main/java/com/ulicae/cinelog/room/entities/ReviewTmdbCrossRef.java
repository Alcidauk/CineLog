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
@Entity(primaryKeys = {"review_id", "media_id"})
public class ReviewTmdbCrossRef {

    @ColumnInfo(name = "review_id")
    public int reviewId;

    @ColumnInfo(name = "media_id")
    public int mediaId;

    public ReviewTmdbCrossRef(int reviewId, int mediaId) {
        this.reviewId = reviewId;
        this.mediaId = mediaId;
    }
}
