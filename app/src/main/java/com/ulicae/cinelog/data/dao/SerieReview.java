package com.ulicae.cinelog.data.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.parceler.Parcel;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

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
@Parcel
@Entity
public class SerieReview {

    @Id(autoincrement = true)
    Long id;

    @ToOne(joinProperty = "tmdb_id")
    TmdbSerie serie;
    long tmdb_id;

    @ToOne(joinProperty = "review_id")
    Review review;
    long review_id;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 147306189)
    private transient SerieReviewDao myDao;

    public SerieReview(Long id, TmdbSerie serie, Review review) {
        this.id = id;
        if(serie != null) {
            setSerie(serie);
        }
        if(review != null) {
            setReview(review);
        }
    }

    @Generated(hash = 1176680609)
    public SerieReview(Long id, long tmdb_id, long review_id) {
        this.id = id;
        this.tmdb_id = tmdb_id;
        this.review_id = review_id;
    }
    @Generated(hash = 1470500809)
    public SerieReview() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getTmdb_id() {
        return this.tmdb_id;
    }
    public void setTmdb_id(long tmdb_id) {
        this.tmdb_id = tmdb_id;
    }
    public long getReview_id() {
        return this.review_id;
    }
    public void setReview_id(long review_id) {
        this.review_id = review_id;
    }
    @Generated(hash = 801892098)
    private transient Long serie__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1090761680)
    public TmdbSerie getSerie() {
        long __key = this.tmdb_id;
        if (serie__resolvedKey == null || !serie__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TmdbSerieDao targetDao = daoSession.getTmdbSerieDao();
            TmdbSerie serieNew = targetDao.load(__key);
            synchronized (this) {
                serie = serieNew;
                serie__resolvedKey = __key;
            }
        }
        return serie;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 831661885)
    public void setSerie(@NotNull TmdbSerie serie) {
        if (serie == null) {
            throw new DaoException(
                    "To-one property 'tmdb_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.serie = serie;
            tmdb_id = serie.getSerie_id();
            serie__resolvedKey = tmdb_id;
        }
    }
    @Generated(hash = 1607015409)
    private transient Long review__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 654994440)
    public Review getReview() {
        long __key = this.review_id;
        if (review__resolvedKey == null || !review__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ReviewDao targetDao = daoSession.getReviewDao();
            Review reviewNew = targetDao.load(__key);
            synchronized (this) {
                review = reviewNew;
                review__resolvedKey = __key;
            }
        }
        return review;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1185623951)
    public void setReview(@NotNull Review review) {
        if (review == null) {
            throw new DaoException(
                    "To-one property 'review_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.review = review;
            review_id = review.getId();
            review__resolvedKey = review_id;
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
        SerieReview that = (SerieReview) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(serie, that.serie) &&
                Objects.equals(review, that.review);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, serie, review);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 657109801)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSerieReviewDao() : null;
    }
}
