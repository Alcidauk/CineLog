package com.alcidauk.cinelog.db;

import com.alcidauk.cinelog.dao.DaoSession;
import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.dao.LocalKinoDao;

import java.util.List;

public class LocalKinoRepository {

    private LocalKinoDao localKinoDao;

    public LocalKinoRepository(DaoSession daoSession) {
        this.localKinoDao = daoSession.getLocalKinoDao();
    }

    public List<LocalKino> findAll() {
        return localKinoDao.loadAll();
    }
}
