package com.ulicae.cinelog.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Review;

import java.util.List;

import io.reactivex.Completable;
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
public interface ReviewDao extends RoomDao<Review> {

    /*@Delete
    void delete(Long kinoId);*/

    @Delete
    void delete(Review review);


    @Query("SELECT * FROM review WHERE item_entity_type = :itemEntitytype")
    Flowable<List<Review>> findAll(ItemEntityType itemEntitytype);

    @Query("SELECT * FROM review WHERE id = :id")
    Flowable<Review> find(long id);

    // TODO comment faire avec une crossref
    /*@Query("SELECT * FROM review WHERE tmdb_id = :tmdb_id")
    Flowable<Review> findByMovieId(Long tmdb_id);*/


    // TODO create is not update
    @Insert
    Completable createOrUpdate(Review review);

    /*Flowable<List<Review>> findAllByRating(boolean asc);

    Flowable<List<Review>> findAllByYear(boolean asc);

    Flowable<List<Review>> findAllByReviewDate(boolean asc);*/

}
