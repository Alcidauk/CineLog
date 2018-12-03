package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.LocalKino;
import com.ulicae.cinelog.data.dao.Review;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.data.dao.TmdbKino;
import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.SerieKinoDtoBuilder;
import com.ulicae.cinelog.utils.KinoDtoToDbBuilder;
import com.ulicae.cinelog.utils.SerieDtoToDbBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;

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

    @Test
    public void getReview() {
        SerieReview serieReview = mock(SerieReview.class);

        doReturn(serieReview).when(serieReviewRepository).find(4L);
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                serieDto,
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, serieKinoDtoBuilder, toDbBuilder).getReview(4L)
        );
    }

    @Test
    public void getByTmdbMovieId() {
        SerieReview serieReview = mock(SerieReview.class);

        doReturn(serieReview).when(serieReviewRepository).findByMovieId(4L);
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                serieDto,
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, serieKinoDtoBuilder, toDbBuilder).getWithTmdbId(4L)
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
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, serieKinoDtoBuilder, toDbBuilder).getAll()
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
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, serieKinoDtoBuilder, toDbBuilder).getAllByRating(true)
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
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, serieKinoDtoBuilder, toDbBuilder).getAllByRating(false)
        );
    }

    @Test
    public void delete() {
        //noinspection ResultOfMethodCallIgnored
        doReturn(545L).when(serieDto).getKinoId();

        new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, serieKinoDtoBuilder, toDbBuilder).delete(serieDto);

        verify(serieReviewRepository).delete(545L);
    }

    @Test
    public void createOrUpdateKino() {
        SerieReview reviewToCreate = mock(SerieReview.class);
        TmdbSerie tmdbKino = mock(TmdbSerie.class);
        doReturn(tmdbKino).when(reviewToCreate).getSerie();

        Review review = mock(Review.class);
        doReturn(review).when(reviewToCreate).getReview();

        doReturn(reviewToCreate).when(toDbBuilder).build(serieDto);

        SerieDto createdKino = mock(SerieDto.class);
        doReturn(createdKino).when(serieKinoDtoBuilder).build(reviewToCreate);

        assertEquals(
                createdKino,
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, serieKinoDtoBuilder, toDbBuilder).createOrUpdate(serieDto)
        );

        verify(serieReviewRepository).createOrUpdate(reviewToCreate);
        verify(reviewRepository).createOrUpdate(review);
        verify(tmdbSerieRepository).createOrUpdate(tmdbKino);
    }

    @Test
    public void createOrUpdateKinoNullTmdbId() {
        SerieReview reviewToCreate = mock(SerieReview.class);

        Review review = mock(Review.class);
        doReturn(review).when(reviewToCreate).getReview();

        doReturn(reviewToCreate).when(toDbBuilder).build(serieDto);

        SerieDto createdKino = mock(SerieDto.class);
        doReturn(createdKino).when(serieKinoDtoBuilder).build(reviewToCreate);

        doReturn(null).when(serieDto).getTmdbKinoId();

        assertEquals(
                createdKino,
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, serieKinoDtoBuilder, toDbBuilder).createOrUpdate(serieDto)
        );

        verify(serieReviewRepository).createOrUpdate(reviewToCreate);
        verify(reviewRepository).createOrUpdate(review);
        verify(tmdbSerieRepository, never()).createOrUpdate(any(TmdbSerie.class));
    }

    @Test
    public void createOrUpdateKinosWithTmdbIdExistingWithId() {
        SerieReview reviewToCreate = mock(SerieReview.class);
        TmdbSerie tmdbKino = mock(TmdbSerie.class);
        doReturn(tmdbKino).when(reviewToCreate).getSerie();

        doReturn(reviewToCreate).when(toDbBuilder).build(serieDto);

        SerieDto createdSerieDto = mock(SerieDto.class);
        doReturn(createdSerieDto).when(serieKinoDtoBuilder).build(reviewToCreate);

        //noinspection ResultOfMethodCallIgnored
        doReturn(15L).when(this.serieDto).getKinoId();

        new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, serieKinoDtoBuilder, toDbBuilder)
                .createOrUpdateWithTmdbId(new ArrayList<SerieDto>() {{
                    add(SerieServiceTest.this.serieDto);
                }});

        verify(serieDto, never()).setKinoId(22222L);

        verify(serieReviewRepository).createOrUpdate(reviewToCreate);
        verify(tmdbSerieRepository).createOrUpdate(tmdbKino);
    }
}