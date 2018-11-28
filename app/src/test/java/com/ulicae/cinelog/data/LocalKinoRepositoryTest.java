package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.LocalKino;
import com.ulicae.cinelog.data.dao.LocalKinoDao;
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
import static org.mockito.ArgumentMatchers.floatThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * CineLog Copyright 2018 Pierre Rognon
 *
 *
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class LocalKinoRepositoryTest {

    @Mock
    private DaoSession daoSession;

    @Mock
    private LocalKinoDao localKinoDao;

    @Mock
    private LocalKino localKino;

    @Before
    public void setUp() {
        doReturn(localKinoDao).when(daoSession).getLocalKinoDao();
    }

    @Test
    public void findAllByRatingAsc() {
        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(localKinoDao).queryBuilder();
        doReturn(queryBuilder).when(queryBuilder).orderAsc(LocalKinoDao.Properties.Rating);

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Arrays.asList(localKino)).when(query).list();

        assertEquals(
                Arrays.asList(localKino),
                new LocalKinoRepository(daoSession).findAllByRating(true)
        );
    }

    @Test
    public void findAllByRatingDesc() {
        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(localKinoDao).queryBuilder();
        doReturn(queryBuilder).when(queryBuilder).orderDesc(LocalKinoDao.Properties.Rating);

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Arrays.asList(localKino)).when(query).list();

        assertEquals(
                Arrays.asList(localKino),
                new LocalKinoRepository(daoSession).findAllByRating(false)
        );
    }


    @Test
    public void findAllByReviewDateAsc() {
        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(localKinoDao).queryBuilder();
        doReturn(queryBuilder).when(queryBuilder).orderAsc(LocalKinoDao.Properties.Review_date);

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Collections.singletonList(localKino)).when(query).list();

        assertEquals(
                Collections.singletonList(localKino),
                new LocalKinoRepository(daoSession).findAllByReviewDate(true)
        );
    }

    @Test
    public void findAllByReviewDateDesc() {
        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(localKinoDao).queryBuilder();
        doReturn(queryBuilder).when(queryBuilder).orderDesc(LocalKinoDao.Properties.Review_date);

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Collections.singletonList(localKino)).when(query).list();

        assertEquals(
                Collections.singletonList(localKino),
                new LocalKinoRepository(daoSession).findAllByReviewDate(false)
        );
    }

    @Test
    public void findAllByYearAsc() {
        TmdbKino oldTmdbKino = mock(TmdbKino.class);
        TmdbKino recentTmdbKino = mock(TmdbKino.class);
        //noinspection ResultOfMethodCallIgnored
        doReturn(1987).when(oldTmdbKino).getYear();
        //noinspection ResultOfMethodCallIgnored
        doReturn(2012).when(recentTmdbKino).getYear();

        LocalKino oldLocalKino = mock(LocalKino.class);
        doReturn(recentTmdbKino).when(localKino).getKino();
        doReturn(oldTmdbKino).when(oldLocalKino).getKino();

        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(localKinoDao).queryBuilder();

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Arrays.asList(localKino, oldLocalKino)).when(query).list();

        assertEquals(
                Arrays.asList(oldLocalKino, localKino),
                new LocalKinoRepository(daoSession).findAllByYear(true)
        );
    }

    @Test
    public void findAllByYearDesc() {
        TmdbKino oldTmdbKino = mock(TmdbKino.class);
        TmdbKino recentTmdbKino = mock(TmdbKino.class);
        //noinspection ResultOfMethodCallIgnored
        doReturn(1987).when(oldTmdbKino).getYear();
        //noinspection ResultOfMethodCallIgnored
        doReturn(2012).when(recentTmdbKino).getYear();

        LocalKino oldLocalKino = mock(LocalKino.class);
        doReturn(recentTmdbKino).when(localKino).getKino();
        doReturn(oldTmdbKino).when(oldLocalKino).getKino();

        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(localKinoDao).queryBuilder();

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Arrays.asList(localKino, oldLocalKino)).when(query).list();

        assertEquals(
                Arrays.asList(localKino, oldLocalKino),
                new LocalKinoRepository(daoSession).findAllByYear(false)
        );
    }

    @Test
    public void findAllByTitleAsc() {
        LocalKino anotherKino = mock(LocalKino.class);
        doReturn("Banane").when(localKino).getTitle();
        doReturn("Poney").when(anotherKino).getTitle();

        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(localKinoDao).queryBuilder();

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Arrays.asList(localKino, anotherKino)).when(query).list();

        assertEquals(
                Arrays.asList(localKino, anotherKino),
                new LocalKinoRepository(daoSession).findAllByTitle(true)
        );
    }

    @Test
    public void findAllByTitleDesc() {
        LocalKino anotherKino = mock(LocalKino.class);
        doReturn("Banane").when(localKino).getTitle();
        doReturn("Poney").when(anotherKino).getTitle();

        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(localKinoDao).queryBuilder();

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Arrays.asList(localKino, anotherKino)).when(query).list();

        assertEquals(
                Arrays.asList(anotherKino, localKino),
                new LocalKinoRepository(daoSession).findAllByTitle(false)
        );
    }

    @Test
    public void findAllByTitleAscNullO1() {
        LocalKino anotherKino = mock(LocalKino.class);
        doReturn(null).when(localKino).getTitle();
        doReturn("Poney").when(anotherKino).getTitle();

        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(localKinoDao).queryBuilder();

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Arrays.asList(localKino, anotherKino)).when(query).list();

        assertEquals(
                Arrays.asList(localKino, anotherKino),
                new LocalKinoRepository(daoSession).findAllByTitle(true)
        );
    }

    @Test
    public void findAllByTitleAscNullO2() {
        LocalKino anotherKino = mock(LocalKino.class);
        doReturn(null).when(anotherKino).getTitle();

        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(localKinoDao).queryBuilder();

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Arrays.asList(localKino, anotherKino)).when(query).list();

        assertEquals(
                Arrays.asList(anotherKino, localKino),
                new LocalKinoRepository(daoSession).findAllByTitle(true)
        );
    }

    @Test
    public void findAllByTitleDescNullO1() {
        LocalKino anotherKino = mock(LocalKino.class);
        doReturn(null).when(localKino).getTitle();
        doReturn("Poney").when(anotherKino).getTitle();

        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(localKinoDao).queryBuilder();

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Arrays.asList(localKino, anotherKino)).when(query).list();

        assertEquals(
                Arrays.asList(anotherKino, localKino),
                new LocalKinoRepository(daoSession).findAllByTitle(false)
        );
    }

    @Test
    public void findAllByTitleDescNullO2() {
        LocalKino anotherKino = mock(LocalKino.class);
        doReturn(null).when(anotherKino).getTitle();

        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(localKinoDao).queryBuilder();

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Arrays.asList(localKino, anotherKino)).when(query).list();

        assertEquals(
                Arrays.asList(localKino, anotherKino),
                new LocalKinoRepository(daoSession).findAllByTitle(false)
        );
    }

    @Test
    public void findAll() {
        List<LocalKino> kinoList = new ArrayList<LocalKino>() {{
            add(localKino);
        }};
        doReturn(kinoList).when(localKinoDao).loadAll();

        assertEquals(
                kinoList,
                new LocalKinoRepository(daoSession).findAll()
        );
    }

    @Test
    public void find() {
        doReturn(localKino).when(localKinoDao).load(4L);

        assertEquals(
                localKino,
                new LocalKinoRepository(daoSession).find(4L)
        );
    }

    @Test
    public void findByMovieId() {
        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(localKinoDao).queryBuilder();
        doReturn(queryBuilder).when(queryBuilder).where(any(WhereCondition.class));//LocalKinoDao.Properties.Tmdb_id.eq(45));
        doReturn(queryBuilder).when(queryBuilder).limit(1);

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Arrays.asList(localKino)).when(query).list();

        assertEquals(
                localKino,
                new LocalKinoRepository(daoSession).findByMovieId(45)
        );

    }

    @Test
    public void createOrUpdate() {
        new LocalKinoRepository(daoSession).createOrUpdate(localKino);

        verify(localKinoDao).insertOrReplace(localKino);
    }

    @Test
    public void delete() {
        // TODO delete the TmdbKino if necessary too.
        new LocalKinoRepository(daoSession).delete(5L);

        verify(localKinoDao).deleteByKey(5L);
    }
}