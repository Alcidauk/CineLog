package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.LocalKino;
import com.ulicae.cinelog.data.dao.TmdbKino;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.KinoDtoBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
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
public class KinoServiceTest {

    @Mock
    private LocalKinoRepository localKinoRepository;

    @Mock
    private TmdbKinoRepository tmdbKinoRepository;

    @Mock
    private KinoDtoBuilder kinoDtoBuilder;

    @Mock
    private KinoDto kinoDto;

    @Test
    public void getKino() throws Exception {
        LocalKino localKino = mock(LocalKino.class);

        doReturn(localKino).when(localKinoRepository).find(4L);
        doReturn(kinoDto).when(kinoDtoBuilder).build(localKino);

        assertEquals(
                kinoDto,
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder).getKino(4L)
        );
    }

    @Test
    public void getKinoByTmdbMovieId() throws Exception {
        LocalKino localKino = mock(LocalKino.class);

        doReturn(localKino).when(localKinoRepository).findByMovieId(4L);
        doReturn(kinoDto).when(kinoDtoBuilder).build(localKino);

        assertEquals(
                kinoDto,
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder).getWithTmdbId(4L)
        );
    }

    @Test
    public void getKinosByRating() throws Exception {
        LocalKino localKino = mock(LocalKino.class);
        LocalKino anotherLocalKino = mock(LocalKino.class);

        doReturn(Arrays.asList(localKino, anotherLocalKino)).when(localKinoRepository).findAllByRating(true);

        KinoDto anotherKinoDto = mock(KinoDto.class);
        doReturn(kinoDto).when(kinoDtoBuilder).build(localKino);
        doReturn(anotherKinoDto).when(kinoDtoBuilder).build(anotherLocalKino);

        assertEquals(
                Arrays.asList(kinoDto, anotherKinoDto),
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder).getKinosByRating(true)
        );
    }

    @Test
    public void getKinosByRatingDesc() throws Exception {
        LocalKino localKino = mock(LocalKino.class);
        LocalKino anotherLocalKino = mock(LocalKino.class);

        doReturn(Arrays.asList(localKino, anotherLocalKino)).when(localKinoRepository).findAllByRating(false);

        KinoDto anotherKinoDto = mock(KinoDto.class);
        doReturn(kinoDto).when(kinoDtoBuilder).build(localKino);
        doReturn(anotherKinoDto).when(kinoDtoBuilder).build(anotherLocalKino);

        assertEquals(
                Arrays.asList(kinoDto, anotherKinoDto),
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder).getKinosByRating(false)
        );
    }

    @Test
    public void getKinosByReviewDateAsc() throws Exception {
        LocalKino localKino = mock(LocalKino.class);
        LocalKino anotherLocalKino = mock(LocalKino.class);

        doReturn(Arrays.asList(localKino, anotherLocalKino)).when(localKinoRepository).findAllByReviewDate(true);

        KinoDto anotherKinoDto = mock(KinoDto.class);
        doReturn(kinoDto).when(kinoDtoBuilder).build(localKino);
        doReturn(anotherKinoDto).when(kinoDtoBuilder).build(anotherLocalKino);

        assertEquals(
                Arrays.asList(kinoDto, anotherKinoDto),
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder).getKinosByReviewDate(true)
        );
    }

    @Test
    public void getKinosByReviewDateDesc() throws Exception {
        LocalKino localKino = mock(LocalKino.class);
        LocalKino anotherLocalKino = mock(LocalKino.class);

        doReturn(Arrays.asList(localKino, anotherLocalKino)).when(localKinoRepository).findAllByReviewDate(false);

        KinoDto anotherKinoDto = mock(KinoDto.class);
        doReturn(kinoDto).when(kinoDtoBuilder).build(localKino);
        doReturn(anotherKinoDto).when(kinoDtoBuilder).build(anotherLocalKino);

        assertEquals(
                Arrays.asList(kinoDto, anotherKinoDto),
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder).getKinosByReviewDate(false)
        );
    }

    @Test
    public void getKinosByYear() throws Exception {
        LocalKino localKino = mock(LocalKino.class);
        LocalKino anotherLocalKino = mock(LocalKino.class);

        doReturn(Arrays.asList(localKino, anotherLocalKino)).when(localKinoRepository).findAllByYear(true);

        KinoDto anotherKinoDto = mock(KinoDto.class);
        doReturn(kinoDto).when(kinoDtoBuilder).build(localKino);
        doReturn(anotherKinoDto).when(kinoDtoBuilder).build(anotherLocalKino);

        assertEquals(
                Arrays.asList(kinoDto, anotherKinoDto),
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder).getKinosByYear(true)
        );
    }

    @Test
    public void getKinosByYearDesc() throws Exception {
        LocalKino localKino = mock(LocalKino.class);
        LocalKino anotherLocalKino = mock(LocalKino.class);

        doReturn(Arrays.asList(localKino, anotherLocalKino)).when(localKinoRepository).findAllByYear(false);

        KinoDto anotherKinoDto = mock(KinoDto.class);
        doReturn(kinoDto).when(kinoDtoBuilder).build(localKino);
        doReturn(anotherKinoDto).when(kinoDtoBuilder).build(anotherLocalKino);

        assertEquals(
                Arrays.asList(kinoDto, anotherKinoDto),
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder).getKinosByYear(false)
        );
    }

    @Test
    public void getAllKinos() throws Exception {
        LocalKino localKino = mock(LocalKino.class);
        LocalKino anotherLocalKino = mock(LocalKino.class);

        KinoDto anotherKinoDto = mock(KinoDto.class);

        doReturn(Arrays.asList(localKino, anotherLocalKino)).when(localKinoRepository).findAll();
        doReturn(kinoDto).when(kinoDtoBuilder).build(localKino);
        doReturn(anotherKinoDto).when(kinoDtoBuilder).build(anotherLocalKino);

        assertEquals(
                Arrays.asList(kinoDto, anotherKinoDto),
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder).getAllKinos()
        );
    }

    @Test
    public void createKino() throws Exception {
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

        KinoDto createdKino = mock(KinoDto.class);
        doReturn(createdKino).when(kinoDtoBuilder).build(kinoToCreate);

        assertEquals(
                createdKino,
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder).createOrUpdateKino(kinoDto)
        );

        verify(localKinoRepository).createOrUpdate(kinoToCreate);
        verify(tmdbKinoRepository).createOrUpdate(tmdbKino);
    }

    @Test
    public void deleteKino() throws Exception {
        //noinspection ResultOfMethodCallIgnored
        doReturn(545L).when(kinoDto).getKinoId();

        new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder).delete(kinoDto);

        verify(localKinoRepository).delete(545L);
    }
}