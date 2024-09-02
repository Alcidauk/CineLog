package com.ulicae.cinelog.room.services;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.services.AsyncDataService;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.CinelogSchedulers;
import com.ulicae.cinelog.room.dao.ReviewAsyncDao;
import com.ulicae.cinelog.room.dao.ReviewTagCrossRefDao;
import com.ulicae.cinelog.room.dao.TagDao;
import com.ulicae.cinelog.room.dto.utils.to.ReviewToDataDtoBuilder;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Review;
import com.ulicae.cinelog.room.entities.ReviewTagCrossRef;
import com.ulicae.cinelog.room.entities.Tag;
import com.ulicae.cinelog.room.entities.Tmdb;

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
public class ReviewAsyncService implements AsyncDataService<KinoDto> {

    private final ReviewAsyncDao reviewDao;
    private final ReviewTagCrossRefDao crossRefDao;
    private final TagDao tagDao;
    private final ReviewToDataDtoBuilder reviewToDataDtoBuilder;

    private CinelogSchedulers cinelogSchedulers;

    private ItemEntityType itemEntityType;

    public ReviewAsyncService(AppDatabase db, ItemEntityType itemEntityType) {
        this(
                db.reviewAsyncDao(),
                db.reviewTagCrossRefDao(),
                db.tagDao(),
                new ReviewToDataDtoBuilder(),
                new CinelogSchedulers(),
                itemEntityType
        );
    }

    public ReviewAsyncService(AppDatabase db) {
        this(
                db.reviewAsyncDao(),
                db.reviewTagCrossRefDao(),
                db.tagDao(),
                new ReviewToDataDtoBuilder(),
                new CinelogSchedulers(),
                null
        );
    }

    ReviewAsyncService(ReviewAsyncDao reviewDao,
                       ReviewTagCrossRefDao crossRefDao,
                       TagDao tagDao,
                       ReviewToDataDtoBuilder reviewToDataDtoBuilder,
                       CinelogSchedulers cinelogSchedulers,
                       ItemEntityType itemEntityType) {
        this.reviewDao = reviewDao;
        this.crossRefDao = crossRefDao;
        this.tagDao = tagDao;
        this.reviewToDataDtoBuilder = reviewToDataDtoBuilder;
        this.cinelogSchedulers = cinelogSchedulers;
        this.itemEntityType = itemEntityType;
    }

    public Review buildItem(KinoDto kinoDto) {
        Tmdb tmdb = null;
        Long tmdbId = kinoDto.getTmdbKinoId();

        if (kinoDto.getTmdbKinoId() != null) {
            tmdb = new Tmdb(
                    Math.toIntExact(tmdbId),
                    kinoDto.getPosterPath(),
                    kinoDto.getOverview(),
                    kinoDto.getYear(),
                    kinoDto.getReleaseDate());
        }

        Review review =
                new Review(
                        0L,
                        ItemEntityType.MOVIE,
                        kinoDto.getTitle(),
                        kinoDto.getReview_date(),
                        kinoDto.getReview(),
                        kinoDto.getRating(),
                        kinoDto.getMaxRating(),
                        tmdb
                );

        return review;
    }

    /**
     * TODO juste ramener le contenu de wishlist, et le tmdb depuis ailleurs pour le mettre dans
     * l'UI
     *
     * @param type
     * @return
     */
    public Flowable<List<KinoDto>> findAllForType(ItemEntityType type) {
        return reviewDao.findAll(type)
                .map(this::getDtoFromDaos);
    }


    /*
    TODO remove tmdb item
     */
    public Completable delete(KinoDto kinoDto) {
        return reviewDao.delete(
                        new Review(
                                kinoDto.getId(),
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                        )
                )
                .subscribeOn(cinelogSchedulers.io())
                .observeOn(cinelogSchedulers.androidMainThread());
    }

    @Override
    public Flowable<List<KinoDto>> findAll() {
        Flowable<List<Review>> reviewsFlowable = reviewDao.findAll();
        if (this.itemEntityType != null) {
            reviewsFlowable = reviewDao.findAll(this.itemEntityType);
        }

        Flowable<List<KinoDto>> dtoFlowable = reviewsFlowable
                .map(this::getDtoFromDaos)
                .doOnNext(
                        kinoDtos -> {
                            for (KinoDto kinoDto : kinoDtos) {
                                List<TagDto> tagDtos = getReviewTags(kinoDto);
                                kinoDto.setTags(tagDtos);
                            }

                        }
                );

        return dtoFlowable;
    }

    // TODO pas en sync ?
    private List<TagDto> getReviewTags(KinoDto kinoDto) {
        List<ReviewTagCrossRef> crossRefs = crossRefDao
                .findForReview(kinoDto.getId())
                .blockingFirst();

        List<TagDto> tagDtos = new ArrayList<>();
        for (ReviewTagCrossRef crossRef : crossRefs) {
            TagDto tagDto = tagDao.find(crossRef.tagId)
                    .map(this::getTagDtoFromTag)
                    .blockingFirst();

            tagDtos.add(tagDto);
        }

        return tagDtos;
    }

    private List<KinoDto> getDtoFromDaos(List<Review> items) {
        List<KinoDto> kinoDtos = new ArrayList<>();
        for (Review item : items) {
            kinoDtos.add(reviewToDataDtoBuilder.build(item, item.tmdb));
        }
        return kinoDtos;
    }

    private TagDto getTagDtoFromTag(Tag item) {
        return new TagDto(item.id, item.name, item.color, item.forMovies, item.forSeries);
    }

    public Flowable<KinoDto> findById(Long id) {
        return reviewDao
                .find(id)
                .map(review ->
                        reviewToDataDtoBuilder.build(review, review.tmdb))
                .doOnNext(kinoDto -> kinoDto.setTags(getReviewTags(kinoDto)));
    }

    // TODO long vs int, verify its ok
    public Flowable<KinoDto> getByTmdbId(Integer tmdbId) {
        return reviewDao
                .findByMovieId(Long.valueOf(tmdbId))
                .map(review -> reviewToDataDtoBuilder.build(review, review.tmdb))
                .doOnNext(kinoDto -> kinoDto.setTags(getReviewTags(kinoDto)));
    }

    /**
     * DATA SERVICE COMPATIBILITY
     **/

    @Override
    public Completable createOrUpdate(KinoDto dtoObject) {
        return reviewDao.insert(buildItem(dtoObject));
    }

    @Override
    public Completable createOrUpdate(List<KinoDto> dtos) {
        List<Review> items = new ArrayList<>();
        for (KinoDto dto : dtos) {
            items.add(buildItem(dto));
        }

        // TODO créer les crossrefs
        return reviewDao
                .insertAll(items);
    }

}
