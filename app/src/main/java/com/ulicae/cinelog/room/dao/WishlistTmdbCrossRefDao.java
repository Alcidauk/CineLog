package com.ulicae.cinelog.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;

import com.ulicae.cinelog.room.entities.WishlistTmdbCrossRef;

import java.util.List;

import io.reactivex.Flowable;

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
public interface WishlistTmdbCrossRefDao extends RoomDao<WishlistTmdbCrossRef> {

    @Delete
    void delete(WishlistTmdbCrossRef wishlistTmdbCrossRef);

    @Query("SELECT * FROM wishlisttmdbcrossref")
    Flowable<List<WishlistTmdbCrossRef>> findAll();

    @Query("SELECT * FROM wishlisttmdbcrossref WHERE wishlistId = :wishlistId")
    Flowable<List<WishlistTmdbCrossRef>> findForReview(long wishlistId);

}