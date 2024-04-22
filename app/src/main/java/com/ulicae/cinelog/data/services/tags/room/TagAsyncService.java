package com.ulicae.cinelog.data.services.tags.room;

import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.services.AsyncDataService;
import com.ulicae.cinelog.room.dao.TagDao;
import com.ulicae.cinelog.room.entities.Tag;

import io.reactivex.Completable;

/**
 * CineLog Copyright 2024 Pierre Rognon
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
public class TagAsyncService implements AsyncDataService<TagDto> {

    private final TagDao tagDao;

    public TagAsyncService(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public Completable createOrUpdate(TagDto dtoObject) {
        return tagDao.insert(
                new Tag(
                        dtoObject.getId() != null ? Math.toIntExact(dtoObject.getId()) : 0,
                        dtoObject.getName(),
                        dtoObject.getColor(),
                        dtoObject.isForMovies(),
                        dtoObject.isForSeries()
                ));
    }
}
