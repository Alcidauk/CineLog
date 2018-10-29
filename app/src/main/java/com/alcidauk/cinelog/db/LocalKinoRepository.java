package com.alcidauk.cinelog.db;

import com.alcidauk.cinelog.dao.DaoSession;
import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.dao.LocalKinoDao;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class LocalKinoRepository {

    private LocalKinoDao localKinoDao;

    public LocalKinoRepository(DaoSession daoSession) {
        this.localKinoDao = daoSession.getLocalKinoDao();
    }

    public List<LocalKino> findAll() {
        return localKinoDao.loadAll();
    }

    // TODO create tmdbkino if needed
    public void createOrUpdate(List<LocalKino> localKinos) {
        localKinoDao.insertInTx(localKinos);
    }

    public void createOrUpdate(LocalKino kinoToCreate) {
        localKinoDao.insertOrReplace(kinoToCreate);
    }

    public LocalKino find(long id) {
        return localKinoDao.load(id);
    }

    public LocalKino findByMovieId(long movieId) {
        Query<LocalKino> localKinoQuery = localKinoDao.queryBuilder()
                .where(LocalKinoDao.Properties.Tmdb_id.eq(movieId))
                .limit(1)
                .build();
        List<LocalKino> localKinos = localKinoQuery.list();
        return localKinos != null && localKinos.size() > 0 ? localKinos.get(0) : null;
    }

    public List<LocalKino> findAllByRating(boolean asc) {
        QueryBuilder<LocalKino> localKinoQueryBuilder = localKinoDao.queryBuilder();

        if (asc) {
            localKinoQueryBuilder = localKinoQueryBuilder
                    .orderAsc(LocalKinoDao.Properties.Rating);
        } else {
            localKinoQueryBuilder = localKinoQueryBuilder
                    .orderDesc(LocalKinoDao.Properties.Rating);
        }

        return localKinoQueryBuilder.build().list();
    }

    public void delete(Long localKinoId) {
        localKinoDao.deleteByKey(localKinoId);
    }
}
