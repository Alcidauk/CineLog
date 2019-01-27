package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.WishlistSerie;
import com.ulicae.cinelog.data.dao.WishlistSerieDao;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class WishlistSerieRepositoryTest {

    @Mock
    private DaoSession daoSession;

    @Mock
    private WishlistSerieDao wishlistSerieDao;

    @Mock
    private WishlistSerie wishlistSerie;

    @Before
    public void setUp() {
        //noinspection ResultOfMethodCallIgnored
        doReturn(wishlistSerieDao).when(daoSession).getWishlistSerieDao();
    }

    @Test
    public void findBySerieId() {
        QueryBuilder queryBuilder = mock(QueryBuilder.class);
        doReturn(queryBuilder).when(wishlistSerieDao).queryBuilder();
        doReturn(queryBuilder).when(queryBuilder).where(any(WhereCondition.class));
        doReturn(queryBuilder).when(queryBuilder).limit(1);

        Query query = mock(Query.class);
        doReturn(query).when(queryBuilder).build();

        doReturn(Collections.singletonList(wishlistSerie)).when(query).list();

        assertEquals(
                wishlistSerie,
                new WishlistSerieRepository(daoSession).findByTmdbId(45)
        );

    }

}