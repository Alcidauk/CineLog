package com.ulicae.cinelog.data.services.wishlist;

import com.ulicae.cinelog.data.TmdbKinoRepository;
import com.ulicae.cinelog.data.WishlistMovieRepository;
import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.TmdbKino;
import com.ulicae.cinelog.data.dao.WishlistMovie;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistMovieToSerieDataDtoBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * CineLog Copyright 2018 Pierre Rognon
 * <p>
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
public class MovieWishlistService implements WishlistService {

    private final WishlistMovieRepository wishlistMovieRepository;
    private TmdbKinoRepository tmdbKinoRepository;
    private WishlistMovieToSerieDataDtoBuilder wishlistMovieToSerieDataDtoBuilder;

    public MovieWishlistService(DaoSession daoSession) {
        this(new WishlistMovieRepository(daoSession), new TmdbKinoRepository(daoSession), new WishlistMovieToSerieDataDtoBuilder());
    }

    MovieWishlistService(WishlistMovieRepository wishlistMovieRepository, TmdbKinoRepository tmdbKinoRepository, WishlistMovieToSerieDataDtoBuilder wishlistMovieToSerieDataDtoBuilder) {
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

    @Override
    public WishlistDataDto getByTmdbId(Integer id) {
        WishlistMovie wishlistMovie = wishlistMovieRepository.findByTmdbId(id);
        return wishlistMovie != null ? wishlistMovieToSerieDataDtoBuilder.build(wishlistMovie) : null;
    }

    @Override
    public WishlistDataDto getById(Long id) {
        WishlistMovie wishlistMovie = wishlistMovieRepository.find(id);
        return wishlistMovie != null ? wishlistMovieToSerieDataDtoBuilder.build(wishlistMovie) : null;
    }


    /** DATA SERVICE COMPATIBILITY **/

    @Override
    public WishlistDataDto getWithTmdbId(long tmdbId) {
        return getByTmdbId(Long.valueOf(tmdbId).intValue());
    }

    @Override
    public WishlistDataDto createOrUpdate(WishlistDataDto dtoObject) {
        createMovieData(dtoObject);
        return getById(dtoObject.getId());
    }

    // TODO generification
    @Override
    public void createOrUpdateFromImport(List<WishlistDataDto> dtos) {
        for (WishlistDataDto dto : dtos) {
            if(dto.getId() == null) {
                WishlistMovie existingDto = wishlistMovieRepository.findByTmdbId(dto.getTmdbId());
                if (existingDto != null) {
                    dto.setId(existingDto.getWishlist_movie_id());
                }
            }

            createOrUpdate(dto);
        }
    }
}
