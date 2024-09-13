package com.ulicae.cinelog.room.services;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.services.AsyncDataTmdbService;
import com.ulicae.cinelog.room.CinelogSchedulers;
import com.ulicae.cinelog.room.dao.ReviewAsyncDao;
import com.ulicae.cinelog.room.dao.ReviewTagCrossRefDao;
import com.ulicae.cinelog.room.dao.TagDao;
import com.ulicae.cinelog.room.dto.utils.to.ReviewToDataDtoBuilder;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Review;
import com.ulicae.cinelog.room.entities.Tmdb;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

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
public class ReviewAsyncService implements AsyncDataTmdbService<KinoDto> {

    private final ReviewTagAsyncService reviewTagAsyncService;

    private final ReviewAsyncDao reviewDao;
    private final ReviewToDataDtoBuilder reviewToDataDtoBuilder;

    private CinelogSchedulers cinelogSchedulers;

    private ItemEntityType itemEntityType;

    public ReviewAsyncService(KinoApplication app, ItemEntityType itemEntityType) {
        this(
                new ReviewTagAsyncService(
                        app.getDb().reviewTagCrossRefDao(),
                        app.getDb().tagDao()
                ),
                app.getDb().reviewAsyncDao(),
                new ReviewToDataDtoBuilder(),
                new CinelogSchedulers(),
                itemEntityType
        );
    }

    ReviewAsyncService(ReviewTagAsyncService reviewTagAsyncService,
                       ReviewAsyncDao reviewDao,
                       ReviewToDataDtoBuilder reviewToDataDtoBuilder,
                       CinelogSchedulers cinelogSchedulers,
                       ItemEntityType itemEntityType) {
        this.reviewTagAsyncService = reviewTagAsyncService;
        this.reviewDao = reviewDao;
        this.reviewToDataDtoBuilder = reviewToDataDtoBuilder;
        this.cinelogSchedulers = cinelogSchedulers;
        this.itemEntityType = itemEntityType;
    }

    // TODO use ReviewFromDtoCreator
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
                        itemEntityType,
                        kinoDto.getTitle(),
                        kinoDto.getReview_date(),
                        kinoDto.getReview(),
                        kinoDto.getRating(),
                        kinoDto.getMaxRating(),
                        tmdb
                );

        return review;
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

        return mapAndFetchTags(reviewsFlowable);
    }

    public Flowable<List<KinoDto>> findByRating(boolean asc) {
        Flowable<List<Review>> query = asc ?
                reviewDao.findAllByRatingAsc(this.itemEntityType) :
                reviewDao.findAllByRatingDesc(this.itemEntityType);

        return mapAndFetchTags(query);
    }

    public Flowable<List<KinoDto>> findByReviewDate(boolean asc) {
        Flowable<List<Review>> query = asc ?
                reviewDao.findAllByReviewDateAsc(this.itemEntityType) :
                reviewDao.findAllByReviewDateDesc(this.itemEntityType);

        return mapAndFetchTags(query);
    }

    public Flowable<List<KinoDto>> findByYear(boolean asc) {
        Flowable<List<Review>> query = asc ?
                reviewDao.findAllByYearAsc(this.itemEntityType) :
                reviewDao.findAllByYearDesc(this.itemEntityType);

        return mapAndFetchTags(query);
    }

    public Flowable<List<KinoDto>> findByTitle(boolean asc) {
        Flowable<List<Review>> query = asc ?
                reviewDao.findAllByTitleAsc(this.itemEntityType) :
                reviewDao.findAllByTitleDesc(this.itemEntityType);

        return mapAndFetchTags(query);
    }

    public Flowable<KinoDto> findById(Long id) {
        return reviewDao
                .find(id)
                .map(review ->
                        reviewToDataDtoBuilder.build(review))
                .doOnNext(kinoDto -> kinoDto.setTags(reviewTagAsyncService.getReviewTags(kinoDto)));
    }

    public Single<KinoDto> getWithTmdbId(long tmdbId) {
        return reviewDao
                .findByMovieIdSingle(tmdbId)
                .map(reviewToDataDtoBuilder::build)
                .doAfterSuccess((kinoDto -> kinoDto.setTags(reviewTagAsyncService.getReviewTags(kinoDto))));
    }

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

    private Flowable<List<KinoDto>> mapAndFetchTags(Flowable<List<Review>> reviewsFlowable) {
        return reviewsFlowable
                .map(this::getDtoFromDaos)
                .doOnNext(
                        kinoDtos -> {
                            for (KinoDto kinoDto : kinoDtos) {
                                List<TagDto> tagDtos = reviewTagAsyncService.getReviewTags(kinoDto);
                                kinoDto.setTags(tagDtos);
                            }

                        }
                );
    }

    private List<KinoDto> getDtoFromDaos(List<Review> items) {
        List<KinoDto> kinoDtos = new ArrayList<>();
        for (Review item : items) {
            kinoDtos.add(reviewToDataDtoBuilder.build(item));
        }
        return kinoDtos;
    }

}
