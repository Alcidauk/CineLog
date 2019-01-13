package com.ulicae.cinelog.data.dao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Objects;

@Entity
public class WishlistMovie {

    @Id
    Long wishlist_movie_id;

    @ToOne(joinProperty = "tmdb_id")
    TmdbKino movie;
    long tmdb_id;

    String title;

    String comment;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 564566955)
    private transient WishlistMovieDao myDao;

    @Generated(hash = 708760245)
    private transient Long movie__resolvedKey;

    public WishlistMovie(Long wishlist_movie_id, TmdbKino movie, String title, String comment) {
        this.wishlist_movie_id = wishlist_movie_id;
        this.title = title;
        this.comment = comment;
        if (movie != null) {
            setMovie(movie);
        }
    }

    @Generated(hash = 2053796790)
    public WishlistMovie(Long wishlist_movie_id, long tmdb_id, String title, String comment) {
        this.wishlist_movie_id = wishlist_movie_id;
        this.tmdb_id = tmdb_id;
        this.title = title;
        this.comment = comment;
    }

    @Generated(hash = 1441381582)
    public WishlistMovie() {
    }

    public Long getWishlist_movie_id() {
        return this.wishlist_movie_id;
    }

    public void setWishlist_movie_id(Long wishlist_movie_id) {
        this.wishlist_movie_id = wishlist_movie_id;
    }

    public long getTmdb_id() {
        return this.tmdb_id;
    }

    public void setTmdb_id(long tmdb_id) {
        this.tmdb_id = tmdb_id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 2004684038)
    public TmdbKino getMovie() {
        long __key = this.tmdb_id;
        if (movie__resolvedKey == null || !movie__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TmdbKinoDao targetDao = daoSession.getTmdbKinoDao();
            TmdbKino movieNew = targetDao.load(__key);
            synchronized (this) {
                movie = movieNew;
                movie__resolvedKey = __key;
            }
        }
        return movie;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1807219775)
    public void setMovie(@NotNull TmdbKino movie) {
        if (movie == null) {
            throw new DaoException(
                    "To-one property 'tmdb_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.movie = movie;
            tmdb_id = movie.getMovie_id();
            movie__resolvedKey = tmdb_id;
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
        WishlistMovie that = (WishlistMovie) o;
        return tmdb_id == that.tmdb_id &&
                Objects.equals(wishlist_movie_id, that.wishlist_movie_id) &&
                Objects.equals(movie, that.movie) &&
                Objects.equals(title, that.title) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {

        return Objects.hash(wishlist_movie_id, movie, tmdb_id, title, comment);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1977406893)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getWishlistMovieDao() : null;
    }
}
