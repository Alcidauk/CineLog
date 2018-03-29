package com.alcidauk.cinelog.dto;

import com.alcidauk.cinelog.dao.DaoSession;
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

    public KinoDto createKino(KinoDto kinoDto) {
        @SuppressWarnings("UnnecessaryUnboxing")
        LocalKino localKino = new LocalKino(
                kinoDto.getKinoId(),
                kinoDto.getTmdbKinoId() != null ? kinoDto.getTmdbKinoId().longValue() : 0L,
                kinoDto.getTitle(),
                kinoDto.getReview_date(),
                kinoDto.getReview(),
                kinoDto.getRating(),
                kinoDto.getMaxRating()
        );
        localKinoRepository.createOrUpdate(localKino);

        TmdbKino tmdbKino = new TmdbKino(
                kinoDto.getTmdbKinoId(),
                kinoDto.getPosterPath(),
                kinoDto.getOverview(),
                kinoDto.getYear(),
                kinoDto.getReleaseDate()
        );
        tmdbKinoRepository.createOrUpdate(tmdbKino);

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

    private List<KinoDto> buildKinos(List<LocalKino> kinos){
        List<KinoDto> kinoDtos = new ArrayList<>();
        for (LocalKino localKino : kinos) {
            kinoDtos.add(kinoDtoBuilder.build(localKino));
        }

        return kinoDtos;
    }
}
