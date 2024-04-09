package com.ulicae.cinelog.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ulicae.cinelog.room.converters.Converters;
import com.ulicae.cinelog.room.dao.ReviewDao;
import com.ulicae.cinelog.room.dao.ReviewTmdbCrossRefDao;
import com.ulicae.cinelog.room.dao.TmdbDao;
import com.ulicae.cinelog.room.entities.Review;
import com.ulicae.cinelog.room.entities.ReviewTmdbCrossRef;
import com.ulicae.cinelog.room.entities.Tmdb;

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
@Database(entities = {Review.class, ReviewTmdbCrossRef.class, Tmdb.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ReviewDao reviewDao();
    public abstract ReviewTmdbCrossRefDao reviewTmdbDao();
    public abstract TmdbDao tmdbDao();


}
