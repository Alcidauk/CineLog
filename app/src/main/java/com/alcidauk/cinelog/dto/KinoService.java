package com.alcidauk.cinelog.dto;

import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.dao.TmdbKino;
import com.alcidauk.cinelog.db.LocalKinoRepository;
import com.alcidauk.cinelog.db.TmdbKinoRepository;

import java.util.ArrayList;
import java.util.List;


public class KinoService {

    private final LocalKinoRepository localKinoRepository;
    private final TmdbKinoRepository tmdbKinoRepository;
    private final KinoDtoBuilder kinoDtoBuilder;


    public KinoService(LocalKinoRepository localKinoRepository, TmdbKinoRepository tmdbKinoRepository, KinoDtoBuilder kinoDtoBuilder) {
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

        List<KinoDto> kinoDtos = new ArrayList<>();
        for (LocalKino localKino : localKinos) {
            kinoDtos.add(kinoDtoBuilder.build(localKino));
        }

        return kinoDtos;
    }

    public KinoDto createKino(KinoDto kinoDto) {
        TmdbKino tmdbKino = new TmdbKino(
                null,
                kinoDto.getPosterPath(),
                kinoDto.getOverview(),
                kinoDto.getYear(),
                kinoDto.getReleaseDate()
        );

        LocalKino localKino = new LocalKino(
                kinoDto.getRating(),
                kinoDto.getMaxRating(),
                kinoDto.getReview(),
                kinoDto.getTitle(),
                kinoDto.getReview_date(),
                tmdbKino
        );

        localKinoRepository.create(localKino);
        tmdbKinoRepository.create(tmdbKino);

        return kinoDtoBuilder.build(localKino);
    }
}
