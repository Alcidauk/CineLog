package com.ulicae.cinelog.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
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
@Entity(
        foreignKeys = {
                @ForeignKey(entity = Review.class,
                parentColumns = "id",
                childColumns = "review_id")
        })
public class TmdbSerieEpisode {

    @PrimaryKey
    @ColumnInfo(name = "tmdb_episode_id")
    public long tmdbEpisodeId;

    @ColumnInfo(name = "review_id")
    public long reviewId;

    @ColumnInfo(name = "watching_date")
    public Date watchingDate;

    public TmdbSerieEpisode(long tmdbEpisodeId, long reviewId, Date watchingDate) {
        this.tmdbEpisodeId = tmdbEpisodeId;
        this.reviewId = reviewId;
        this.watchingDate = watchingDate;
    }

    public long getTmdbEpisodeId() {
        return tmdbEpisodeId;
    }

    public void setTmdbEpisodeId(long tmdbEpisodeId) {
        this.tmdbEpisodeId = tmdbEpisodeId;
    }

    public Date getWatchingDate() {
        return watchingDate;
    }

    public void setWatchingDate(Date watchingDate) {
        this.watchingDate = watchingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TmdbSerieEpisode that = (TmdbSerieEpisode) o;
        return tmdbEpisodeId == that.tmdbEpisodeId && Objects.equals(watchingDate, that.watchingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tmdbEpisodeId, watchingDate);
    }
}