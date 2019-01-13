package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dto.data.SerieDataDto;
import com.ulicae.cinelog.data.dto.data.TmdbSerieToSerieDataDtoBuilder;

import java.util.ArrayList;
import java.util.List;

public class SerieDataService {

    private TmdbSerieRepository tmdbSerieRepository;
    private TmdbSerieToSerieDataDtoBuilder tmdbSerieToSerieDataDtoBuilder;

    public SerieDataService(DaoSession daoSession) {
        this(new TmdbSerieRepository(daoSession), new TmdbSerieToSerieDataDtoBuilder());
    }

    SerieDataService(TmdbSerieRepository tmdbSerieRepository, TmdbSerieToSerieDataDtoBuilder tmdbSerieToSerieDataDtoBuilder) {
        this.tmdbSerieRepository = tmdbSerieRepository;
        this.tmdbSerieToSerieDataDtoBuilder = tmdbSerieToSerieDataDtoBuilder;
    }

    public void createSerieData(SerieDataDto serieDataDto) {
        TmdbSerie serieToCreate = new TmdbSerie(
                serieDataDto.getId() != null ? serieDataDto.getId().longValue() : null,
                serieDataDto.getTmdbId(),
                "A movie",
                "2125",
                "an overview",
                2015,
                "A release date");

        tmdbSerieRepository.createOrUpdate(serieToCreate);
    }

    public List<SerieDataDto> getAll() {
        List<TmdbSerie> tmdbSeries = tmdbSerieRepository.findAll();

        List<SerieDataDto> serieDataDtos = new ArrayList<>();
        for (TmdbSerie tmdbSerie : tmdbSeries) {
            serieDataDtos.add(tmdbSerieToSerieDataDtoBuilder.build(tmdbSerie));
        }
        return serieDataDtos;
    }
}
