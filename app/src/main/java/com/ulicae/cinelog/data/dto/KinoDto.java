package com.ulicae.cinelog.data.dto;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

/**
 * CineLog Copyright 2022 Pierre Rognon
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
@SuppressWarnings("WeakerAccess")
@Parcel
public class KinoDto implements ItemDto {

    Long kinoId;
    Long tmdbKinoId;

    String title;

    Date review_date;
    String review;

    Float rating;
    Integer maxRating;

    String posterPath;
    String overview;
    int year;
    String releaseDate;

    List<TagDto> tags;

    public KinoDto() {
    }

    public KinoDto(Long kinoId, Long tmdbKinoId, String title, Date review_date, String review,
                   Float rating, Integer maxRating, String posterPath, String overview,
                   int year, String releaseDate, List<TagDto> tags) {
        this.kinoId = kinoId;
        this.tmdbKinoId = tmdbKinoId;
        this.title = title;
        this.review_date = review_date;
        this.review = review;
        this.rating = rating;
        this.maxRating = maxRating;
        this.posterPath = posterPath;
        this.overview = overview;
        this.year = year;
        this.releaseDate = releaseDate;
        this.tags = tags;
    }

    public Long getKinoId() {
        return kinoId;
    }

    public void setKinoId(Long kinoId) {
        this.kinoId = kinoId;
    }

    public Long getTmdbKinoId() {
        return tmdbKinoId;
    }

    public void setTmdbKinoId(Long tmdbKinoId) {
        this.tmdbKinoId = tmdbKinoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReview_date() {
        return review_date;
    }

    public void setReview_date(Date review_date) {
        this.review_date = review_date;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(Integer maxRating) {
        this.maxRating = maxRating;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KinoDto kinoDto = (KinoDto) o;

        if (year != kinoDto.year) return false;
        if (kinoId != null ? !kinoId.equals(kinoDto.kinoId) : kinoDto.kinoId != null) return false;
        if (tmdbKinoId != null ? !tmdbKinoId.equals(kinoDto.tmdbKinoId) : kinoDto.tmdbKinoId != null)
            return false;
        if (title != null ? !title.equals(kinoDto.title) : kinoDto.title != null) return false;
        if (review_date != null ? !review_date.equals(kinoDto.review_date) : kinoDto.review_date != null)
            return false;
        if (review != null ? !review.equals(kinoDto.review) : kinoDto.review != null) return false;
        if (rating != null ? !rating.equals(kinoDto.rating) : kinoDto.rating != null) return false;
        if (maxRating != null ? !maxRating.equals(kinoDto.maxRating) : kinoDto.maxRating != null)
            return false;
        if (posterPath != null ? !posterPath.equals(kinoDto.posterPath) : kinoDto.posterPath != null)
            return false;
        if (overview != null ? !overview.equals(kinoDto.overview) : kinoDto.overview != null)
            return false;
        return releaseDate != null ? releaseDate.equals(kinoDto.releaseDate) : kinoDto.releaseDate == null;
    }

    @Override
    public int hashCode() {
        int result = kinoId != null ? kinoId.hashCode() : 0;
        result = 31 * result + (tmdbKinoId != null ? tmdbKinoId.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (review_date != null ? review_date.hashCode() : 0);
        result = 31 * result + (review != null ? review.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        result = 31 * result + (maxRating != null ? maxRating.hashCode() : 0);
        result = 31 * result + (posterPath != null ? posterPath.hashCode() : 0);
        result = 31 * result + (overview != null ? overview.hashCode() : 0);
        result = 31 * result + year;
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "KinoDto{" +
                "kinoId=" + kinoId +
                ", tmdbKinoId=" + tmdbKinoId +
                ", title='" + title + '\'' +
                ", review_date=" + review_date +
                ", review='" + review + '\'' +
                ", rating=" + rating +
                ", maxRating=" + maxRating +
                ", posterPath='" + posterPath + '\'' +
                ", overview='" + overview + '\'' +
                ", year=" + year +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
