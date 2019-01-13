package com.ulicae.cinelog.utils;

import com.ulicae.cinelog.data.dao.Review;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dto.SerieDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;


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
public class SerieDtoToDbBuilderTest {

    @Mock
    private SerieDto serieDto;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void build() {
        Date reviewDate = new Date();

        doReturn(784L).when(serieDto).getKinoId();
        doReturn(4564321L).when(serieDto).getTmdbKinoId();
        doReturn(32132L).when(serieDto).getReviewId();
        doReturn(5f).when(serieDto).getRating();
        doReturn(10).when(serieDto).getMaxRating();
        doReturn("an overview").when(serieDto).getOverview();
        doReturn("a poster path").when(serieDto).getPosterPath();
        doReturn("a release date").when(serieDto).getReleaseDate();
        doReturn("a review").when(serieDto).getReview();
        doReturn("a title").when(serieDto).getTitle();
        doReturn(1456).when(serieDto).getYear();
        doReturn(reviewDate).when(serieDto).getReview_date();

        TmdbSerie tmdbSerie = new TmdbSerie(
                4564321L,
                4564321,
                "a title",
                "a poster path",
                "an overview",
                1456,
                "a release date"
        );
        Review review = new Review(
                32132L,
                "a title",
                reviewDate,
                "a review",
                5f,
                10
        );

        SerieReview serieReview = new SerieReview(
                784L,
                tmdbSerie,
                review
        );

        assertEquals(
                serieReview,
                new SerieDtoToDbBuilder().build(serieDto)
        );
    }

}