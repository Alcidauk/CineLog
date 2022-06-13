package com.ulicae.cinelog.data.dto;

import com.ulicae.cinelog.data.dao.Review;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.data.dao.Tag;
import com.ulicae.cinelog.data.dao.TmdbSerie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

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
public class SerieKinoDtoBuilderTest {

    @Mock
    private SerieReview serieReview;

    @Mock
    private Review review;

    @Mock
    private TmdbSerie tmdbSerie;

    @Mock
    private Tag tag;

    @Mock
    private TagDto tagDto;

    @Mock
    private TagDtoBuilder tagDtoBuilder;

    @Test
    public void build() {
        doReturn(2015).when(tmdbSerie).getYear();
        doReturn("a path").when(tmdbSerie).getPoster_path();
        doReturn(24L).when(tmdbSerie).getSerie_id();
        doReturn("an horrible overview").when(tmdbSerie).getOverview();
        doReturn("1956/12/12").when(tmdbSerie).getRelease_date();

        doReturn(tmdbSerie).when(serieReview).getSerie();
        doReturn(review).when(serieReview).getReview();
        doReturn(4L).when(serieReview).getId();

        doReturn(15314L).when(review).getId();
        doReturn(4.5f).when(review).getRating();
        doReturn(10).when(review).getMaxRating();
        doReturn("a superb review").when(review).getReview();
        doReturn("what a title").when(review).getTitle();
        Date reviewDate = new Date();
        doReturn(reviewDate).when(review).getReview_date();


        doReturn(tagDto).when(tagDtoBuilder).build(tag);
        doReturn(Collections.singletonList(tag)).when(review).getTags();

        assertEquals(
                new SerieDto(
                        4L,
                        24L,
                        15314L,
                        "what a title",
                        reviewDate,
                        "a superb review",
                        4.5f,
                        10,
                        "a path",
                        "an horrible overview",
                        2015,
                        "1956/12/12",
                        null
                ),
                new SerieKinoDtoBuilder(tagDtoBuilder).build(serieReview)
        );
    }

    @Test(expected = NullPointerException.class)
    public void build_null() {
        new SerieKinoDtoBuilder().build(null);
    }

}