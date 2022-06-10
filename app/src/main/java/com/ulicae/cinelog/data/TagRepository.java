package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.JoinLocalKinoWithTag;
import com.ulicae.cinelog.data.dao.JoinLocalKinoWithTagDao;
import com.ulicae.cinelog.data.dao.Tag;
import com.ulicae.cinelog.data.dao.TagDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * CineLog Copyright 2022 Pierre Rognon
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
public class TagRepository extends CrudRepository<TagDao, Tag> {

    public TagRepository(DaoSession daoSession) {
        super(daoSession.getTagDao());
    }

    public List<Tag> findMovieTags() {
        Query<Tag> query = dao.queryBuilder()
                .where(TagDao.Properties.ForMovies.eq(true))
                .build();
        return query.list();
    }

    public List<Tag> findSeriesTags() {
        Query<Tag> query = dao.queryBuilder()
                .where(TagDao.Properties.ForSeries.eq(true))
                .build();
        return query.list();
    }

}
