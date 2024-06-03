package com.ulicae.cinelog.data;

import android.content.Context;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.services.reviews.KinoService;
import com.ulicae.cinelog.data.services.reviews.SerieService;
import com.ulicae.cinelog.room.AppDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ServiceFactoryTest {

    @Mock
    private DaoSession daoSession;

    @Mock
    private MainActivity context;

    @Mock
    private AppDatabase db;

    @Mock
    private KinoApplication app;

    @Before
    public void setUp() throws Exception {
        Mockito.doReturn(app).when(context).getApplication();
        Mockito.doReturn(db).when(app).getDb();
    }

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