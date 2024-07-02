package com.ulicae.cinelog.room.services;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.data.services.reviews.DataService;
import com.ulicae.cinelog.data.services.reviews.ItemService;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.dao.WishlistItemDao;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Tmdb;
import com.ulicae.cinelog.room.entities.WishlistItem;

import java.util.ArrayList;
import java.util.List;

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
public class WishlistService implements ItemService<WishlistDataDto>, DataService<WishlistDataDto> {

    private AppDatabase db;
    private final ItemEntityType itemEntityType;

    public WishlistService(AppDatabase db, ItemEntityType itemEntityType) {
        this.db = db;
        this.itemEntityType = itemEntityType;
    }

    /*
    TODO avoid blockingfirst and make return async
     */
    @Override
    public List<WishlistDataDto> getAll() {
        WishlistItemDao wishlistItemDao = db.wishlistItemDao();

        List<WishlistItem> all1 = wishlistItemDao.findAll(itemEntityType).blockingFirst();
        List<WishlistDataDto> itemsDto = new ArrayList<>();
        for(WishlistItem item: all1) {
            Tmdb tmdb = item.tmdb;

            itemsDto.add(new WishlistDataDto(
                    (long) item.id,
                    null, // TODO supprimer ce param
                    item.title,
                    tmdb != null ? tmdb.posterPath : null,
                    tmdb != null ? tmdb.overview: null,
                    tmdb != null ? tmdb.year : 0,
                    tmdb != null ? tmdb.releaseDate : null,
                    this.itemEntityType == ItemEntityType.MOVIE ? WishlistItemType.MOVIE : WishlistItemType.SERIE
            ));
        }

        return itemsDto;
    }

    @Override
    public void createOrUpdateFromImport(List<WishlistDataDto> kinoDtos) {

    }

    @Override
    public void delete(WishlistDataDto dtoObject) {

    }

    @Override
    public WishlistDataDto getWithTmdbId(long tmdbId) {
        return null;
    }

    @Override
    public WishlistDataDto createOrUpdate(WishlistDataDto dtoObject) {
        return null;
    }

     /*private final ReviewDao reviewDao;
   private final LocalKinoRepository localKinoRepository;
    private final TmdbKinoRepository tmdbKinoRepository;
    private final KinoDtoBuilder kinoDtoBuilder;
    private final KinoDtoToDbBuilder kinoDtoToDbBuilder;
    private final TagService tagService;*/

    /*private final ReviewDtoBuilder kinoDtoBuilder;

    private final ReviewDtoToDbBuilder kinoDtoToDbBuilder;

    public ReviewService(AppDatabase appDb) {
        this(appDb.userDao());
    }

    ReviewService(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    public Flowable<KinoDto> getKino(long id) {
        // TODO send kino to something that get kino
        return reviewDao
                .find(id)
                .to(new Function<Flowable<Review>, KinoDto>() {
                    @Override
                    public KinoDto apply(Flowable<Review> reviewFlowable) throws Exception {
                        // KinoDto return kinoDtoBuilder.build(kino);
                        return new KinoDto(0L,0L, "coucou", null,
                                "a review", 2.0f, 2, "",
                                "", 10, "", new ArrayList<>());
                    }
                });
    }

    public List<KinoDto> getAll() {
        Flowable<List<Review>> localKinos = reviewDao.findAll();

        return buildKinos(localKinos);
    }

    public void createOrUpdateFromImport(List<KinoDto> kinoDtos) {
        for (KinoDto kinoDto : kinoDtos) {
            if (kinoDto.getKinoId() == null) {
                LocalKino existingKino = reviewDao.findByMovieId(kinoDto.getTmdbKinoId());
                if (existingKino != null) {
                    kinoDto.setKinoId(existingKino.getId());
                }
            }

            KinoDto createdKino = createOrUpdate(kinoDto);
            linkToTags(createdKino, kinoDto.getTags());
        }
    }

    private void linkToTags(KinoDto createdKino, List<TagDto> tags) {
        if (tags != null) {
            for (TagDto tag : tags) {
                tagService.addTagToItemIfNotExists(tag, createdKino);
            }
        }
    }

    public KinoDto createOrUpdate(KinoDto kinoDto) {
        LocalKino localKino = kinoDtoToDbBuilder.build(kinoDto);

        if (kinoDto.getTmdbKinoId() != null) {
            tmdbKinoRepository.createOrUpdate(localKino.getKino());
        }
        reviewDao.createOrUpdate(localKino);

        return kinoDtoBuilder.build(localKino);
    }

    public KinoDto getWithTmdbId(long tmdbId) {
        LocalKino byMovieId = reviewDao.findByMovieId(tmdbId);
        return byMovieId != null ? kinoDtoBuilder.build(byMovieId) : null;
    }

    public List<KinoDto> getKinosByRating(boolean asc) {
        List<LocalKino> localKinos = reviewDao.findAllByRating(asc);

        return buildKinos(localKinos);
    }

    public List<KinoDto> getKinosByYear(boolean asc) {
        List<LocalKino> localKinos = reviewDao.findAllByYear(asc);

        return buildKinos(localKinos);
    }

    public List<KinoDto> getKinosByReviewDate(boolean asc) {
        List<LocalKino> localKinos = reviewDao.findAllByReviewDate(asc);

        return buildKinos(localKinos);
    }

    public List<KinoDto> getKinosByTitle(boolean asc) {
        List<LocalKino> localKinos = localKinoRepository.findAllByTitle(asc);

        return buildKinos(localKinos);
    }

    private List<KinoDto> buildKinos(List<LocalKino> kinos) {
        List<KinoDto> kinoDtos = new ArrayList<>();
        for (LocalKino localKino : kinos) {
            kinoDtos.add(kinoDtoBuilder.build(localKino));
        }

        return kinoDtos;
    }

    public void delete(KinoDto kinoDto) {
        reviewDao.delete(kinoDto.getKinoId());
    }*/
}
