package com.ulicae.cinelog.data;

import android.content.Context;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.services.reviews.KinoService;
import com.ulicae.cinelog.data.services.reviews.SerieService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ServiceFactoryTest {

    @Mock
    private DaoSession daoSession;

    @Mock
    private Context context;

    @Test
    public void kinoService() {
        assertTrue(new ServiceFactory(context).create("kino", daoSession) instanceof KinoService);
    }

    @Test
    public void serieService() {
        assertTrue(new ServiceFactory(context).create("serie", daoSession) instanceof SerieService);
    }

    @Test(expected = NullPointerException.class)
    public void unexistingService() {
        new ServiceFactory(context).create("banane", daoSession);
    }
}