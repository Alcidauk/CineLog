package com.ulicae.cinelog.utils;

import com.ulicae.cinelog.data.dao.LocalKino;
import com.ulicae.cinelog.data.dao.TmdbKino;
import com.ulicae.cinelog.data.dto.KinoDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class KinoDtoToDbBuilderTest {

    @Mock
    private KinoDto kinoDto;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void build() {
        Date reviewDate = new Date();

        doReturn(784L).when(kinoDto).getKinoId();
        doReturn(4564321L).when(kinoDto).getTmdbKinoId();
        doReturn(5f).when(kinoDto).getRating();
        doReturn(10).when(kinoDto).getMaxRating();
        doReturn("an overview").when(kinoDto).getOverview();
        doReturn("a poster path").when(kinoDto).getPosterPath();
        doReturn("a release date").when(kinoDto).getReleaseDate();
        doReturn("a review").when(kinoDto).getReview();
        doReturn("a title").when(kinoDto).getTitle();
        doReturn(1456).when(kinoDto).getYear();
        doReturn(reviewDate).when(kinoDto).getReview_date();

        TmdbKino tmdbKino = new TmdbKino(
                4564321L,
                "a poster path",
                "an overview",
                1456,
                "a release date"
        );

        LocalKino kinoToCreate = new LocalKino(
                784L,
                4564321L,
                "a title",
                reviewDate,
                "a review",
                5f,
                10
        );
        kinoToCreate.setKino(tmdbKino);

        assertEquals(
                kinoToCreate,
                new KinoDtoToDbBuilder().build(kinoDto)
        );
    }
}