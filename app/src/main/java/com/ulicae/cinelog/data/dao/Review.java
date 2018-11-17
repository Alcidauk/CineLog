package com.ulicae.cinelog.data.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.parceler.Parcel;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

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
@Parcel
@Entity
public class Review {

    @Id(autoincrement = true)
    Long id;

    @NotNull
    String title;

    Date review_date;
    String review;

    Float rating;
    Integer maxRating;

    @Generated(hash = 2003261521)
    public Review(Long id, @NotNull String title, Date review_date, String review,
            Float rating, Integer maxRating) {
        this.id = id;
        this.title = title;
        this.review_date = review_date;
        this.review = review;
        this.rating = rating;
        this.maxRating = maxRating;
    }
    @Generated(hash = 2008964488)
    public Review() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Date getReview_date() {
        return this.review_date;
    }
    public void setReview_date(Date review_date) {
        this.review_date = review_date;
    }
    public String getReview() {
        return this.review;
    }
    public void setReview(String review) {
        this.review = review;
    }
    public Float getRating() {
        return this.rating;
    }
    public void setRating(Float rating) {
        this.rating = rating;
    }
    public Integer getMaxRating() {
        return this.maxRating;
    }
    public void setMaxRating(Integer maxRating) {
        this.maxRating = maxRating;
    }
}
