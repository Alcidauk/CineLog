package com.ulicae.cinelog.network;

import com.ulicae.cinelog.data.dto.SerieDto;
import com.uwetrottmann.tmdb2.entities.TvShow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class TmdbGetterServiceTest {

    @Mock
    private TmdbServiceWrapper tmdbServiceWrapper;

    @Mock
    private SerieBuilderFromMovie serieBuilderFromMovie;

    @Test
    public void getSerieDtoWithTmdbId() throws IOException {
        //noinspection unchecked
        Call<TvShow> call = mock(Call.class);
        doReturn(call).when(tmdbServiceWrapper).searchTvShowById(24);

        //noinspection unchecked
        Response<TvShow> response = mock(Response.class);
        doReturn(response).when(call).execute();

        TvShow tvShow = mock(TvShow.class);
        //noinspection ResultOfMethodCallIgnored
        doReturn(tvShow).when(response).body();

        SerieDto serieDto = mock(SerieDto.class);
        doReturn(serieDto).when(serieBuilderFromMovie).build(tvShow);

        assertEquals(
                serieDto,
                new TmdbGetterService(tmdbServiceWrapper, serieBuilderFromMovie).getSerieWithTmdbId(24)
        );
    }

}