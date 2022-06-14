package com.ulicae.cinelog.data.services.reviews;

import com.ulicae.cinelog.data.LocalKinoRepository;
import com.ulicae.cinelog.data.TmdbKinoRepository;
import com.ulicae.cinelog.data.dao.LocalKino;
import com.ulicae.cinelog.data.dao.TmdbKino;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.KinoDtoBuilder;
import com.ulicae.cinelog.utils.KinoDtoToDbBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder, null, null).getKino(4L)
        );
    }

    @Test
    public void getKinoByTmdbMovieId() throws Exception {
        LocalKino localKino = mock(LocalKino.class);

        doReturn(localKino).when(localKinoRepository).findByMovieId(4L);
        doReturn(kinoDto).when(kinoDtoBuilder).build(localKino);

        assertEquals(
                kinoDto,
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder, null, null).getWithTmdbId(4L)
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
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder, null, null).getKinosByRating(true)
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
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder, null, null).getKinosByRating(false)
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
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder, null, null).getKinosByReviewDate(true)
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
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder, null, null).getKinosByReviewDate(false)
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
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder, null, null).getKinosByYear(true)
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
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder, null, null).getKinosByYear(false)
        );
    }

    @Test
    public void getKinosByTitleAsc() {
        LocalKino localKino = mock(LocalKino.class);
        LocalKino anotherLocalKino = mock(LocalKino.class);

        doReturn(Arrays.asList(localKino, anotherLocalKino)).when(localKinoRepository).findAllByTitle(true);

        KinoDto anotherKinoDto = mock(KinoDto.class);
        doReturn(kinoDto).when(kinoDtoBuilder).build(localKino);
        doReturn(anotherKinoDto).when(kinoDtoBuilder).build(anotherLocalKino);

        assertEquals(
                Arrays.asList(kinoDto, anotherKinoDto),
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder, null, null).getKinosByTitle(true)
        );
    }

    @Test
    public void getKinosByTitleDesc() {
        LocalKino localKino = mock(LocalKino.class);
        LocalKino anotherLocalKino = mock(LocalKino.class);

        doReturn(Arrays.asList(localKino, anotherLocalKino)).when(localKinoRepository).findAllByTitle(false);

        KinoDto anotherKinoDto = mock(KinoDto.class);
        doReturn(kinoDto).when(kinoDtoBuilder).build(localKino);
        doReturn(anotherKinoDto).when(kinoDtoBuilder).build(anotherLocalKino);

        assertEquals(
                Arrays.asList(kinoDto, anotherKinoDto),
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder, null, null).getKinosByTitle(false)
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
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder, null, null).getAll()
        );
    }

    @Test
    public void createKino() {
        LocalKino kinoToCreate = mock(LocalKino.class);
        TmdbKino tmdbKino = mock(TmdbKino.class);
        doReturn(tmdbKino).when(kinoToCreate).getKino();

        KinoDtoToDbBuilder builder = mock(KinoDtoToDbBuilder.class);
        doReturn(kinoToCreate).when(builder).build(kinoDto);

        KinoDto createdKino = mock(KinoDto.class);
        doReturn(createdKino).when(kinoDtoBuilder).build(kinoToCreate);

        assertEquals(
                createdKino,
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder, builder, null).createOrUpdate(kinoDto)
        );

        verify(localKinoRepository).createOrUpdate(kinoToCreate);
        verify(tmdbKinoRepository).createOrUpdate(tmdbKino);
    }

    @Test
    public void createKinoNullTmdb() {
        LocalKino kinoToCreate = mock(LocalKino.class);

        KinoDtoToDbBuilder builder = mock(KinoDtoToDbBuilder.class);
        doReturn(kinoToCreate).when(builder).build(kinoDto);

        KinoDto createdKino = mock(KinoDto.class);
        doReturn(createdKino).when(kinoDtoBuilder).build(kinoToCreate);

        assertEquals(
                createdKino,
                new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder, builder, null).createOrUpdate(kinoDto)
        );

        verify(localKinoRepository).createOrUpdate(kinoToCreate);
    }

    @Test
    public void createOrUpdateKinosWithTmdbIdExistingWithoutId() {
        LocalKino kinoToCreate = mock(LocalKino.class);
        TmdbKino tmdbKino = mock(TmdbKino.class);
        doReturn(tmdbKino).when(kinoToCreate).getKino();

        KinoDtoToDbBuilder builder = mock(KinoDtoToDbBuilder.class);
        doReturn(kinoToCreate).when(builder).build(kinoDto);

        KinoDto createdKino = mock(KinoDto.class);
        doReturn(createdKino).when(kinoDtoBuilder).build(kinoToCreate);

        //noinspection ResultOfMethodCallIgnored
        doReturn(34555L).when(kinoDto).getTmdbKinoId();
        //noinspection ResultOfMethodCallIgnored
        doReturn(null).when(kinoDto).getKinoId();

        LocalKino existingKino = mock(LocalKino.class);
        //noinspection ResultOfMethodCallIgnored
        doReturn(22222L).when(existingKino).getId();

        doReturn(existingKino).when(localKinoRepository).findByMovieId(34555L);

        new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder, builder, null).createOrUpdateFromImport(new ArrayList<KinoDto>(){{add(kinoDto);}});

        verify(kinoDto).setKinoId(22222L);

        verify(localKinoRepository).createOrUpdate(kinoToCreate);
        verify(tmdbKinoRepository).createOrUpdate(tmdbKino);
    }

    @Test
    public void createOrUpdateKinosWithTmdbIdExistingWithId() {
        LocalKino kinoToCreate = mock(LocalKino.class);
        TmdbKino tmdbKino = mock(TmdbKino.class);
        doReturn(tmdbKino).when(kinoToCreate).getKino();

        KinoDtoToDbBuilder builder = mock(KinoDtoToDbBuilder.class);
        doReturn(kinoToCreate).when(builder).build(kinoDto);

        KinoDto createdKino = mock(KinoDto.class);
        doReturn(createdKino).when(kinoDtoBuilder).build(kinoToCreate);

        //noinspection ResultOfMethodCallIgnored
        doReturn(15L).when(kinoDto).getKinoId();

        new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder, builder, null).createOrUpdateFromImport(new ArrayList<KinoDto>(){{add(kinoDto);}});

        verify(kinoDto, never()).setKinoId(22222L);

        verify(localKinoRepository).createOrUpdate(kinoToCreate);
        verify(tmdbKinoRepository).createOrUpdate(tmdbKino);
    }

    @Test
    public void createOrUpdateKinosWithTmdbIdNotExisting() {
        LocalKino kinoToCreate = mock(LocalKino.class);
        TmdbKino tmdbKino = mock(TmdbKino.class);
        doReturn(tmdbKino).when(kinoToCreate).getKino();

        KinoDtoToDbBuilder builder = mock(KinoDtoToDbBuilder.class);
        doReturn(kinoToCreate).when(builder).build(kinoDto);

        KinoDto createdKino = mock(KinoDto.class);
        doReturn(createdKino).when(kinoDtoBuilder).build(kinoToCreate);

        //noinspection ResultOfMethodCallIgnored
        doReturn(34555L).when(kinoDto).getTmdbKinoId();
        //noinspection ResultOfMethodCallIgnored
        doReturn(null).when(kinoDto).getKinoId();

        doReturn(null).when(localKinoRepository).findByMovieId(34555L);

        new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder, builder, null).createOrUpdateFromImport(new ArrayList<KinoDto>(){{add(kinoDto);}});

        verify(kinoDto, never()).setKinoId(22222L);

        verify(localKinoRepository).createOrUpdate(kinoToCreate);
        verify(tmdbKinoRepository).createOrUpdate(tmdbKino);
    }

    @Test
    public void deleteKino() {
        //noinspection ResultOfMethodCallIgnored
        doReturn(545L).when(kinoDto).getKinoId();

        new KinoService(localKinoRepository, tmdbKinoRepository, kinoDtoBuilder, null, null).delete(kinoDto);

        verify(localKinoRepository).delete(545L);
    }
}