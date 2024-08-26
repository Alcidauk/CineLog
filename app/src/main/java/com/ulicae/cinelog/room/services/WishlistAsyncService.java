package com.ulicae.cinelog.room.services;

import com.ulicae.cinelog.data.dao.WishlistSerie;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistSerieToSerieDataDtoBuilder;
import com.ulicae.cinelog.data.services.AsyncDataService;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.CinelogSchedulers;
import com.ulicae.cinelog.room.dao.WishlistItemDao;
import com.ulicae.cinelog.room.dto.utils.to.WishlistItemToDataDtoBuilder;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Tmdb;
import com.ulicae.cinelog.room.entities.WishlistItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

/**
 * CineLog Copyright 2024 Pierre Rognon
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
public class WishlistAsyncService implements AsyncDataService<WishlistDataDto> {

    private WishlistItemDao wishlistItemDao;

    private WishlistSerieToSerieDataDtoBuilder wishlistSerieToSerieDataDtoBuilder;
    private WishlistItemToDataDtoBuilder wishlistItemToDataDtoBuilder;

    private CinelogSchedulers cinelogSchedulers;

    public WishlistAsyncService(AppDatabase db) {
        this(
                db.wishlistItemDao(),
                new WishlistSerieToSerieDataDtoBuilder(),
                new WishlistItemToDataDtoBuilder(),
                new CinelogSchedulers()
        );
    }

    WishlistAsyncService(WishlistItemDao wishlistItemDao,
                         WishlistSerieToSerieDataDtoBuilder wishlistSerieToSerieDataDtoBuilder,
                         WishlistItemToDataDtoBuilder wishlistItemToDataDtoBuilder,
                         CinelogSchedulers cinelogSchedulers) {
        this.wishlistItemDao = wishlistItemDao;
        this.wishlistSerieToSerieDataDtoBuilder = wishlistSerieToSerieDataDtoBuilder;
        this.wishlistItemToDataDtoBuilder = wishlistItemToDataDtoBuilder;
        this.cinelogSchedulers = cinelogSchedulers;
    }

    public Completable createSerieData(WishlistDataDto wishlistDataDto) {
        Tmdb tmdbSerie = null;
        Long tmdbId = wishlistDataDto.getTmdbId() != null ? wishlistDataDto.getTmdbId().longValue() : null;

        if (wishlistDataDto.getTmdbId() != null) {
            tmdbSerie = new Tmdb(
                    Math.toIntExact(tmdbId),
                    wishlistDataDto.getPosterPath(),
                    wishlistDataDto.getOverview(),
                    wishlistDataDto.getFirstYear(),
                    wishlistDataDto.getReleaseDate());
        }

        WishlistItem wishlistItem =
                new WishlistItem(
                        wishlistDataDto.getId(),
                        ItemEntityType.SERIE,
                        wishlistDataDto.getTitle(),
                        tmdbSerie
                );

        return wishlistItemDao.insert(wishlistItem);
    }

    /**
     * TODO juste ramener le contenu de wishlist, et le tmdb depuis ailleurs pour le mettre dans
     * l'UI
     *
     * @param type
     * @return
     */
    public Flowable<List<WishlistDataDto>> findAllForType(ItemEntityType type) {
        return wishlistItemDao.findAll(type)
                .map(this::getDtoFromDaos);
    }

    public Completable delete(WishlistDataDto wishlistDataDto) {
        return wishlistItemDao.delete(
                        new WishlistItem(
                                wishlistDataDto.getId(),
                                null,
                                null,
                                null
                        )
                )
                .subscribeOn(cinelogSchedulers.io())
                .observeOn(cinelogSchedulers.androidMainThread());
    }

    @Override
    public Flowable<List<WishlistDataDto>> findAll() {
        return wishlistItemDao
                .findAll()
                .map(this::getDtoFromDaos);
    }

    private List<WishlistDataDto> getDtoFromDaos(List<WishlistItem> items) {
        List<WishlistDataDto> wishlistDataDtos = new ArrayList<>();
        for (WishlistItem item : items) {
            wishlistDataDtos.add(wishlistItemToDataDtoBuilder.build(item, item.tmdb));
        }
        return wishlistDataDtos;
    }

    public Flowable<WishlistDataDto> findById(Long id) {
        return wishlistItemDao
                .find(id)
                .map(wishlistItem ->
                        wishlistItemToDataDtoBuilder.build(wishlistItem, wishlistItem.tmdb));
    }

    public Flowable<WishlistDataDto> getByTmdbId(Integer tmdbId) {
        return wishlistItemDao
                .findByTmdbId(tmdbId)
                .map(wishlistItem -> wishlistItemToDataDtoBuilder.build(wishlistItem, wishlistItem.tmdb));
    }

    /**
     * DATA SERVICE COMPATIBILITY
     **/

    @Override
    public Completable createOrUpdate(WishlistDataDto dtoObject) {
        return createSerieData(dtoObject);
    }

    @Override
    public void createOrUpdateFromImport(List<WishlistDataDto> dtos) {
        for (WishlistDataDto dto : dtos) {
            if (dto.getId() == null) {
                WishlistSerie existingDto = null; // TODO ishlistSerieRepository.findByTmdbId(dto.getTmdbId());
                if (existingDto != null) {
                    dto.setId(existingDto.getWishlist_serie_id());
                }
            }

            // TODO ne pas subscribe ici mais au call de createOrUpdateFromImport
            createOrUpdate(dto).subscribe();
        }
    }
}
