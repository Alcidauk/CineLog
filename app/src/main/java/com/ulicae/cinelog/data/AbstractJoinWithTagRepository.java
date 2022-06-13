package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.JoinWithTag;

import org.greenrobot.greendao.AbstractDao;

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
public abstract class AbstractJoinWithTagRepository<T extends AbstractDao<E, Long>, E extends JoinWithTag>
        extends CrudRepository<T, E> {

    public AbstractJoinWithTagRepository(T dao) {
        super(dao);
    }

    public abstract E findByTagAndEntityId(long tagId, long kinoId);
    public abstract void createJoin(long tagId, long entityId);
    public abstract List<E> findAllByTag(long tagId);

}
