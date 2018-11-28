package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.LocalKino;
import com.ulicae.cinelog.data.dao.TmdbKino;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.KinoDtoBuilder;
import com.ulicae.cinelog.utils.KinoDtoToDbBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * CineLog Copyright 2018 Pierre Rognon
 * kinolog Copyright (C) 2017  ryan rigby
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
public class KinoService implements DataService<KinoDto> {

    private final LocalKinoRepository localKinoRepository;
    private final TmdbKinoRepository tmdbKinoRepository;
    private final KinoDtoBuilder kinoDtoBuilder;
    private KinoDtoToDbBuilder kinoDtoToDbBuilder;

    public KinoService(DaoSession session) {
        this.localKinoRepository = new LocalKinoRepository(session);
        this.tmdbKinoRepository = new TmdbKinoRepository(session);
        this.kinoDtoBuilder = new KinoDtoBuilder();
    }

    KinoService(LocalKinoRepository localKinoRepository, TmdbKinoRepository tmdbKinoRepository, KinoDtoBuilder kinoDtoBuilder, KinoDtoToDbBuilder builder) {
        this.localKinoRepository = localKinoRepository;
        this.tmdbKinoRepository = tmdbKinoRepository;
        this.kinoDtoBuilder = kinoDtoBuilder;
        kinoDtoToDbBuilder = builder;
    }

    public KinoDto getKino(long id) {
        LocalKino kino = localKinoRepository.find(id);

        return kinoDtoBuilder.build(kino);
    }

    public List<KinoDto> getAllKinos() {
        List<LocalKino> localKinos = localKinoRepository.findAll();

        return buildKinos(localKinos);
    }

    public void createOrUpdateKinosWithTmdbId(List<KinoDto> kinoDtos) {
        for (KinoDto kinoDto : kinoDtos) {
            createOrUpdate(kinoDto);
        }
    }

    public KinoDto createOrUpdate(KinoDto kinoDto) {
        LocalKino localKino = kinoDtoToDbBuilder.build(kinoDto);

        tmdbKinoRepository.createOrUpdate(localKino.getKino());
        localKinoRepository.createOrUpdate(localKino);

        return kinoDtoBuilder.build(localKino);
    }

    public KinoDto getWithTmdbId(long tmdbId) {
        LocalKino byMovieId = localKinoRepository.findByMovieId(tmdbId);
        return byMovieId != null ? kinoDtoBuilder.build(byMovieId) : null;
    }

    public List<KinoDto> getKinosByRating(boolean asc) {
        List<LocalKino> localKinos = localKinoRepository.findAllByRating(asc);

        return buildKinos(localKinos);
    }

    public List<KinoDto> getKinosByYear(boolean asc) {
        List<LocalKino> localKinos = localKinoRepository.findAllByYear(asc);

        return buildKinos(localKinos);
    }

    public List<KinoDto> getKinosByReviewDate(boolean asc) {
        List<LocalKino> localKinos = localKinoRepository.findAllByReviewDate(asc);

        return buildKinos(localKinos);
    }

    public List<KinoDto> getKinosByTitle(boolean asc) {
        List<LocalKino> localKinos = localKinoRepository.findAllByTitle(asc);

        return buildKinos(localKinos);
    }

    private List<KinoDto> buildKinos(List<LocalKino> kinos) {
        List<KinoDto> kinoDtos = new ArrayList<>();
        for (LocalKino localKino : kinos) {
            kinoDtos.add(kinoDtoBuilder.build(localKino));
        }

        return kinoDtos;
    }

    public void delete(KinoDto kinoDto) {
        localKinoRepository.delete(kinoDto.getKinoId());
    }
}
