package com.ulicae.cinelog.data.dao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

/**
 * CineLog Copyright 2018 Pierre Rognon
 * kinolog Copyright (C) 2017  ryan rigby
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
public class LocalKino {

    @Id(autoincrement = true)
    Long id;

    @ToOne(joinProperty = "tmdb_id")
    TmdbKino kino;
    long tmdb_id;

    @NotNull
    String title;

    Date review_date;
    String review;

    Float rating;
    Integer maxRating;

    @ToMany
    @JoinEntity(
            entity = JoinLocalKinoWithTag.class,
            sourceProperty = "localKinoId",
            targetProperty = "tagId"
    )
    List<Tag> tags;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 458053940)
    private transient LocalKinoDao myDao;

    @Generated(hash = 1191362854)
    private transient Long kino__resolvedKey;

    public LocalKino() {
    }

    public LocalKino(Float rating, Integer maxRating, String review, String title, Date review_date,
                     TmdbKino tmdbKino, List<Tag> tags) {
        this.rating = rating;
        this.maxRating = maxRating;
        this.review = review;
        this.title = title;
        this.review_date = review_date;
        setKino(tmdbKino);
        this.tags = tags;
    }


    public LocalKino(Float rating, String review, String title, Date review_date, TmdbKino tmdbKino,
                     List<Tag> tags) {
        this.rating = rating;
        this.review = review;
        this.title = title;
        this.review_date = review_date;
        setKino(tmdbKino);
        this.tags = tags;
    }

    public LocalKino(String title) {
        this.title = title;
        this.rating = 0f;
        this.review_date = new Date();
    }

    @Generated(hash = 193728723)
    public LocalKino(Long id, long tmdb_id, @NotNull String title, Date review_date, String review, Float rating, Integer maxRating) {
        this.id = id;
        this.tmdb_id = tmdb_id;
        this.title = title;
        this.review_date = review_date;
        this.review = review;
        this.rating = rating;
        this.maxRating = maxRating;
    }

    public LocalKino(String title, TmdbKino tmdbKino) {
        this.title = title;
        setKino(tmdbKino);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getRating() {
        return this.rating;
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

    public String getReview() {
        return this.review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTitle() {
        return this.title;
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

    public Long getTmdb_id() {
        return this.tmdb_id;
    }

    public void setTmdb_id(Long tmdb_id) {
        this.tmdb_id = tmdb_id;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1387996076)
    public TmdbKino getKino() {
        long __key = this.tmdb_id;
        if (kino__resolvedKey == null || !kino__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TmdbKinoDao targetDao = daoSession.getTmdbKinoDao();
            TmdbKino kinoNew = targetDao.load(__key);
            synchronized (this) {
                kino = kinoNew;
                kino__resolvedKey = __key;
            }
        }
        return kino;
    }

    public void setTmdb_id(long tmdb_id) {
        this.tmdb_id = tmdb_id;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1992277457)
    public void setKino(@NotNull TmdbKino kino) {
        if (kino == null) {
            throw new DaoException("To-one property 'tmdb_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.kino = kino;
            tmdb_id = kino.getMovie_id();
            kino__resolvedKey = tmdb_id;
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalKino localKino = (LocalKino) o;

        if (tmdb_id != localKino.tmdb_id) return false;
        if (id != null ? !id.equals(localKino.id) : localKino.id != null) return false;
        if (kino != null ? !kino.equals(localKino.kino) : localKino.kino != null) return false;
        if (title != null ? !title.equals(localKino.title) : localKino.title != null) return false;
        if (review_date != null ? !review_date.equals(localKino.review_date) : localKino.review_date != null)
            return false;
        if (review != null ? !review.equals(localKino.review) : localKino.review != null)
            return false;
        if (rating != null ? !rating.equals(localKino.rating) : localKino.rating != null)
            return false;
        return maxRating != null ? maxRating.equals(localKino.maxRating) : localKino.maxRating == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (kino != null ? kino.hashCode() : 0);
        result = 31 * result + (int) (tmdb_id ^ (tmdb_id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (review_date != null ? review_date.hashCode() : 0);
        result = 31 * result + (review != null ? review.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        result = 31 * result + (maxRating != null ? maxRating.hashCode() : 0);
        return result;
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1436914097)
    public List<Tag> getTags() {
        if (tags == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TagDao targetDao = daoSession.getTagDao();
            List<Tag> tagsNew = targetDao._queryLocalKino_Tags(id);
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 346087300)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLocalKinoDao() : null;
    }
}
