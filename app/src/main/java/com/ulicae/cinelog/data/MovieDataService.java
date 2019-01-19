package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.TmdbKino;
import com.ulicae.cinelog.data.dao.WishlistMovie;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistMovieToSerieDataDtoBuilder;
import com.ulicae.cinelog.data.services.WishlistService;

import java.util.ArrayList;
import java.util.List;

public class MovieDataService implements WishlistService {

    private final WishlistMovieRepository wishlistMovieRepository;
    private TmdbKinoRepository tmdbKinoRepository;
    private WishlistMovieToSerieDataDtoBuilder wishlistMovieToSerieDataDtoBuilder;

    public MovieDataService(DaoSession daoSession) {
        this(new WishlistMovieRepository(daoSession), new TmdbKinoRepository(daoSession), new WishlistMovieToSerieDataDtoBuilder());
    }

    MovieDataService(WishlistMovieRepository wishlistMovieRepository, TmdbKinoRepository tmdbKinoRepository, WishlistMovieToSerieDataDtoBuilder wishlistMovieToSerieDataDtoBuilder) {
        this.wishlistMovieRepository = wishlistMovieRepository;
        this.tmdbKinoRepository = tmdbKinoRepository;
        this.wishlistMovieToSerieDataDtoBuilder = wishlistMovieToSerieDataDtoBuilder;
    }

    public void createMovieData(WishlistDataDto wishlistDataDto) {
        TmdbKino tmdbKino = null;
        if (wishlistDataDto.getTmdbId() != null) {
            tmdbKino = new TmdbKino(
                    wishlistDataDto.getTmdbId() != null ? wishlistDataDto.getTmdbId().longValue() : null,
                    wishlistDataDto.getPosterPath(),
                    wishlistDataDto.getOverview(),
                    wishlistDataDto.getFirstYear(),
                    wishlistDataDto.getReleaseDate());
            tmdbKinoRepository.createOrUpdate(tmdbKino);
        }

        WishlistMovie wishlistMovie = new WishlistMovie(
                wishlistDataDto.getId(),
                tmdbKino,
                wishlistDataDto.getTitle(),
                null
        );
        wishlistMovieRepository.createOrUpdate(wishlistMovie);
    }

    public List<WishlistDataDto> getAll() {
        List<WishlistMovie> wishlistMovies = wishlistMovieRepository.findAll();

        List<WishlistDataDto> serieDataDtos = new ArrayList<>();
        for (WishlistMovie wishlistMovie : wishlistMovies) {
            serieDataDtos.add(wishlistMovieToSerieDataDtoBuilder.build(wishlistMovie));
        }
        return serieDataDtos;
    }

    public void delete(WishlistDataDto wishlistDataDto) {
        wishlistMovieRepository.delete(wishlistDataDto.getId());
    }
}
