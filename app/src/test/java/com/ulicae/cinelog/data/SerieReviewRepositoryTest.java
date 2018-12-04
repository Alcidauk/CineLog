package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.LocalKino;
import com.ulicae.cinelog.data.dao.LocalKinoDao;
import com.ulicae.cinelog.data.dao.Review;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.data.dao.SerieReviewDao;
import com.ulicae.cinelog.data.dao.TmdbKino;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
@RunWith(MockitoJUnitRunner.class)
public class SerieReviewRepositoryTest {

    @Mock
    private DaoSession daoSession;

    @Mock
    private SerieReviewDao serieReviewDao;

    @Mock
    private SerieReview serieReview;

    @Before
    public void setUp() {
        //noinspection ResultOfMethodCallIgnored
        doReturn(serieReviewDao).when(daoSession).getSerieReviewDao();
    }

    @Test
    public void findAllByRatingAsc() {
        Review badReview = mock(Review.class);
        Review betterReview = mock(Review.class);
        //noinspection ResultOfMethodCallIgnored
        doReturn(10.0f).when(badReview).getRating();
        //noinspection ResultOfMethodCallIgnored
        doReturn(30).when(badReview).getMaxRating();
        //noinspection ResultOfMethodCallIgnored
        doReturn(5.0f).when(betterReview).getRating();
        //noinspection ResultOfMethodCallIgnored
        doReturn(10).when(betterReview).getMaxRating();

        SerieReview badSerieReview = mock(SerieReview.class);
        doReturn(betterReview).when(serieReview).getReview();
        doReturn(badReview).when(badSerieReview).getReview();

        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(serieReviewDao).queryBuilder();

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Arrays.asList(serieReview, badSerieReview)).when(query).list();

        assertEquals(
                Arrays.asList(badSerieReview, serieReview),
                new SerieReviewRepository(daoSession).findAllByRating(true)
        );
    }

    @Test
    public void findAllByRatingDesc() {
        Review badReview = mock(Review.class);
        Review betterReview = mock(Review.class);
        //noinspection ResultOfMethodCallIgnored
        doReturn(10.0f).when(badReview).getRating();
        //noinspection ResultOfMethodCallIgnored
        doReturn(30).when(badReview).getMaxRating();
        //noinspection ResultOfMethodCallIgnored
        doReturn(5.0f).when(betterReview).getRating();
        //noinspection ResultOfMethodCallIgnored
        doReturn(10).when(betterReview).getMaxRating();

        SerieReview badSerieReview = mock(SerieReview.class);
        doReturn(betterReview).when(serieReview).getReview();
        doReturn(badReview).when(badSerieReview).getReview();

        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(serieReviewDao).queryBuilder();

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Arrays.asList(serieReview, badSerieReview)).when(query).list();

        assertEquals(
                Arrays.asList(serieReview, badSerieReview),
                new SerieReviewRepository(daoSession).findAllByRating(false)
        );
    }

    @Test
    public void findAllByTitleAsc() {
        Review aReview = mock(Review.class);
        Review anotherReview = mock(Review.class);
        //noinspection ResultOfMethodCallIgnored
        doReturn("Banane").when(aReview).getTitle();
        //noinspection ResultOfMethodCallIgnored
        doReturn("Pingouin").when(anotherReview).getTitle();

        SerieReview aSerieReview = mock(SerieReview.class);
        doReturn(anotherReview).when(serieReview).getReview();
        doReturn(aReview).when(aSerieReview).getReview();

        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(serieReviewDao).queryBuilder();

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Arrays.asList(serieReview, aSerieReview)).when(query).list();

        assertEquals(
                Arrays.asList(aSerieReview, serieReview),
                new SerieReviewRepository(daoSession).findAllByTitle(true)
        );
    }

    @Test
    public void findAllByTitleDesc() {
        Review aReview = mock(Review.class);
        Review anotherReview = mock(Review.class);
        //noinspection ResultOfMethodCallIgnored
        doReturn("Banane").when(aReview).getTitle();
        //noinspection ResultOfMethodCallIgnored
        doReturn("Pingouin").when(anotherReview).getTitle();

        SerieReview aSerieReview = mock(SerieReview.class);
        doReturn(anotherReview).when(serieReview).getReview();
        doReturn(aReview).when(aSerieReview).getReview();

        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(serieReviewDao).queryBuilder();

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Arrays.asList(serieReview, aSerieReview)).when(query).list();

        assertEquals(
                Arrays.asList(serieReview, aSerieReview),
                new SerieReviewRepository(daoSession).findAllByTitle(false)
        );
    }


    @Test
    public void findAll() {
        List<SerieReview> kinoList = new ArrayList<SerieReview>() {{
            add(serieReview);
        }};
        doReturn(kinoList).when(serieReviewDao).loadAll();

        assertEquals(
                kinoList,
                new SerieReviewRepository(daoSession).findAll()
        );
    }

    @Test
    public void find() {
        doReturn(serieReview).when(serieReviewDao).load(4L);

        assertEquals(
                serieReview,
                new SerieReviewRepository(daoSession).find(4L)
        );
    }

    @Test
    public void findByMovieId() {
        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(serieReviewDao).queryBuilder();
        doReturn(queryBuilder).when(queryBuilder).where(any(WhereCondition.class));
        doReturn(queryBuilder).when(queryBuilder).limit(1);

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Collections.singletonList(serieReview)).when(query).list();

        assertEquals(
                serieReview,
                new SerieReviewRepository(daoSession).findByMovieId(45)
        );

    }

    @Test
    public void createOrUpdate() {
        new SerieReviewRepository(daoSession).createOrUpdate(serieReview);

        verify(serieReviewDao).insertOrReplace(serieReview);
    }

    @Test
    public void delete() {
        // TODO delete the TmdbKino if necessary too.
        new SerieReviewRepository(daoSession).delete(5L);

        verify(serieReviewDao).deleteByKey(5L);
    }

}