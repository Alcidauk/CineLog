package com.alcidauk.cinelog.db;

import com.alcidauk.cinelog.dao.DaoSession;
import com.alcidauk.cinelog.dao.TmdbKino;
import com.alcidauk.cinelog.dao.TmdbKinoDao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
        doReturn(tmdbKinoDao).when(daoSession).getTmdbKinoDao();

        new TmdbKinoRepository(daoSession).createOrUpdate(tmdbKino);

        verify(tmdbKinoDao).insertOrReplace(tmdbKino);
    }
}