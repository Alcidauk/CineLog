package com.ulicae.cinelog.db;

import com.ulicae.cinelog.dao.DaoSession;
import com.ulicae.cinelog.dao.TmdbKino;
import com.ulicae.cinelog.dao.TmdbKinoDao;

public class TmdbKinoRepository {

    private TmdbKinoDao tmdbKinoDao;

    public TmdbKinoRepository(DaoSession daoSession) {
        this.tmdbKinoDao = daoSession.getTmdbKinoDao();
    }

    public void createOrUpdate(TmdbKino tmdbKino) {
        tmdbKinoDao.insertOrReplace(tmdbKino);
    }

    public TmdbKino find(long id) {
        return tmdbKinoDao.load(id);
    }
}
