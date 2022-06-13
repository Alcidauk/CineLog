package com.ulicae.cinelog.data.services.wishlist;

import com.ulicae.cinelog.data.TmdbSerieRepository;
import com.ulicae.cinelog.data.WishlistSerieRepository;
import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dao.WishlistSerie;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistSerieToSerieDataDtoBuilder;

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
public class SerieWishlistService implements WishlistService {

    private final WishlistSerieRepository wishlistSerieRepository;
    private TmdbSerieRepository tmdbSerieRepository;
    private WishlistSerieToSerieDataDtoBuilder wishlistSerieToSerieDataDtoBuilder;

    public SerieWishlistService(DaoSession daoSession) {
        this(new WishlistSerieRepository(daoSession), new TmdbSerieRepository(daoSession), new WishlistSerieToSerieDataDtoBuilder());
    }

    SerieWishlistService(WishlistSerieRepository wishlistSerieRepository, TmdbSerieRepository tmdbSerieRepository, WishlistSerieToSerieDataDtoBuilder wishlistSerieToSerieDataDtoBuilder) {
        this.wishlistSerieRepository = wishlistSerieRepository;
        this.tmdbSerieRepository = tmdbSerieRepository;
        this.wishlistSerieToSerieDataDtoBuilder = wishlistSerieToSerieDataDtoBuilder;
    }

    public void createSerieData(WishlistDataDto wishlistDataDto) {
        TmdbSerie tmdbSerie = null;
        if (wishlistDataDto.getTmdbId() != null) {
            tmdbSerie = new TmdbSerie(
                    wishlistDataDto.getTmdbId() != null ? wishlistDataDto.getTmdbId().longValue() : null,
                    wishlistDataDto.getPosterPath(),
                    wishlistDataDto.getOverview(),
                    wishlistDataDto.getFirstYear(),
                    wishlistDataDto.getReleaseDate());
            tmdbSerieRepository.createOrUpdate(tmdbSerie);
        }

        WishlistSerie wishlistSerie = new WishlistSerie(
                wishlistDataDto.getId(),
                tmdbSerie,
                wishlistDataDto.getTitle(),
                null
        );
        wishlistSerieRepository.createOrUpdate(wishlistSerie);
    }

    public List<WishlistDataDto> getAll() {
        List<WishlistSerie> wishlistSeries = wishlistSerieRepository.findAll();

        List<WishlistDataDto> serieDataDtos = new ArrayList<>();
        for (WishlistSerie wishlistSerie : wishlistSeries) {
            serieDataDtos.add(wishlistSerieToSerieDataDtoBuilder.build(wishlistSerie));
        }
        return serieDataDtos;
    }

    public void delete(WishlistDataDto wishlistDataDto) {
        wishlistSerieRepository.delete(wishlistDataDto.getId());
    }

    @Override
    public WishlistDataDto getByTmdbId(Integer id) {
        WishlistSerie wishlistSerie = wishlistSerieRepository.findByTmdbId(id);
        return wishlistSerie != null ? wishlistSerieToSerieDataDtoBuilder.build(wishlistSerie) : null;
    }

    @Override
    public WishlistDataDto getById(Long id) {
        WishlistSerie wishlistSerie = wishlistSerieRepository.find(id);
        return wishlistSerie != null ? wishlistSerieToSerieDataDtoBuilder.build(wishlistSerie) : null;
    }

    /** DATA SERVICE COMPATIBILITY **/

    @Override
    public WishlistDataDto getWithTmdbId(long tmdbId) {
        return getByTmdbId(Long.valueOf(tmdbId).intValue());
    }

    @Override
    public WishlistDataDto createOrUpdate(WishlistDataDto dtoObject) {
        createSerieData(dtoObject);
        return getById(dtoObject.getId());
    }

    // TODO generification
    @Override
    public void createOrUpdateFromImport(List<WishlistDataDto> dtos) {
        for (WishlistDataDto dto : dtos) {
            if(dto.getId() == null) {
                WishlistSerie existingDto = wishlistSerieRepository.findByTmdbId(dto.getTmdbId());
                if (existingDto != null) {
                    dto.setId(existingDto.getWishlist_serie_id());
                }
            }

            createOrUpdate(dto);
        }
    }
}
