package com.ulicae.cinelog.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ulicae.cinelog.room.entities.Review;
import com.ulicae.cinelog.room.entities.ReviewTmdbCrossRef;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
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
public interface ReviewTmdbCrossRefDao extends RoomDao<ReviewTmdbCrossRef> {

    /*@Delete
    void delete(Long kinoId);*/

    @Delete
    void delete(ReviewTmdbCrossRef reviewTmdbCrossRef);

    @Query("SELECT * FROM reviewtmdbcrossref")
    Flowable<List<ReviewTmdbCrossRef>> findAll();

    @Query("SELECT * FROM reviewtmdbcrossref WHERE review_id = :reviewId")
    Flowable<List<ReviewTmdbCrossRef>> findForReview(long reviewId);

    // TODO comment faire avec une crossref
    /*@Query("SELECT * FROM review WHERE tmdb_id = :tmdb_id")
    Flowable<Review> findByMovieId(Long tmdb_id);*/


    // TODO create is not update
    @Insert
    Completable createOrUpdate(ReviewTmdbCrossRef reviewTmdbCrossRef);

    /*Flowable<List<Review>> findAllByRating(boolean asc);

    Flowable<List<Review>> findAllByYear(boolean asc);

    Flowable<List<Review>> findAllByReviewDate(boolean asc);*/

}
