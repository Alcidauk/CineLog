package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.TmdbKinoRepository;
import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.TmdbKino;
import com.ulicae.cinelog.data.dao.TmdbKinoDao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
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