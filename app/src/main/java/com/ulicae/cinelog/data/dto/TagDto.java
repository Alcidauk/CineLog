package com.ulicae.cinelog.data.dto;

import org.parceler.Parcel;

import java.util.Objects;

/**
 * CineLog Copyright 2018 Pierre Rognon
 *
 *
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 *
 */
// need to have weakerAccess to allow use of parcel
@Parcel
public class TagDto implements ItemDto {

    Long id;
    String name;
    String color;

    boolean forMovies;
    boolean forSeries;

    public TagDto() {
    }

    public TagDto(Long id, String name, String color, boolean forMovies, boolean forSeries) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.forMovies = forMovies;
        this.forSeries = forSeries;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isForMovies() {
        return forMovies;
    }

    public void setForMovies(boolean forMovies) {
        this.forMovies = forMovies;
    }

    public boolean isForSeries() {
        return forSeries;
    }

    public void setForSeries(boolean forSeries) {
        this.forSeries = forSeries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDto tagDto = (TagDto) o;
        return forMovies == tagDto.forMovies && forSeries == tagDto.forSeries && Objects.equals(id, tagDto.id) && Objects.equals(name, tagDto.name) && Objects.equals(color, tagDto.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, forMovies, forSeries);
    }
}
