package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.LocalKino;
import com.ulicae.cinelog.data.dao.LocalKinoDao;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

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
public class LocalKinoRepository extends CrudRepository<LocalKinoDao, LocalKino> {

    public LocalKinoRepository(DaoSession daoSession) {
        super(daoSession.getLocalKinoDao());
    }

    public LocalKino findByMovieId(long movieId) {
        Query<LocalKino> localKinoQuery = dao.queryBuilder()
                .where(LocalKinoDao.Properties.Tmdb_id.eq(movieId))
                .limit(1)
                .build();
        List<LocalKino> localKinos = localKinoQuery.list();
        return localKinos != null && localKinos.size() > 0 ? localKinos.get(0) : null;
    }

    public List<LocalKino> findAllByRating(boolean asc) {
        return queryOrderBy(asc, LocalKinoDao.Properties.Rating);
    }

    public List<LocalKino> findAllByYear(boolean asc) {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

        QueryBuilder<LocalKino> localKinoQueryBuilder = dao.queryBuilder();

        List<LocalKino> list = localKinoQueryBuilder.build().list();

        Comparator<LocalKino> comparator;
        if (asc) {
            comparator = new Comparator<LocalKino>() {
                @Override
                public int compare(LocalKino o1, LocalKino o2) {
                    int o1year = o1.getKino() != null ? o1.getKino().getYear() : 0;
                    int o2year = o2.getKino() != null ? o2.getKino().getYear() : 0;
                    if (o1year == o2year) {
                        return 0;
                    }
                    return o1year < o2year ? -1 : 1;
                }
            };
        } else {
            comparator = new Comparator<LocalKino>() {
                @Override
                public int compare(LocalKino o1, LocalKino o2) {
                    int o1year = o1.getKino() != null ? o1.getKino().getYear() : 0;
                    int o2year = o2.getKino() != null ? o2.getKino().getYear() : 0;
                    if (o1year == o2year) {
                        return 0;
                    }
                    return o1year < o2year ? 1 : -1;
                }
            };
        }

        Collections.sort(list, comparator);
        return list;
    }

    public List<LocalKino> findAllByReviewDate(boolean asc) {
        return queryOrderBy(asc, LocalKinoDao.Properties.Review_date);
    }

    public List<LocalKino> findAllByTitle(final boolean asc) {
        QueryBuilder<LocalKino> localKinoQueryBuilder = dao.queryBuilder();

        List<LocalKino> list = localKinoQueryBuilder.build().list();

        Collections.sort(list, new Comparator<LocalKino>() {
            @Override
            public int compare(LocalKino o1, LocalKino o2) {
                if (o1.getTitle() == null) {
                    return -1;
                } else if (o2.getTitle() == null) {
                    return 1;
                } else {
                    // TODO take care of locale
                    return Collator.getInstance().compare(o1.getTitle(), o2.getTitle());
                }
            }
        });

        if(!asc){
            Collections.reverse(list);
        }

        return list;
    }

    private List<LocalKino> queryOrderBy(boolean asc, Property property) {
        QueryBuilder<LocalKino> localKinoQueryBuilder = dao.queryBuilder();

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
