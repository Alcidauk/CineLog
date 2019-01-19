package com.ulicae.cinelog.data.services;

import com.ulicae.cinelog.data.TmdbSerieRepository;
import com.ulicae.cinelog.data.WishlistSerieRepository;
import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dao.WishlistSerie;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.data.dto.data.WishlistSerieToSerieDataDtoBuilder;
import com.ulicae.cinelog.data.services.SerieDataService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SerieDataServiceTest {

    @Mock
    private WishlistSerieRepository wishlistSerieRepository;

    @Mock
    private TmdbSerieRepository tmdbSerieRepository;

    @Mock
    private WishlistSerieToSerieDataDtoBuilder wishlistSerieToSerieDataDtoBuilder;

    @Test
    public void createSerieData() {
        new SerieDataService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).createSerieData(
                new WishlistDataDto(24L, 264564, "A movie", "2125", "an overview", 2015, "A release date", WishlistItemType.SERIE)
        );

        TmdbSerie tmdbSerie = new TmdbSerie(264564L, "2125", "an overview", 2015, "A release date");
        WishlistSerie wishlistSerie = new WishlistSerie(24L, tmdbSerie, "A movie", null);

        verify(tmdbSerieRepository).createOrUpdate(tmdbSerie);
        verify(wishlistSerieRepository).createOrUpdate(wishlistSerie);
    }

    @Test
    public void createSerieData_noTmdb() {
        new SerieDataService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).createSerieData(
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
                new SerieDataService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).getAll()
        );
    }

    @Test
    public void delete() {
        // TODO remove the tmdb id at the same time if not linked to another entity
        final WishlistDataDto wishlistDataDto = mock(WishlistDataDto.class);
        doReturn(345L).when(wishlistDataDto).getId();

        new SerieDataService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).delete(wishlistDataDto);

        verify(wishlistSerieRepository).delete(345L);
    }
}