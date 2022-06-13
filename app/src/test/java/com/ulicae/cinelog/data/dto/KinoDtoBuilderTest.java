package com.ulicae.cinelog.data.dto;

import com.ulicae.cinelog.data.dao.LocalKino;
import com.ulicae.cinelog.data.dao.Tag;
import com.ulicae.cinelog.data.dao.TmdbKino;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
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
public class KinoDtoBuilderTest {

    @Mock
    private LocalKino localKino;

    @Mock
    private TmdbKino tmdbKino;

    @Mock
    private Tag tag;

    @Mock
    private TagDto tagDto;

    @Mock
    private TagDtoBuilder tagDtoBuilder;

    @Test
    public void build() throws Exception {
        doReturn(2015).when(tmdbKino).getYear();
        doReturn("a path").when(tmdbKino).getPoster_path();
        doReturn(24L).when(tmdbKino).getMovie_id();
        doReturn("an horrible overview").when(tmdbKino).getOverview();
        doReturn("1956/12/12").when(tmdbKino).getRelease_date();

        doReturn(tmdbKino).when(localKino).getKino();
        doReturn(4L).when(localKino).getId();
        doReturn(4.5f).when(localKino).getRating();
        doReturn(10).when(localKino).getMaxRating();
        doReturn("a superb review").when(localKino).getReview();
        doReturn("what a title").when(localKino).getTitle();
        Date reviewDate = new Date();
        doReturn(reviewDate).when(localKino).getReview_date();

        doReturn(tagDto).when(tagDtoBuilder).build(tag);
        doReturn(Collections.singletonList(tag)).when(localKino).getTags();

        assertEquals(
                new KinoDto(
                      4L,
                        24L,
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
                new KinoDtoBuilder(tagDtoBuilder).build(localKino)
        );
    }
}