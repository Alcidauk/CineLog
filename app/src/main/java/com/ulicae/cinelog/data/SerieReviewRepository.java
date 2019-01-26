package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.LocalKino;
import com.ulicae.cinelog.data.dao.Review;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.data.dao.SerieReviewDao;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
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
public class SerieReviewRepository extends CrudRepository<SerieReviewDao, SerieReview> {

    public SerieReviewRepository(DaoSession daoSession) {
        super(daoSession.getSerieReviewDao());
    }

    public SerieReview findByMovieId(long movieId) {
        Query<SerieReview> serieReviewQuery = dao.queryBuilder()
                .where(SerieReviewDao.Properties.Tmdb_id.eq(movieId))
                .limit(1)
                .build();
        List<SerieReview> serieReviews = serieReviewQuery.list();
        return serieReviews != null && serieReviews.size() > 0 ? serieReviews.get(0) : null;
    }

    public List<SerieReview> findAllByRating(boolean asc) {
        QueryBuilder<SerieReview> localKinoQueryBuilder = dao.queryBuilder();

        List<SerieReview> list = localKinoQueryBuilder.build().list();

        Collections.sort(list, new RatingComparator());

        if(asc){
            return list;
        }

        Collections.reverse(list);
        return list;
    }

    public List<SerieReview> findAllByTitle(boolean asc) {
        QueryBuilder<SerieReview> serieReviewQueryBuilder = dao.queryBuilder();

        List<SerieReview> list = serieReviewQueryBuilder.build().list();

        Collections.sort(list, new Comparator<SerieReview>() {
            @Override
            public int compare(SerieReview o1, SerieReview o2) {
                Review review1 = o1.getReview();
                Review review2 = o2.getReview();

                if (review1 == null || review1.getTitle() == null) {
                    return -1;
                } else if (review2 == null || review2.getTitle() == null) {
                    return 1;
                } else {
                    // TODO take care of locale
                    return Collator.getInstance().compare(review1.getTitle(), review2.getTitle());
                }
            }
        });

        if(!asc){
            Collections.reverse(list);
        }

        return list;
    }
}
