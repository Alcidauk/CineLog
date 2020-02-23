package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.data.dao.SerieReviewDao;
import com.ulicae.cinelog.data.dao.WishlistMovie;
import com.ulicae.cinelog.data.dao.WishlistMovieDao;
import com.ulicae.cinelog.data.dao.WishlistSerie;
import com.ulicae.cinelog.data.dao.WishlistSerieDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * CineLog Copyright 2018 Pierre Rognon
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
public class WishlistMovieRepository extends CrudRepository<WishlistMovieDao, WishlistMovie> {

    public WishlistMovieRepository(DaoSession daoSession) {
        super(daoSession.getWishlistMovieDao());
    }

    public WishlistMovie findByTmdbId(long movieId) {
        Query<WishlistMovie> wishlistMovieQuery = dao.queryBuilder()
                .where(WishlistMovieDao.Properties.Tmdb_id.eq(movieId))
                .limit(1)
                .build();
        List<WishlistMovie> wishlistMovies = wishlistMovieQuery.list();
        return wishlistMovies != null && wishlistMovies.size() > 0 ? wishlistMovies.get(0) : null;
    }

}
