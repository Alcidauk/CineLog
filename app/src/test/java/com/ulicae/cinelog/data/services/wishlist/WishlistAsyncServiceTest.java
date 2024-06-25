package com.ulicae.cinelog.data.services.wishlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.ulicae.cinelog.data.TmdbSerieRepository;
import com.ulicae.cinelog.data.WishlistSerieRepository;
import com.ulicae.cinelog.data.dao.WishlistSerie;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.data.dto.data.WishlistSerieToSerieDataDtoBuilder;
import com.ulicae.cinelog.data.services.wishlist.room.WishlistAsyncService;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.dao.TmdbDao;
import com.ulicae.cinelog.room.dao.WishlistItemDao;
import com.ulicae.cinelog.room.dao.WishlistTmdbCrossRefDao;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Tmdb;
import com.ulicae.cinelog.room.entities.WishlistItem;
import com.ulicae.cinelog.room.entities.WishlistTmdbCrossRef;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.observers.TestObserver;

/**
 * CineLog Copyright 2019 Pierre Rognon
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
public class WishlistAsyncServiceTest {

    @Mock
    private AppDatabase db;

    @Mock
    private WishlistItemDao wishlistItemDao;

    @Mock
    private TmdbDao tmdbDao;

    @Mock
    private WishlistTmdbCrossRefDao wishlistTmdbCrossRefDao;

    @Mock
    private WishlistSerieRepository wishlistSerieRepository;

    @Mock
    private TmdbSerieRepository tmdbSerieRepository;

    @Mock
    private WishlistSerieToSerieDataDtoBuilder wishlistSerieToSerieDataDtoBuilder;

    @Before
    public void setUp() throws Exception {
        doReturn(wishlistItemDao).when(db).wishlistItemDao();
        doReturn(tmdbDao).when(db).tmdbDao();
        doReturn(wishlistTmdbCrossRefDao).when(db).wishlistTmdbCrossRefDao();
    }

    @Test
    public void createSerieData() {
        TestObserver daoInsertObserver = TestObserver.create();
        Completable insertCompletable = Completable.complete()
                .doOnSubscribe(d -> daoInsertObserver.onSubscribe(d));

        new WishlistAsyncService(db).createSerieData(
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

        given(wishlistItemDao.insert(
                new WishlistItem(
                        12L,
                        ItemEntityType.SERIE,
                        "A title"
                )
        )).willReturn(insertCompletable);

        TestObserver tmdbDaoInsertObserver = TestObserver.create();
        Completable tmdbDaoInsertCompletable = Completable.complete()
                .doOnSubscribe(d -> daoInsertObserver.onSubscribe(d));

        //pass completable to your mock
        given(tmdbDao.insert(
                new Tmdb(
                        264564L,
                        ItemEntityType.SERIE,
                        "2125",
                        "an overview",
                        2015,
                        "A release date"
                )
        )).willReturn(12L);

        // tmdbDaoInsertObserver.ass();

        // TODO

       verify(wishlistTmdbCrossRefDao).insert(
                new WishlistTmdbCrossRef(
                        12L,
                        264564L
                        )
        );

    }

    @Test
    public void createSerieData_noTmdb() {
        new SerieWishlistService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).createSerieData(
                new WishlistDataDto(24L, null, "A movie", "2125", "an overview", 2015, "A release date", WishlistItemType.SERIE)
        );

        WishlistSerie wishlistSerie = new WishlistSerie(24L, null, "A movie", null);
        verify(wishlistSerieRepository).createOrUpdate(wishlistSerie);
    }

    @Test
    public void getAll() {
        final WishlistSerie wishlistSerie = mock(WishlistSerie.class);
        doReturn(new ArrayList<WishlistSerie>(){{add(wishlistSerie);}}).when(wishlistSerieRepository).findAll();

        final WishlistDataDto wishlistDataDto = mock(WishlistDataDto.class);
        doReturn(wishlistDataDto).when(wishlistSerieToSerieDataDtoBuilder).build(wishlistSerie);

        assertEquals(
                new ArrayList<WishlistDataDto>() {{ add(wishlistDataDto); }},
                new SerieWishlistService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).getAll()
        );
    }

    @Test
    public void getByTmdbId() {
        final WishlistSerie wishlistSerie = mock(WishlistSerie.class);
        doReturn(wishlistSerie).when(wishlistSerieRepository).findByTmdbId(34455L);

        final WishlistDataDto wishlistDataDto = mock(WishlistDataDto.class);
        doReturn(wishlistDataDto).when(wishlistSerieToSerieDataDtoBuilder).build(wishlistSerie);

        assertEquals(
                wishlistDataDto,
                new SerieWishlistService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).getByTmdbId(34455)
        );
    }

    @Test
    public void getById() {
        final WishlistSerie wishlistSerie = mock(WishlistSerie.class);
        doReturn(wishlistSerie).when(wishlistSerieRepository).find(34455L);

        final WishlistDataDto wishlistDataDto = mock(WishlistDataDto.class);
        doReturn(wishlistDataDto).when(wishlistSerieToSerieDataDtoBuilder).build(wishlistSerie);

        assertEquals(
                wishlistDataDto,
                new SerieWishlistService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).getById(34455L)
        );
    }

    @Test
    public void getByTmdbId_noSerie() {
        doReturn(null).when(wishlistSerieRepository).findByTmdbId(34455L);
        assertNull( new SerieWishlistService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).getByTmdbId(34455));
    }

    @Test
    public void delete() {
        // TODO remove the tmdb id at the same time if not linked to another entity
        final WishlistDataDto wishlistDataDto = mock(WishlistDataDto.class);
        doReturn(345L).when(wishlistDataDto).getId();

        new SerieWishlistService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).delete(wishlistDataDto);

        verify(wishlistSerieRepository).delete(345L);
    }
}