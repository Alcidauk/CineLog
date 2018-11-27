package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.DaoSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ServiceFactoryTest {

    @Mock
    private DaoSession daoSession;

    @Test
    public void kinoService() {
        assertTrue(new ServiceFactory().create("kino", daoSession) instanceof KinoService);
    }

    @Test
    public void serieService() {
        assertTrue(new ServiceFactory().create("serie", daoSession) instanceof SerieService);
    }

    @Test(expected = NullPointerException.class)
    public void unexistingService() {
        new ServiceFactory().create("banane", daoSession);
    }
}