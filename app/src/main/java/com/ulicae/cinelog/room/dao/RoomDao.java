package com.ulicae.cinelog.room.dao;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;


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
public interface RoomDao<T> {

    @Insert
    Completable insertAll(List<T> entities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(T entity);
}
