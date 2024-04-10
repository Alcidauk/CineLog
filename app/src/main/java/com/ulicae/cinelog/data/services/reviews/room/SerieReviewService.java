package com.ulicae.cinelog.data.services.reviews.room;

import android.app.Application;

import androidx.room.Room;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.services.reviews.DataService;
import com.ulicae.cinelog.data.services.reviews.ItemService;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.dao.ReviewDao;
import com.ulicae.cinelog.room.dao.ReviewTmdbCrossRefDao;
import com.ulicae.cinelog.room.dao.TmdbDao;
import com.ulicae.cinelog.room.entities.Review;
import com.ulicae.cinelog.room.entities.ReviewEntityType;
import com.ulicae.cinelog.room.entities.ReviewTmdbCrossRef;
import com.ulicae.cinelog.room.entities.Tmdb;

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
public class SerieReviewService implements ItemService<SerieDto>, DataService<SerieDto> {

    // TODO does not have a real usage, it should be ReviewService that does this job

    private AppDatabase db;
    public SerieReviewService(Application application) {
        db = Room.databaseBuilder(application.getApplicationContext(), AppDatabase.class, "database-cinelog").build();

    }

    /*
    TODO avoid blockingfirst and make return async
     */
    @Override
    public List<SerieDto> getAll() {
        ReviewDao reviewDao = db.reviewDao();
        ReviewTmdbCrossRefDao reviewTmdbDao = db.reviewTmdbDao();
        TmdbDao tmdbDao = db.tmdbDao();

        List<Review> all1 = reviewDao.findAll(ReviewEntityType.SERIE).blockingFirst();
        List<SerieDto> kinos = new ArrayList<>();
        for(Review review: all1) {
            Tmdb tmdb = null;
            List<ReviewTmdbCrossRef> crossRefs = reviewTmdbDao.findForReview(review.id).blockingFirst();
            for(ReviewTmdbCrossRef reviewTmdbCrossRef : crossRefs) {
                tmdb = tmdbDao.find(reviewTmdbCrossRef.movieId).blockingFirst();
            }

            kinos.add(new SerieDto(
                    (long) review.id,
                    tmdb != null ? tmdb.movieId : null,
                    (long) review.id,
                    review.title, review.reviewDate, review.review,
                    review.rating, review.maxRating,
                    tmdb != null ? tmdb.posterPath : null,
                    tmdb != null ? tmdb.overview: null,
                    tmdb != null ? tmdb.year : 0,
                    tmdb != null ? tmdb.releaseDate : null,
                    new ArrayList<>()
            ));
        }

        return kinos;
    }

    @Override
    public void createOrUpdateFromImport(List<SerieDto> kinoDtos) {

    }

    @Override
    public void delete(SerieDto dtoObject) {

    }

    @Override
    public SerieDto getWithTmdbId(long tmdbId) {
        return null;
    }

    @Override
    public SerieDto createOrUpdate(SerieDto dtoObject) {
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
