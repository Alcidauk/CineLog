package com.ulicae.cinelog.room.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Review;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

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
public interface ReviewAsyncDao extends AsyncRoomDao<Review> {


    @Transaction
    @Query("SELECT * FROM review")
    Flowable<List<Review>> findAll();

    @Transaction
    @Query("SELECT * FROM review WHERE item_entity_type = :itemEntitytype")
    Flowable<List<Review>> findAll(ItemEntityType itemEntitytype);

    @Transaction
    @Query("SELECT * FROM review WHERE item_entity_type = :itemEntitytype ORDER BY rating ASC")
    Flowable<List<Review>> findAllByRatingAsc(ItemEntityType itemEntitytype);
    @Transaction
    @Query("SELECT * FROM review WHERE item_entity_type = :itemEntitytype ORDER BY rating DESC")
    Flowable<List<Review>> findAllByRatingDesc(ItemEntityType itemEntitytype);

    @Transaction
    @Query("SELECT * FROM review WHERE item_entity_type = :itemEntitytype ORDER BY review_date ASC")
    Flowable<List<Review>> findAllByReviewDateAsc(ItemEntityType itemEntitytype);
    @Transaction
    @Query("SELECT * FROM review WHERE item_entity_type = :itemEntitytype ORDER BY review_date DESC")
    Flowable<List<Review>> findAllByReviewDateDesc(ItemEntityType itemEntitytype);

    @Transaction
    @Query("SELECT * FROM review WHERE item_entity_type = :itemEntitytype ORDER BY release_date ASC")
    Flowable<List<Review>> findAllByYearAsc(ItemEntityType itemEntitytype);
    @Transaction
    @Query("SELECT * FROM review WHERE item_entity_type = :itemEntitytype ORDER BY release_date DESC")
    Flowable<List<Review>> findAllByYearDesc(ItemEntityType itemEntitytype);

    @Transaction
    @Query("SELECT * FROM review WHERE item_entity_type = :itemEntitytype ORDER BY title ASC")
    Flowable<List<Review>> findAllByTitleAsc(ItemEntityType itemEntitytype);
    @Transaction
    @Query("SELECT * FROM review WHERE item_entity_type = :itemEntitytype ORDER BY title DESC")
    Flowable<List<Review>> findAllByTitleDesc(ItemEntityType itemEntitytype);

    @Transaction
    @Query("SELECT * FROM review WHERE id = :id")
    Flowable<Review> find(long id);

    @Transaction
    @Query("SELECT * FROM review WHERE tmdb_id = :tmdb_id")
    Flowable<Review> findByMovieId(Long tmdb_id);


    @Transaction
    @Query("SELECT * FROM review WHERE tmdb_id = :tmdb_id")
    Single<Review> findByMovieIdSingle(Long tmdb_id);
}
