package com.ulicae.cinelog.data.dto;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * CineLog Copyright 2022 Pierre Rognon
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
@Parcel
public class SerieDto extends KinoDto {

    Long reviewId;

    public SerieDto() {
    }

    // TODO improve it
    public SerieDto(Long kinoId, Long tmdbKinoId, Long reviewId, String title, Date review_date,
                    String review, Float rating, Integer maxRating, String posterPath,
                    String overview, int year, String releaseDate, List<TagDto> tags) {
        super(kinoId, tmdbKinoId, title, review_date, review, rating, maxRating, posterPath,
                overview, year, releaseDate, tags);
        this.reviewId = reviewId;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SerieDto serieDto = (SerieDto) o;
        return Objects.equals(reviewId, serieDto.reviewId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), reviewId);
    }
}
