package com.alcidauk.cinelog.db;

import com.alcidauk.cinelog.dao.DaoSession;
import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.dao.TmdbKino;
import com.alcidauk.cinelog.dao.TmdbKinoDao;

import java.util.List;

public class TmdbKinoRepository {

    private TmdbKinoDao tmdbKinoDao;

    public TmdbKinoRepository(DaoSession daoSession) {
        this.tmdbKinoDao = daoSession.getTmdbKinoDao();
    }

    public void create(TmdbKino tmdbKino) {
        tmdbKinoDao.insert(tmdbKino);
    }

}
