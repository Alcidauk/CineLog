package com.ulicae.cinelog.network.task.sync;

import com.ulicae.cinelog.data.SerieService;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.network.SerieBuilderFromMovie;
import com.ulicae.cinelog.network.TmdbGetterService;
import com.ulicae.cinelog.network.TmdbServiceWrapper;
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
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SerieSyncNetworkTaskTest {

    @Mock
    private SerieBuilderFromMovie serieBuilderFromMovie;

    @Mock
    private SerieService serieService;

    @Test
    public void doInBackground() throws IOException {
        //noinspection unchecked
        Call<TvShow> call = mock(Call.class);

        //noinspection unchecked
        Response<TvShow> response = mock(Response.class);
        doReturn(response).when(call).execute();

        TvShow tvShow = mock(TvShow.class);
        //noinspection ResultOfMethodCallIgnored
        doReturn(tvShow).when(response).body();

        SerieDto serieDto = mock(SerieDto.class);
        doReturn(serieDto).when(serieBuilderFromMovie).build(tvShow);

        SerieReview serieReview = mock(SerieReview.class);

        //noinspection unchecked
        assertEquals(
                serieDto,
                new SerieSyncNetworkTask(serieBuilderFromMovie, serieReview, serieService).doInBackground(call)
        );
    }

    @Test
    public void onPostExecute() {
        SerieDto serieDto = mock(SerieDto.class);
        SerieReview serieReview = mock(SerieReview.class);

        new SerieSyncNetworkTask(serieBuilderFromMovie, serieReview, serieService).onPostExecute(serieDto);

        verify(serieService).updateTmdbInfo(serieDto, serieReview);

    }

}