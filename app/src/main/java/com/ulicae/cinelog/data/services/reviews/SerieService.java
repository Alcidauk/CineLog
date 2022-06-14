package com.ulicae.cinelog.data.services.reviews;

import android.content.Context;

import com.ulicae.cinelog.data.ReviewRepository;
import com.ulicae.cinelog.data.SerieReviewRepository;
import com.ulicae.cinelog.data.TmdbSerieRepository;
import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.Review;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.SerieKinoDtoBuilder;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.services.reviews.DataService;
import com.ulicae.cinelog.data.services.tags.TagService;
import com.ulicae.cinelog.network.TmdbGetterService;
import com.ulicae.cinelog.utils.SerieDtoToDbBuilder;

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
public class SerieService implements DataService<SerieDto> {

    private final SerieReviewRepository serieReviewRepository;
    private final ReviewRepository reviewRepository;
    private final TmdbSerieRepository tmdbSerieRepository;
    private final TmdbGetterService tmdbGetterService;
    private final SerieKinoDtoBuilder serieKinoDtoBuilder;
    private final SerieDtoToDbBuilder dtoToDbBuilder;
    private final TagService tagService;

    SerieService(SerieReviewRepository serieReviewRepository, ReviewRepository reviewRepository,
                 TmdbSerieRepository tmdbSerieRepository, TmdbGetterService tmdbGetterService,
                 SerieKinoDtoBuilder serieKinoDtoBuilder, SerieDtoToDbBuilder dtoToDbBuilder,
                 TagService tagService) {
        this.serieReviewRepository = serieReviewRepository;
        this.reviewRepository = reviewRepository;
        this.tmdbSerieRepository = tmdbSerieRepository;
        this.tmdbGetterService = tmdbGetterService;
        this.serieKinoDtoBuilder = serieKinoDtoBuilder;
        this.dtoToDbBuilder = dtoToDbBuilder;
        this.tagService = tagService;
    }

    public SerieService(DaoSession daoSession, Context context) {
        this(new SerieReviewRepository(daoSession),
                new ReviewRepository(daoSession),
                new TmdbSerieRepository(daoSession),
                new TmdbGetterService(context), new SerieKinoDtoBuilder(),
                new SerieDtoToDbBuilder(),
                new TagService(daoSession)
        );
    }

    public SerieDto getReview(long id) {
        SerieReview review = serieReviewRepository.find(id);

        return serieKinoDtoBuilder.build(review);
    }

    public SerieDto getWithTmdbId(long movieId) {
        SerieReview review = serieReviewRepository.findByMovieId(movieId);

        return review != null ? serieKinoDtoBuilder.build(review) : null;
    }

    @Override
    public void delete(SerieDto serieDto) {
        serieReviewRepository.delete(serieDto.getKinoId());
    }

    @Override
    public SerieDto createOrUpdate(SerieDto serieDto) {
        Review review = dtoToDbBuilder.buildReview(serieDto);
        TmdbSerie tmdbSerie = dtoToDbBuilder.buildTmdbSerie(serieDto);

        if(review != null){
            reviewRepository.createOrUpdate(review);
        }
        if(tmdbSerie != null){
            tmdbSerieRepository.createOrUpdate(tmdbSerie);
        }

        SerieReview serieReview = new SerieReview(serieDto.getKinoId(), tmdbSerie, review);
        serieReviewRepository.createOrUpdate(serieReview);

        return serieKinoDtoBuilder.build(serieReview);
    }

    // TODO generic
    @Override
    public void createOrUpdateFromImport(List<SerieDto> serieDtos) {
        for (SerieDto serieDto : serieDtos) {
            if (serieDto.getKinoId() == null) {
                SerieReview existingReview = serieReviewRepository.findByMovieId(serieDto.getTmdbKinoId());
                if (existingReview != null) {
                    serieDto.setKinoId(existingReview.getId());
                }
            }

            SerieDto createdSerie = createOrUpdate(serieDto);
            linkToTags(createdSerie, serieDto.getTags());
        }
    }

    private void linkToTags(KinoDto createdKino, List<TagDto> tags) {
        if (tags != null) {
            for (TagDto tag : tags) {
                tagService.addTagToItemIfNotExists(tag, createdKino);
            }
        }
    }

    public void syncWithTmdb(long tmdbId) {
        SerieReview serieReview = serieReviewRepository.findByMovieId(tmdbId);

        tmdbGetterService.startSyncWithTmdb(this, serieReview, tmdbId);
    }

    public void updateTmdbInfo(SerieDto updatedDto, SerieReview serieReview){
        Review review = serieReview.getReview();
        review.setTitle(updatedDto.getTitle());

        TmdbSerie tmdbSerie = serieReview.getSerie();
        tmdbSerie.setYear(updatedDto.getYear());
        tmdbSerie.setPoster_path(updatedDto.getPosterPath());
        tmdbSerie.setOverview(updatedDto.getOverview());
        tmdbSerie.setRelease_date(updatedDto.getReleaseDate());

        tmdbSerieRepository.createOrUpdate(tmdbSerie);
        reviewRepository.createOrUpdate(review);
    }

    public List<SerieDto> getAll() {
        return buildKinos(serieReviewRepository.findAll());
    }

    public List<SerieDto> getAllByRating(boolean asc) {
        return buildKinos(serieReviewRepository.findAllByRating(asc));
    }

    public List<SerieDto> getAllByTitle(boolean asc) {
        return buildKinos(serieReviewRepository.findAllByTitle(asc));
    }

    private List<SerieDto> buildKinos(List<SerieReview> kinos) {
        List<SerieDto> kinoDtos = new ArrayList<>();
        for (SerieReview serieReview : kinos) {
            kinoDtos.add(serieKinoDtoBuilder.build(serieReview));
        }

        return kinoDtos;
    }
}
