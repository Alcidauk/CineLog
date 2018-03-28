package com.alcidauk.cinelog.db;

import com.alcidauk.cinelog.dao.DaoSession;
import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.dao.LocalKinoDao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LocalKinoRepositoryTest {

    @Mock
    private DaoSession daoSession;

    @Mock
    private LocalKinoDao localKinoDao;

    @Mock
    private LocalKino localKino;

    @Before
    public void setUp() throws Exception {
        doReturn(localKinoDao).when(daoSession).getLocalKinoDao();
    }

    @Test
    public void findAll() throws Exception {
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
    public void find() throws Exception {
        doReturn(localKino).when(localKinoDao).load(4L);

        assertEquals(
                localKino,
                new LocalKinoRepository(daoSession).find(4L)
        );
    }

    @Test
    public void create() throws Exception {
        new LocalKinoRepository(daoSession).create(localKino);

        verify(localKinoDao).insert(localKino);
    }
}