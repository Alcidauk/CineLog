package com.ulicae.cinelog.data.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Objects;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class WishlistSerie {

    @Id
    Long wishlist_serie_id;

    @ToOne(joinProperty = "tmdb_id")
    TmdbSerie serie;
    long tmdb_id;

    String title;

    String comment;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1897687674)
    private transient WishlistSerieDao myDao;

    public WishlistSerie(Long wishlist_serie_id, TmdbSerie serie, String title, String comment) {
        this.wishlist_serie_id = wishlist_serie_id;
        this.title = title;
        this.comment = comment;
        if(serie != null) {
            setSerie(serie);
        }
    }

    @Generated(hash = 1788962352)
    public WishlistSerie(Long wishlist_serie_id, long tmdb_id, String title, String comment) {
        this.wishlist_serie_id = wishlist_serie_id;
        this.tmdb_id = tmdb_id;
        this.title = title;
        this.comment = comment;
    }

    @Generated(hash = 490884657)
    public WishlistSerie() {
    }

    @Generated(hash = 801892098)
    private transient Long serie__resolvedKey;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishlistSerie that = (WishlistSerie) o;
        return tmdb_id == that.tmdb_id &&
                Objects.equals(wishlist_serie_id, that.wishlist_serie_id) &&
                Objects.equals(serie, that.serie) &&
                Objects.equals(title, that.title) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {

        return Objects.hash(wishlist_serie_id, serie, tmdb_id, title, comment);
    }

    public Long getWishlist_serie_id() {
        return this.wishlist_serie_id;
    }

    public void setWishlist_serie_id(Long wishlist_serie_id) {
        this.wishlist_serie_id = wishlist_serie_id;
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

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 688793257)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getWishlistSerieDao() : null;
    }
}
