package com.ulicae.cinelog.data.services.wishlist.room;

import com.ulicae.cinelog.data.dao.WishlistSerie;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.data.dto.data.WishlistSerieToSerieDataDtoBuilder;
import com.ulicae.cinelog.data.services.wishlist.WishlistService;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.dao.WishlistItemDao;
import com.ulicae.cinelog.room.dto.utils.to.WishlistItemToDataDtoBuilder;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Tmdb;
import com.ulicae.cinelog.room.entities.WishlistItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
public class WishlistAsyncService implements WishlistService {

    private WishlistItemDao wishlistItemDao;

    private WishlistSerieToSerieDataDtoBuilder wishlistSerieToSerieDataDtoBuilder;
    private WishlistItemToDataDtoBuilder wishlistItemToDataDtoBuilder;

    public WishlistAsyncService(AppDatabase db) {
        this(
                db.wishlistItemDao(),
                new WishlistSerieToSerieDataDtoBuilder(),
                new WishlistItemToDataDtoBuilder()
        );
    }

    WishlistAsyncService(WishlistItemDao wishlistItemDao,
                         WishlistSerieToSerieDataDtoBuilder wishlistSerieToSerieDataDtoBuilder,
                         WishlistItemToDataDtoBuilder wishlistItemToDataDtoBuilder) {
        this.wishlistItemDao = wishlistItemDao;
        this.wishlistSerieToSerieDataDtoBuilder = wishlistSerieToSerieDataDtoBuilder;
        this.wishlistItemToDataDtoBuilder = wishlistItemToDataDtoBuilder;
    }

    public void createSerieData(WishlistDataDto wishlistDataDto) {
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
        wishlistItemDao.insert(wishlistItem);
    }

    /**
     * TODO juste ramener le contenu de wishlist, et le tmdb depuis ailleurs pour le mettre dans
     * l'UI
     *
     * @param type
     * @return
     */
    public List<WishlistDataDto> getAll(ItemEntityType type) {
        List<WishlistItem> items = wishlistItemDao.findAll(type).blockingFirst();

        List<WishlistDataDto> wishlistItemDtos = new ArrayList<>();
        for (WishlistItem item : items) {
            // TODO créer le builder avec room. wishlistItemDtos.add(wishlistSerieToSerieDataDtoBuilder.buildWishlistDataDto(item));
        }
        return wishlistItemDtos;
    }

    @Override
    public List<WishlistDataDto> getAll() {
        return null;
    }

    public void delete(WishlistDataDto wishlistDataDto) {
        wishlistItemDao.delete(
                        new WishlistItem(
                                wishlistDataDto.getId(),
                                wishlistDataDto.getWishlistItemType() ==
                                        WishlistItemType.MOVIE ? ItemEntityType.MOVIE : ItemEntityType.SERIE,
                                null,
                                null
                        )
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(); // TODO un toaster ?
    }

    /**
     * TODO gérer l'async
     *
     * @param id
     * @return
     */
    @Override
    public WishlistDataDto getByTmdbId(Integer id) {
        return null;
        //WishlistSerie wishlistSerie = wishlistSerieRepository.findByTmdbId(id);
        //return wishlistSerie != null ? wishlistSerieToSerieDataDtoBuilder.build(wishlistSerie) : null;
    }

    @Override
    public WishlistDataDto getById(Long id) {
        WishlistItem item = wishlistItemDao.find(id).blockingFirst();

        return item != null ? wishlistItemToDataDtoBuilder.build(item, item.tmdb) : null;
    }

    /**
     * DATA SERVICE COMPATIBILITY
     **/

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
            if (dto.getId() == null) {
                WishlistSerie existingDto = null; // TODO ishlistSerieRepository.findByTmdbId(dto.getTmdbId());
                if (existingDto != null) {
                    dto.setId(existingDto.getWishlist_serie_id());
                }
            }

            createOrUpdate(dto);
        }
    }
}
