package com.ulicae.cinelog.data.dao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.parceler.Parcel;

import java.util.Date;

/**
 * CineLog Copyright 2018 Pierre Rognon
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
@Entity
public class SerieEpisode {

    @Id
    Long episodeId;

    Integer tmdbEpisodeId;

    @ToOne(joinProperty = "tmdb_id")
    TmdbSerie serie;
    long tmdb_id;

    Date watchingDate;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 2022022907)
    private transient SerieEpisodeDao myDao;

    @Generated(hash = 2017202277)
    public SerieEpisode(Long episodeId, Integer tmdbEpisodeId, long tmdb_id,
            Date watchingDate) {
        this.episodeId = episodeId;
        this.tmdbEpisodeId = tmdbEpisodeId;
        this.tmdb_id = tmdb_id;
        this.watchingDate = watchingDate;
    }

    @Generated(hash = 1461195645)
    public SerieEpisode() {
    }

    public Long getEpisodeId() {
        return this.episodeId;
    }

    public void setEpisodeId(Long episodeId) {
        this.episodeId = episodeId;
    }

    public Integer getTmdbEpisodeId() {
        return this.tmdbEpisodeId;
    }

    public void setTmdbEpisodeId(Integer tmdbEpisodeId) {
        this.tmdbEpisodeId = tmdbEpisodeId;
    }

    public long getTmdb_id() {
        return this.tmdb_id;
    }

    public void setTmdb_id(long tmdb_id) {
        this.tmdb_id = tmdb_id;
    }

    public Date getWatchingDate() {
        return this.watchingDate;
    }

    public void setWatchingDate(Date watchingDate) {
        this.watchingDate = watchingDate;
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
    @Generated(hash = 1529627527)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSerieEpisodeDao() : null;
    }


}
