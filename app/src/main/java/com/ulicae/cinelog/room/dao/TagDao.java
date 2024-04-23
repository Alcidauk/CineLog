package com.ulicae.cinelog.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;

import com.ulicae.cinelog.room.entities.Tag;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;


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
@Dao
public interface TagDao extends RoomDao<Tag> {

    @Delete
    void delete(Tag tag);

    @Query("SELECT * FROM tag")
    Flowable<List<Tag>> findAll();

    /* TODO utilisation dans les pages d'ajout de tag
    @Query("SELECT * FROM tag WHERE for_movies = :forMovies AND for_series = :forSeries")
    Flowable<List<Tag>> findAllForItem(boolean forMovies, boolean forSeries);*/

    @Query("SELECT * FROM tag WHERE id = :id")
    Flowable<Tag> find(long id);

}
