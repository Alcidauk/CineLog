package com.ulicae.cinelog.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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
public class Tmdb {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "tmdb_id")
    public long tmdbId;

    @ColumnInfo
    public ItemEntityType type;

    @ColumnInfo(name = "poster_path")
    public String posterPath;
    @ColumnInfo
    public String overview;

    @ColumnInfo
    public int year;

    @ColumnInfo(name = "release_date")
    public String releaseDate;

    public Tmdb(long tmdbId, ItemEntityType type, String posterPath, String overview, int year, String releaseDate) {
        this.tmdbId = tmdbId;
        this.type = type;
        this.posterPath = posterPath;
        this.overview = overview;
        this.year = year;
        this.releaseDate = releaseDate;
    }

    public long getId() {
        return id;
    }

    public long getTmdbId() {
        return tmdbId;
    }

    public ItemEntityType getType() {
        return type;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public int getYear() {
        return year;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tmdb tmdb = (Tmdb) o;
        return id == tmdb.id && tmdbId == tmdb.tmdbId && year == tmdb.year && type == tmdb.type && Objects.equals(posterPath, tmdb.posterPath) && Objects.equals(overview, tmdb.overview) && Objects.equals(releaseDate, tmdb.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tmdbId, type, posterPath, overview, year, releaseDate);
    }
}
