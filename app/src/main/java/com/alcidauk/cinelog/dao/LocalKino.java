package com.alcidauk.cinelog.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.parceler.Parcel;

import java.util.Date;
import org.greenrobot.greendao.DaoException;

/**
 * Created by ryan on 10/05/17.
 */
@Parcel
@Entity
public class LocalKino {

    long tmdb_id;

    @ToOne(joinProperty = "tmdb_id")
    TmdbKino kino;

    @Id(autoincrement = true)
    Long id;

    String poster_path;

    Float rating;
    Integer maxRating;
    String review;
    String overview;
    int year;

    @NotNull
    String title;
    String release_date;
    int movie_id;

    Date review_date;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 458053940)
    private transient LocalKinoDao myDao;

    @Generated(hash = 1191362854)
    private transient Long kino__resolvedKey;


    public LocalKino() {
    }

    public LocalKino(String poster_path, Float rating, String review, String overview, int year, String title, String release_date, int movie_id, Date review_date) {
        this.poster_path = poster_path;
        this.rating = rating;
        this.review = review;
        this.overview = overview;
        this.year = year;
        this.title = title;
        this.release_date = release_date;
        this.movie_id = movie_id;
        this.review_date = review_date;
    }

    public LocalKino(String title, String release_date, String poster_path, String overview, int year, int movie_id) {
        this.title = title;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.movie_id = movie_id;
        this.rating = 0f;
        this.overview = overview;
        this.year = year;
        //this.review = "";
        this.review_date = new Date();
    }

    @Generated(hash = 1689429566)
    public LocalKino(long tmdb_id, Long id, String poster_path, Float rating, Integer maxRating, String review, String overview, int year, @NotNull String title,
            String release_date, int movie_id, Date review_date) {
        this.tmdb_id = tmdb_id;
        this.id = id;
        this.poster_path = poster_path;
        this.rating = rating;
        this.maxRating = maxRating;
        this.review = review;
        this.overview = overview;
        this.year = year;
        this.title = title;
        this.release_date = release_date;
        this.movie_id = movie_id;
        this.review_date = review_date;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPoster_path() {
        return this.poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
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

    public String getRelease_date() {
        return this.release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getMovie_id() {
        return this.movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getOverview() {
        return this.overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
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

    /** To-one relationship, resolved on first access. */
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

    /** called by internal mechanisms, do not call yourself. */
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 346087300)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLocalKinoDao() : null;
    }
}
