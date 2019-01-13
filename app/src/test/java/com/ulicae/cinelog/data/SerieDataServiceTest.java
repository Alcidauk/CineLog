package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dto.data.SerieDataDto;
import com.ulicae.cinelog.data.dto.data.TmdbSerieToSerieDataDtoBuilder;

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
    private TmdbSerieRepository tmdbSerieRepository;

    @Mock
    private TmdbSerieToSerieDataDtoBuilder tmdbSerieToSerieDataDtoBuilder;

    @Test
    public void createSerieData() {
        new SerieDataService(tmdbSerieRepository, tmdbSerieToSerieDataDtoBuilder).createSerieData(
                new SerieDataDto(24, 264564, "A movie", "2125", "an overview", 2015, "A release date")
        );

        verify(tmdbSerieRepository).createOrUpdate(
                new TmdbSerie(24L, 264564, "A movie", "2125", "an overview", 2015, "A release date")
        );
    }

    @Test
    public void createSerieData_nullId() {
        new SerieDataService(tmdbSerieRepository, tmdbSerieToSerieDataDtoBuilder).createSerieData(
                new SerieDataDto(null, 264564, "A movie", "2125", "an overview", 2015, "A release date")
        );

        verify(tmdbSerieRepository).createOrUpdate(
                new TmdbSerie(null, 264564, "A movie", "2125", "an overview", 2015, "A release date")
        );
    }

    @Test
    public void getAll() {
        final TmdbSerie tmdbSerie = mock(TmdbSerie.class);
        doReturn(new ArrayList<TmdbSerie>(){{add(tmdbSerie);}}).when(tmdbSerieRepository).findAll();

        final SerieDataDto serieDataDto = mock(SerieDataDto.class);
        doReturn(serieDataDto).when(tmdbSerieToSerieDataDtoBuilder).build(tmdbSerie);

        assertEquals(
                new ArrayList<SerieDataDto>() {{
                    add(serieDataDto);
                }},
                new SerieDataService(tmdbSerieRepository, tmdbSerieToSerieDataDtoBuilder).getAll()
        );
    }
}