package com.alcidauk.cinelog.dto;

import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.dao.TmdbKino;
import com.alcidauk.cinelog.db.LocalKinoRepository;
import com.alcidauk.cinelog.db.TmdbKinoRepository;

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
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder).getKinoByTmdbMovieId(4L)
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
                null,
                "a poster path",
                "an overview",
                1456,
                "a release date"
        );

        LocalKino kinoToCreate = new LocalKino(
                5f,
                10,
                "a review",
                "a title",
                reviewDate,
                tmdbKino
        );

        KinoDto createdKino = mock(KinoDto.class);
        doReturn(createdKino).when(kinoDtoBuilder).build(kinoToCreate);

        assertEquals(
                createdKino,
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder).createKino(kinoDto)
        );

        verify(localKinoRepository).createOrUpdate(kinoToCreate);
        verify(tmdbKinoRepository).createOrUpdate(tmdbKino);
    }

    @Test
    public void deleteKino() throws Exception {
        //noinspection ResultOfMethodCallIgnored
        doReturn(545L).when(kinoDto).getKinoId();

        new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder).deleteKino(kinoDto);

        verify(localKinoRepository).delete(545L);
    }
}