package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.WishlistMovie;
import com.ulicae.cinelog.data.dao.WishlistMovieDao;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

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
public class WishlistMovieRepositoryTest {

    @Mock
    private DaoSession daoSession;

    @Mock
    private WishlistMovieDao wishlistMovieDao;

    @Mock
    private WishlistMovie wishlistMovie;

    @Before
    public void setUp() {
        //noinspection ResultOfMethodCallIgnored
        doReturn(wishlistMovieDao).when(daoSession).getWishlistMovieDao();
    }

    @Test
    public void findByMovieId() {
        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(wishlistMovieDao).queryBuilder();
        doReturn(queryBuilder).when(queryBuilder).where(any(WhereCondition.class));
        doReturn(queryBuilder).when(queryBuilder).limit(1);

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Collections.singletonList(wishlistMovie)).when(query).list();

        assertEquals(
                wishlistMovie,
                new WishlistMovieRepository(daoSession).findByTmdbId(45)
        );

    }

}