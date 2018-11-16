package com.ulicae.cinelog.db;

import com.ulicae.cinelog.dao.DaoSession;
import com.ulicae.cinelog.dao.LocalKino;
import com.ulicae.cinelog.dao.LocalKinoDao;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
public class LocalKinoRepository {

    private LocalKinoDao localKinoDao;

    public LocalKinoRepository(DaoSession daoSession) {
        this.localKinoDao = daoSession.getLocalKinoDao();
    }

    public void createOrUpdate(LocalKino kinoToCreate) {
        localKinoDao.insertOrReplace(kinoToCreate);
    }

    public void delete(Long localKinoId) {
        localKinoDao.deleteByKey(localKinoId);
    }

    public List<LocalKino> findAll() {
        return localKinoDao.loadAll();
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
        return queryOrderBy(asc, LocalKinoDao.Properties.Rating);
    }

    public List<LocalKino> findAllByYear(final boolean asc) {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

        QueryBuilder<LocalKino> localKinoQueryBuilder = localKinoDao.queryBuilder();

        List<LocalKino> list = localKinoQueryBuilder.build().list();

        Collections.sort(list, new Comparator<LocalKino>() {
            @Override
            public int compare(LocalKino o1, LocalKino o2) {
                int o1year = o1.getKino() != null ? o1.getKino().getYear() : 0;
                int o2year = o2.getKino() != null ? o2.getKino().getYear() : 0;

                if(asc) {
                    return o1year < o2year ? -1 : 1;
                } else {
                    return o1year < o2year ? 1 : -1;
                }
            }
        });

        return list;
    }

    public List<LocalKino> findAllByReviewDate(boolean asc) {
        return queryOrderBy(asc, LocalKinoDao.Properties.Review_date);
    }

    private List<LocalKino> queryOrderBy(boolean asc, Property property) {
        QueryBuilder<LocalKino> localKinoQueryBuilder = localKinoDao.queryBuilder();

        if (asc) {
            localKinoQueryBuilder = localKinoQueryBuilder
                    .orderAsc(property);
        } else {
            localKinoQueryBuilder = localKinoQueryBuilder
                    .orderDesc(property);
        }

        return localKinoQueryBuilder.build().list();
    }
}
