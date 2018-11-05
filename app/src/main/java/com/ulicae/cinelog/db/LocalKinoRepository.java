package com.ulicae.cinelog.db;

import com.ulicae.cinelog.dao.DaoSession;
import com.ulicae.cinelog.dao.LocalKino;
import com.ulicae.cinelog.dao.LocalKinoDao;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

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
public class LocalKinoRepository {

    private LocalKinoDao localKinoDao;

    public LocalKinoRepository(DaoSession daoSession) {
        this.localKinoDao = daoSession.getLocalKinoDao();
    }

    public List<LocalKino> findAll() {
        return localKinoDao.loadAll();
    }

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
