package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dao.WishlistSerie;
import com.ulicae.cinelog.data.dto.data.SerieDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistSerieToSerieDataDtoBuilder;

import java.util.ArrayList;
import java.util.List;

public class SerieDataService {

    private final WishlistSerieRepository wishlistSerieRepository;
    private TmdbSerieRepository tmdbSerieRepository;
    private WishlistSerieToSerieDataDtoBuilder wishlistSerieToSerieDataDtoBuilder;

    public SerieDataService(DaoSession daoSession) {
        this(new WishlistSerieRepository(daoSession), new TmdbSerieRepository(daoSession), new WishlistSerieToSerieDataDtoBuilder());
    }

    SerieDataService(WishlistSerieRepository wishlistSerieRepository, TmdbSerieRepository tmdbSerieRepository, WishlistSerieToSerieDataDtoBuilder wishlistSerieToSerieDataDtoBuilder) {
        this.wishlistSerieRepository = wishlistSerieRepository;
        this.tmdbSerieRepository = tmdbSerieRepository;
        this.wishlistSerieToSerieDataDtoBuilder = wishlistSerieToSerieDataDtoBuilder;
    }

    public void createSerieData(SerieDataDto serieDataDto) {
        TmdbSerie tmdbSerie = null;
        if (serieDataDto.getTmdbId() != null) {
            tmdbSerie = new TmdbSerie(
                    serieDataDto.getTmdbId() != null ? serieDataDto.getTmdbId().longValue() : null,
                    serieDataDto.getPosterPath(),
                    serieDataDto.getOverview(),
                    serieDataDto.getFirstYear(),
                    serieDataDto.getReleaseDate());
            tmdbSerieRepository.createOrUpdate(tmdbSerie);
        }

        WishlistSerie wishlistSerie = new WishlistSerie(
                serieDataDto.getId(),
                tmdbSerie,
                serieDataDto.getTitle(),
                null
        );
        wishlistSerieRepository.createOrUpdate(wishlistSerie);
    }

    public List<SerieDataDto> getAll() {
        List<WishlistSerie> wishlistSeries = wishlistSerieRepository.findAll();

        List<SerieDataDto> serieDataDtos = new ArrayList<>();
        for (WishlistSerie wishlistSerie : wishlistSeries) {
            serieDataDtos.add(wishlistSerieToSerieDataDtoBuilder.build(wishlistSerie));
        }
        return serieDataDtos;
    }
}
