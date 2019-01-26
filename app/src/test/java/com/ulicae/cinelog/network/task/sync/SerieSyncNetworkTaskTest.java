package com.ulicae.cinelog.network.task.sync;

import com.ulicae.cinelog.data.services.reviews.SerieService;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.network.SerieBuilderFromMovie;
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

/**
 * CineLog Copyright 2018 Pierre Rognon
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