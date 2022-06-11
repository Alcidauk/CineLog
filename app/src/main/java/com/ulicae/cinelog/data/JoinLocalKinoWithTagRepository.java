package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.JoinLocalKinoWithTag;
import com.ulicae.cinelog.data.dao.JoinLocalKinoWithTagDao;

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
public class JoinLocalKinoWithTagRepository extends AbstractJoinWithTagRepository<JoinLocalKinoWithTagDao, JoinLocalKinoWithTag> {

    public JoinLocalKinoWithTagRepository(DaoSession daoSession) {
        super(daoSession.getJoinLocalKinoWithTagDao());
    }

    @Override
    public JoinLocalKinoWithTag findByTagAndEntityId(long tagId, long kinoId) {
        Query<JoinLocalKinoWithTag> query = dao.queryBuilder()
                .where(JoinLocalKinoWithTagDao.Properties.LocalKinoId.eq(kinoId))
                .where(JoinLocalKinoWithTagDao.Properties.TagId.eq(tagId))
                .limit(1)
                .build();
        List<JoinLocalKinoWithTag> results = query.list();
        return results != null && results.size() > 0 ? results.get(0) : null;
    }

    @Override
    public List<JoinLocalKinoWithTag> findAllByTag(long tagId) {
        Query<JoinLocalKinoWithTag> query = dao.queryBuilder()
                .where(JoinLocalKinoWithTagDao.Properties.TagId.eq(tagId))
                .limit(1)
                .build();
        return query.list();
    }

    @Override
    public void createJoin(long tagId, long entityId) {
        createOrUpdate(new JoinLocalKinoWithTag(tagId, entityId));
    }
}
