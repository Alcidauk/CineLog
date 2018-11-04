package com.ulicae.cinelog.dto;

import com.ulicae.cinelog.dao.LocalKino;
import com.ulicae.cinelog.dao.TmdbKino;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class KinoDtoBuilderTest {

    @Mock
    private LocalKino localKino;

    @Mock
    private TmdbKino tmdbKino;

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
                        "1956/12/12"
                ),
                new KinoDtoBuilder().build(localKino)
        );
    }
}