package com.alcidauk.cinelog.db;

import com.alcidauk.cinelog.dao.DaoSession;
import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.dao.LocalKinoDao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class LocalKinoRepositoryTest {

    @Mock
    private DaoSession daoSession;

    @Mock
    private LocalKinoDao localKinoDao;

    @Mock
    private LocalKino localKino;

    @Test
    public void findAll() throws Exception {
        doReturn(localKinoDao).when(daoSession).getLocalKinoDao();

        List<LocalKino> kinoList = new ArrayList<LocalKino>() {{
            add(localKino);
        }};
        doReturn(kinoList).when(localKinoDao).loadAll();

        assertEquals(
                kinoList,
                new LocalKinoRepository(daoSession).findAll()
        );
    }
}