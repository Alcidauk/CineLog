package com.ulicae.cinelog.room.services;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.CinelogSchedulers;
import com.ulicae.cinelog.room.dao.WishlistItemDao;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Tmdb;
import com.ulicae.cinelog.room.entities.WishlistItem;
import com.ulicae.cinelog.room.services.WishlistAsyncService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;

/**
 * CineLog Copyright 2024 Pierre Rognon
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
public class WishlistAsyncServiceTest {

    @Mock
    private AppDatabase db;

    @Mock
    private WishlistItemDao wishlistItemDao;

    @Mock
    private Completable deleteCompletable;

    @Mock
    private CinelogSchedulers cinelogSchedulers;

    @Mock
    private Scheduler ioScheduler;

    @Mock
    private Scheduler mainScheduler;

    @Before
    public void setUp() {
        doReturn(wishlistItemDao).when(db).wishlistItemDao();
        doReturn(ioScheduler).when(cinelogSchedulers).io();
        doReturn(mainScheduler).when(cinelogSchedulers).androidMainThread();
    }

    @Test
    public void createSerieData() {
        new WishlistAsyncService(db).createSerieData(
                new WishlistDataDto(
                        12L,
                        264564L,
                        "A title",
                        "a poster path",
                        "an overview",
                        2013,
                        "a release date",
                        WishlistItemType.SERIE
                )
        );

        verify(wishlistItemDao).insert(
                new WishlistItem(
                        12L,
                        ItemEntityType.SERIE,
                        "A title",
                        new Tmdb(
                                264564L,
                                "a poster path",
                                "an overview",
                                2013,
                                "a release date"
                        )
                )
        );
    }


    @Test
    public void delete() {
        WishlistAsyncService wishlistAsyncService = new WishlistAsyncService(
                wishlistItemDao,
                null,
                null,
                cinelogSchedulers

        );

        doReturn(deleteCompletable).when(wishlistItemDao).delete(
                new WishlistItem(
                        12L,
                        null,
                        null,
                        null
                )
        );

        doReturn(deleteCompletable).when(deleteCompletable).subscribeOn(ioScheduler);
        doReturn(deleteCompletable).when(deleteCompletable).observeOn(mainScheduler);

        wishlistAsyncService.delete(
                new WishlistDataDto(
                        12L,
                        264564L,
                        "A tv show",
                        "2125",
                        "an overview",
                        2015,
                        "A release date",
                        WishlistItemType.SERIE
                )
        );

        verify(wishlistItemDao).delete(
                new WishlistItem(
                        12L,
                        null,
                        null,
                        null
                )
        );

        verify(deleteCompletable).subscribe();
    }
}