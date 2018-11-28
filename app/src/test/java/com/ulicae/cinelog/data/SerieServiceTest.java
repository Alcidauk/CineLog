package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.Review;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.SerieKinoDtoBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * CineLog Copyright 2018 Pierre Rognon
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

    @Test
    public void getReview() {
        SerieReview serieReview = mock(SerieReview.class);

        doReturn(serieReview).when(serieReviewRepository).find(4L);
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                serieDto,
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, serieKinoDtoBuilder).getReview(4L)
        );
    }

    @Test
    public void getByTmdbMovieId() {
        SerieReview serieReview = mock(SerieReview.class);

        doReturn(serieReview).when(serieReviewRepository).findByMovieId(4L);
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                serieDto,
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, serieKinoDtoBuilder).getWithTmdbId(4L)
        );
    }

    @Test
    public void getAll() {
        final SerieReview serieReview = mock(SerieReview.class);

        doReturn(new ArrayList<SerieReview>(){{add(serieReview);}}).when(serieReviewRepository).findAll();
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                new ArrayList<SerieDto>(){{add(serieDto);}},
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, serieKinoDtoBuilder).getAll()
        );
    }

    @Test
    public void getByRatingAsc() {
        final SerieReview serieReview = mock(SerieReview.class);

        doReturn(new ArrayList<SerieReview>(){{add(serieReview);}}).when(serieReviewRepository).findAllByRating(true);
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                new ArrayList<SerieDto>(){{add(serieDto);}},
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, serieKinoDtoBuilder).getAllByRating(true)
        );
    }

    @Test
    public void getByRatingDesc() {
        final SerieReview serieReview = mock(SerieReview.class);

        doReturn(new ArrayList<SerieReview>(){{add(serieReview);}}).when(serieReviewRepository).findAllByRating(false);
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                new ArrayList<SerieDto>(){{add(serieDto);}},
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, serieKinoDtoBuilder).getAllByRating(false)
        );
    }

    @Test
    public void delete() {
        //noinspection ResultOfMethodCallIgnored
        doReturn(545L).when(serieDto).getKinoId();

        new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, serieKinoDtoBuilder).delete(serieDto);

        verify(serieReviewRepository).delete(545L);
    }

    @Test
    public void createOrUpdateKino() {
        Date reviewDate = new Date();

        TmdbSerie tmdbSerie = new TmdbSerie(
                4564321L,
                "a poster path",
                "an overview",
                1456,
                "a release date"
        );
        Review review = new Review(
                784L,
                "a title",
                reviewDate,
                "a review",
                5f,
                10
        );
        SerieReview serieReview = new SerieReview(
                515631L,
                tmdbSerie,
                review
        );

        SerieDto serieDto = new SerieDto(
                515631L,
                4564321L,
                784L,
                "a title",
                reviewDate,
                "a review",
                5f,
                10,
                "a poster path",
                "an overview",
                1456,
                "a release date"
        );
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                serieDto,
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, serieKinoDtoBuilder).createOrUpdate(serieDto)
        );

        verify(serieReviewRepository).createOrUpdate(serieReview);
        verify(reviewRepository).createOrUpdate(review);
        verify(tmdbSerieRepository).createOrUpdate(tmdbSerie);
    }
}