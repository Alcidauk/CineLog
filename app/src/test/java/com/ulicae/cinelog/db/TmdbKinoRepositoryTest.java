package com.ulicae.cinelog.db;

import com.ulicae.cinelog.dao.DaoSession;
import com.ulicae.cinelog.dao.TmdbKino;
import com.ulicae.cinelog.dao.TmdbKinoDao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TmdbKinoRepositoryTest {

    @Mock
    private DaoSession daoSession;

    @Mock
    private TmdbKinoDao tmdbKinoDao;

    @Mock
    private TmdbKino tmdbKino;

    @Test
    public void create() throws Exception {
        //noinspection ResultOfMethodCallIgnored
        doReturn(tmdbKinoDao).when(daoSession).getTmdbKinoDao();

        new TmdbKinoRepository(daoSession).createOrUpdate(tmdbKino);

        verify(tmdbKinoDao).insertOrReplace(tmdbKino);
    }

    @Test
    public void find() throws Exception {
        //noinspection ResultOfMethodCallIgnored
        doReturn(tmdbKinoDao).when(daoSession).getTmdbKinoDao();

        doReturn(tmdbKino).when(tmdbKinoDao).load(4L);

        assertEquals(
                tmdbKino,
                new TmdbKinoRepository(daoSession).find(4L)
        );
    }
}