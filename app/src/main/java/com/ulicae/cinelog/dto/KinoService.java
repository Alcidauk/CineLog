package com.ulicae.cinelog.dto;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.LocalKino;
import com.ulicae.cinelog.data.dao.TmdbKino;
import com.ulicae.cinelog.data.db.LocalKinoRepository;
import com.ulicae.cinelog.data.db.TmdbKinoRepository;

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
public class KinoService {

    private final LocalKinoRepository localKinoRepository;
    private final TmdbKinoRepository tmdbKinoRepository;
    private final KinoDtoBuilder kinoDtoBuilder;

    public KinoService(DaoSession session) {
        this.localKinoRepository = new LocalKinoRepository(session);
        this.tmdbKinoRepository = new TmdbKinoRepository(session);
        this.kinoDtoBuilder = new KinoDtoBuilder();
    }

    KinoService(LocalKinoRepository localKinoRepository, TmdbKinoRepository tmdbKinoRepository, KinoDtoBuilder kinoDtoBuilder) {
        this.localKinoRepository = localKinoRepository;
        this.tmdbKinoRepository = tmdbKinoRepository;
        this.kinoDtoBuilder = kinoDtoBuilder;
    }

    public KinoDto getKino(long id) {
        LocalKino kino = localKinoRepository.find(id);

        return kinoDtoBuilder.build(kino);
    }

    public List<KinoDto> getAllKinos() {
        List<LocalKino> localKinos = localKinoRepository.findAll();

        return buildKinos(localKinos);
    }

    public void createOrUpdateKinos(List<KinoDto> kinoDtos) {
        for (KinoDto kinoDto : kinoDtos) {
            createOrUpdateKino(kinoDto);
        }
    }

    public KinoDto createOrUpdateKino(KinoDto kinoDto) {
        //noinspection UnnecessaryUnboxing
        LocalKino localKino = new LocalKino(
                kinoDto.getKinoId(),
                kinoDto.getTmdbKinoId() != null ? kinoDto.getTmdbKinoId().longValue() : 0L,
                kinoDto.getTitle(),
                kinoDto.getReview_date(),
                kinoDto.getReview(),
                kinoDto.getRating(),
                kinoDto.getMaxRating()
        );

        if (kinoDto.getTmdbKinoId() != null) {
            TmdbKino tmdbKino = new TmdbKino(
                    kinoDto.getTmdbKinoId(),
                    kinoDto.getPosterPath(),
                    kinoDto.getOverview(),
                    kinoDto.getYear(),
                    kinoDto.getReleaseDate()
            );
            tmdbKinoRepository.createOrUpdate(tmdbKino);

            localKino.setKino(tmdbKino);
        }

        localKinoRepository.createOrUpdate(localKino);

        return kinoDtoBuilder.build(localKino);
    }

    public KinoDto getKinoByTmdbMovieId(long tmdbMovieId) {
        LocalKino byMovieId = localKinoRepository.findByMovieId(tmdbMovieId);
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

    private List<KinoDto> buildKinos(List<LocalKino> kinos) {
        List<KinoDto> kinoDtos = new ArrayList<>();
        for (LocalKino localKino : kinos) {
            kinoDtos.add(kinoDtoBuilder.build(localKino));
        }

        return kinoDtos;
    }

    public void deleteKino(KinoDto kinoDto) {
        localKinoRepository.delete(kinoDto.getKinoId());
    }
}
