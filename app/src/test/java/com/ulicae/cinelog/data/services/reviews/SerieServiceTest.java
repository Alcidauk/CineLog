package com.ulicae.cinelog.data.services.reviews;

import com.ulicae.cinelog.data.ReviewRepository;
import com.ulicae.cinelog.data.SerieReviewRepository;
import com.ulicae.cinelog.data.TmdbSerieRepository;
import com.ulicae.cinelog.data.dao.Review;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.SerieKinoDtoBuilder;
import com.ulicae.cinelog.network.TmdbGetterService;
import com.ulicae.cinelog.utils.SerieDtoToDbBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
public class SerieServiceTest {

    @Mock
    private SerieReviewRepository serieReviewRepository;

    @Mock
    private TmdbSerieRepository tmdbSerieRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private SerieKinoDtoBuilder serieKinoDtoBuilder;

    @Mock
    private SerieDto serieDto;

    @Mock
    private SerieDtoToDbBuilder toDbBuilder;

    @Mock
    private TmdbGetterService tmdbGetterService;

    @Test
    public void getReview() {
        SerieReview serieReview = mock(SerieReview.class);

        doReturn(serieReview).when(serieReviewRepository).find(4L);
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                serieDto,
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).getReview(4L)
        );
    }

    @Test
    public void getByTmdbMovieId() {
        SerieReview serieReview = mock(SerieReview.class);

        doReturn(serieReview).when(serieReviewRepository).findByMovieId(4L);
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                serieDto,
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).getWithTmdbId(4L)
        );
    }

    @Test
    public void getAll() {
        final SerieReview serieReview = mock(SerieReview.class);

        doReturn(new ArrayList<SerieReview>() {{
            add(serieReview);
        }}).when(serieReviewRepository).findAll();
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                new ArrayList<SerieDto>() {{
                    add(serieDto);
                }},
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).getAll()
        );
    }

    @Test
    public void getByRatingAsc() {
        final SerieReview serieReview = mock(SerieReview.class);

        doReturn(new ArrayList<SerieReview>() {{
            add(serieReview);
        }}).when(serieReviewRepository).findAllByRating(true);
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                new ArrayList<SerieDto>() {{
                    add(serieDto);
                }},
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).getAllByRating(true)
        );
    }

    @Test
    public void getByRatingDesc() {
        final SerieReview serieReview = mock(SerieReview.class);

        doReturn(new ArrayList<SerieReview>() {{
            add(serieReview);
        }}).when(serieReviewRepository).findAllByRating(false);
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                new ArrayList<SerieDto>() {{
                    add(serieDto);
                }},
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).getAllByRating(false)
        );
    }

    @Test
    public void getAllByTitleAsc() {
        SerieReview serieReview = mock(SerieReview.class);
        SerieReview anotherSerieReview = mock(SerieReview.class);

        doReturn(Arrays.asList(serieReview, anotherSerieReview)).when(serieReviewRepository).findAllByTitle(true);

        SerieDto anotherSerieDto = mock(SerieDto.class);
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);
        doReturn(anotherSerieDto).when(serieKinoDtoBuilder).build(anotherSerieReview);

        assertEquals(
                Arrays.asList(serieDto, anotherSerieDto),
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).getAllByTitle(true)
        );
    }

    @Test
    public void getAllByTitleDesc() {
        SerieReview serieReview = mock(SerieReview.class);
        SerieReview anotherSerieReview = mock(SerieReview.class);

        doReturn(Arrays.asList(serieReview, anotherSerieReview)).when(serieReviewRepository).findAllByTitle(false);

        SerieDto anotherSerieDto = mock(SerieDto.class);
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);
        doReturn(anotherSerieDto).when(serieKinoDtoBuilder).build(anotherSerieReview);

        assertEquals(
                Arrays.asList(serieDto, anotherSerieDto),
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).getAllByTitle(false)
        );
    }

    @Test
    public void delete() {
        //noinspection ResultOfMethodCallIgnored
        doReturn(545L).when(serieDto).getKinoId();

        new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).delete(serieDto);

        verify(serieReviewRepository).delete(545L);
    }

    @Test
    public void createOrUpdateKino() {
        TmdbSerie tmdbKino = mock(TmdbSerie.class);
        doReturn(tmdbKino).when(toDbBuilder).buildTmdbSerie(serieDto);

        Review review = mock(Review.class);
        doReturn(review).when(toDbBuilder).buildReview(serieDto);

        //noinspection ResultOfMethodCallIgnored
        doReturn(12L).when(serieDto).getKinoId();

        SerieReview serieReview = new SerieReview(12L, tmdbKino, review);

        SerieDto createdKino = mock(SerieDto.class);
        doReturn(createdKino).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                createdKino,
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).createOrUpdate(serieDto)
        );

        verify(serieReviewRepository).createOrUpdate(serieReview);
        verify(reviewRepository).createOrUpdate(review);
        verify(tmdbSerieRepository).createOrUpdate(tmdbKino);
    }

    @Test
    public void createOrUpdateSerieNullTmdbId() {
        Review review = mock(Review.class);
        doReturn(review).when(toDbBuilder).buildReview(serieDto);

        //noinspection ResultOfMethodCallIgnored
        doReturn(45L).when(serieDto).getKinoId();

        SerieReview reviewToCreate = new SerieReview(45L, null, review);

        SerieDto createdKino = mock(SerieDto.class);
        doReturn(createdKino).when(serieKinoDtoBuilder).build(reviewToCreate);

        assertEquals(
                createdKino,
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).createOrUpdate(serieDto)
        );

        verify(serieReviewRepository).createOrUpdate(reviewToCreate);
        verify(reviewRepository).createOrUpdate(review);
        verify(tmdbSerieRepository, never()).createOrUpdate(any(TmdbSerie.class));
    }

    @Test
    public void createOrUpdateKinosNoExistingId() {
        TmdbSerie tmdbKino = mock(TmdbSerie.class);
        doReturn(tmdbKino).when(toDbBuilder).buildTmdbSerie(serieDto);

        Review review = mock(Review.class);
        doReturn(review).when(toDbBuilder).buildReview(serieDto);

        //noinspection ResultOfMethodCallIgnored
        doReturn(12L).when(serieDto).getKinoId();

        SerieReview serieReview = new SerieReview(12L, tmdbKino, review);

        SerieDto createdKino = mock(SerieDto.class);
        doReturn(createdKino).when(serieKinoDtoBuilder).build(serieReview);

        new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null)
                .createOrUpdateFromImport(new ArrayList<SerieDto>() {{
                    add(serieDto);
                }});

        verify(serieDto, never()).setKinoId(12L);

        verify(serieReviewRepository).createOrUpdate(serieReview);
        verify(tmdbSerieRepository).createOrUpdate(tmdbKino);
    }

    @Test
    public void createOrUpdateKinosWithTmdbIdExistingWithId() {
        //noinspection ResultOfMethodCallIgnored
        doReturn(null).when(serieDto).getKinoId();
        //noinspection ResultOfMethodCallIgnored
        doReturn(4561432L).when(serieDto).getTmdbKinoId();

        TmdbSerie tmdbKino = mock(TmdbSerie.class);
        doReturn(tmdbKino).when(toDbBuilder).buildTmdbSerie(serieDto);

        Review review = mock(Review.class);
        doReturn(review).when(toDbBuilder).buildReview(serieDto);

        SerieReview serieReview = new SerieReview(null, tmdbKino, review);
        SerieReview dbSerieReview = new SerieReview(24L, tmdbKino, review);

        doReturn(dbSerieReview).when(serieReviewRepository).findByMovieId(4561432L);

        new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null)
                .createOrUpdateFromImport(new ArrayList<SerieDto>() {{
                    add(serieDto);
                }});

        verify(serieDto).setKinoId(24L);

        verify(serieReviewRepository).createOrUpdate(serieReview);
        verify(tmdbSerieRepository).createOrUpdate(tmdbKino);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void syncSerieWithTmdb() {
        SerieReview serieReview = mock(SerieReview.class);
        doReturn(serieReview).when(serieReviewRepository).findByMovieId(24);

        SerieService serieService = new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository,
                tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null);
        serieService.syncWithTmdb(24);

        verify(tmdbGetterService).startSyncWithTmdb(serieService, serieReview, 24);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void updateTmdbInfo() {
        SerieReview serieReview = mock(SerieReview.class);

        TmdbSerie tmdbSerie = mock(TmdbSerie.class);
        doReturn(tmdbSerie).when(serieReview).getSerie();
        Review review = mock(Review.class);
        doReturn(review).when(serieReview).getReview();

        SerieDto updatedDto = mock(SerieDto.class);
        doReturn("Banane").when(updatedDto).getOverview();
        doReturn("Bleue").when(updatedDto).getPosterPath();
        doReturn("24/01/2018").when(updatedDto).getReleaseDate();
        doReturn(2018).when(updatedDto).getYear();
        doReturn("Alala").when(updatedDto).getTitle();

        new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository,
                tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).updateTmdbInfo(updatedDto, serieReview);

        verify(tmdbSerie).setOverview("Banane");
        verify(tmdbSerie).setPoster_path("Bleue");
        verify(tmdbSerie).setRelease_date("24/01/2018");
        verify(tmdbSerie).setYear(2018);
        verify(review).setTitle("Alala");

        verify(tmdbSerieRepository).createOrUpdate(tmdbSerie);
        verify(reviewRepository).createOrUpdate(review);
    }
}