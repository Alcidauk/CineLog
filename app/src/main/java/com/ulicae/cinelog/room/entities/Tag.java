package com.ulicae.cinelog.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
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
public class Tag {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public String color;

    @ColumnInfo(name = "for_movies")
    public boolean forMovies;
    @ColumnInfo(name = "for_series")
    public boolean forSeries;

    public Tag(int id, String name, String color, boolean forMovies, boolean forSeries) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.forMovies = forMovies;
        this.forSeries = forSeries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return id == tag.id && forMovies == tag.forMovies && forSeries == tag.forSeries && Objects.equals(name, tag.name) && Objects.equals(color, tag.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, forMovies, forSeries);
    }
}
