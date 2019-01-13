package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dao.WishlistSerie;
import com.ulicae.cinelog.data.dto.data.SerieDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistSerieToSerieDataDtoBuilder;

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
                new SerieDataDto(24L, 264564, "A movie", "2125", "an overview", 2015, "A release date")
        );

        TmdbSerie tmdbSerie = new TmdbSerie(264564L, "2125", "an overview", 2015, "A release date");
        WishlistSerie wishlistSerie = new WishlistSerie(24L, tmdbSerie, "A movie", null);

        verify(tmdbSerieRepository).createOrUpdate(tmdbSerie);
        verify(wishlistSerieRepository).createOrUpdate(wishlistSerie);
    }

    @Test
    public void createSerieData_noTmdb() {
        new SerieDataService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).createSerieData(
                new SerieDataDto(24L, null, "A movie", "2125", "an overview", 2015, "A release date")
        );

        WishlistSerie wishlistSerie = new WishlistSerie(24L, null, "A movie", null);
        verify(wishlistSerieRepository).createOrUpdate(wishlistSerie);
    }

    @Test
    public void getAll() {
        final WishlistSerie wishlistSerie = mock(WishlistSerie.class);
        doReturn(new ArrayList<WishlistSerie>(){{add(wishlistSerie);}}).when(wishlistSerieRepository).findAll();

        final SerieDataDto serieDataDto = mock(SerieDataDto.class);
        doReturn(serieDataDto).when(wishlistSerieToSerieDataDtoBuilder).build(wishlistSerie);

        assertEquals(
                new ArrayList<SerieDataDto>() {{ add(serieDataDto); }},
                new SerieDataService(wishlistSerieRepository, tmdbSerieRepository, wishlistSerieToSerieDataDtoBuilder).getAll()
        );
    }
}