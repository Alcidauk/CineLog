package com.ulicae.cinelog.data.dto.data;

import com.ulicae.cinelog.data.dao.TmdbSerie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TmdbSerieToSerieDataDtoBuilderTest {

    @Mock
    private TmdbSerie tmdbSerie;

    @Test
    public void build() {
        Mockito.doReturn(456L).when(tmdbSerie).getSerie_id();
        Mockito.doReturn(4564564).when(tmdbSerie).getTmdb_id();
        Mockito.doReturn("Versailles").when(tmdbSerie).getTitle();
        Mockito.doReturn(2015).when(tmdbSerie).getYear();
        Mockito.doReturn("An overview").when(tmdbSerie).getOverview();
        Mockito.doReturn("/poster/path").when(tmdbSerie).getPoster_path();
        Mockito.doReturn("a releaseDate").when(tmdbSerie).getRelease_date();

        assertEquals(
                new SerieDataDto(
                        456,
                        4564564,
                        "Versailles",
                        "/poster/path",
                        "An overview",
                        2015,
                        "a releaseDate"
                ),
                new TmdbSerieToSerieDataDtoBuilder().build(tmdbSerie)
        );
    }
}