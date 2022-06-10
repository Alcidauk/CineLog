package com.ulicae.cinelog.data.dao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.parceler.Parcel;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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
@Parcel
@Entity
public class Review {

    @Id(autoincrement = true)
    Long id;

    @NotNull
    String title;

    @ToMany
    @JoinEntity(
            entity = JoinReviewWithTag.class,
            sourceProperty = "reviewId",
            targetProperty = "tagId"
    )
    List<Tag> tags;
    Date review_date;
    String review;

    Float rating;
    Integer maxRating;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1047363205)
    private transient ReviewDao myDao;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review1 = (Review) o;
        return Objects.equals(id, review1.id) &&
                Objects.equals(title, review1.title) &&
                Objects.equals(review_date, review1.review_date) &&
                Objects.equals(review, review1.review) &&
                Objects.equals(rating, review1.rating) &&
                Objects.equals(maxRating, review1.maxRating);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, review_date, review, rating, maxRating);
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2038524768)
    public List<Tag> getTags() {
        if (tags == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TagDao targetDao = daoSession.getTagDao();
            List<Tag> tagsNew = targetDao._queryReview_Tags(id);
            synchronized (this) {
                if (tags == null) {
                    tags = tagsNew;
                }
            }
        }
        return tags;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 404234)
    public synchronized void resetTags() {
        tags = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1625721578)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getReviewDao() : null;
    }

}
