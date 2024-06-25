package com.ulicae.cinelog.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ulicae.cinelog.room.converters.Converters;
import com.ulicae.cinelog.room.dao.ReviewDao;
import com.ulicae.cinelog.room.dao.ReviewTagCrossRefDao;
import com.ulicae.cinelog.room.dao.ReviewTmdbCrossRefDao;
import com.ulicae.cinelog.room.dao.SyncWishlistItemDao;
import com.ulicae.cinelog.room.dao.TagDao;
import com.ulicae.cinelog.room.dao.TmdbDao;
import com.ulicae.cinelog.room.dao.WishlistItemDao;
import com.ulicae.cinelog.room.dao.WishlistTmdbCrossRefDao;
import com.ulicae.cinelog.room.entities.Review;
import com.ulicae.cinelog.room.entities.ReviewTagCrossRef;
import com.ulicae.cinelog.room.entities.ReviewTmdbCrossRef;
import com.ulicae.cinelog.room.entities.Tag;
import com.ulicae.cinelog.room.entities.Tmdb;
import com.ulicae.cinelog.room.entities.WishlistItem;
import com.ulicae.cinelog.room.entities.WishlistTmdbCrossRef;

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
@Database(
        entities = {
                Review.class,
                ReviewTmdbCrossRef.class,
                Tmdb.class,
                WishlistItem.class,
                WishlistTmdbCrossRef.class,
                Tag.class,
                ReviewTagCrossRef.class
        },
        version = 3
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TmdbDao tmdbDao();

    public abstract ReviewDao reviewDao();
    public abstract ReviewTmdbCrossRefDao reviewTmdbDao();

    public abstract WishlistItemDao wishlistItemDao();
    public abstract WishlistTmdbCrossRefDao wishlistTmdbCrossRefDao();

    public abstract TagDao tagDao();

    public abstract ReviewTagCrossRefDao reviewTagCrossRefDao();

    public abstract SyncWishlistItemDao syncWishlistItemDao();


    /*
    TODO
    - migrate all db
    - avoid use of kinodto, and have a review dto and a tmdb dto
     */

}
