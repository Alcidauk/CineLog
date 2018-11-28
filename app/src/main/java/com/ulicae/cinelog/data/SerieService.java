package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.LocalKino;
import com.ulicae.cinelog.data.dao.Review;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.SerieKinoDtoBuilder;

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
    private ReviewRepository reviewRepository;
    private final TmdbSerieRepository tmdbSerieRepository;
    private final SerieKinoDtoBuilder serieKinoDtoBuilder;

    public SerieService(SerieReviewRepository serieReviewRepository, ReviewRepository reviewRepository, TmdbSerieRepository tmdbSerieRepository, SerieKinoDtoBuilder serieKinoDtoBuilder) {
        this.serieReviewRepository = serieReviewRepository;
        this.reviewRepository = reviewRepository;
        this.tmdbSerieRepository = tmdbSerieRepository;
        this.serieKinoDtoBuilder = serieKinoDtoBuilder;
    }

    public SerieService(DaoSession daoSession) {
        this.serieReviewRepository = new SerieReviewRepository(daoSession);
        this.tmdbSerieRepository = new TmdbSerieRepository(daoSession);
        this.reviewRepository = new ReviewRepository(daoSession);
        this.serieKinoDtoBuilder = new SerieKinoDtoBuilder();
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
        Review review = null;
        if(serieDto.getReviewId() != null ||
                serieDto.getReview() != null ||
                serieDto.getRating() != null ||
                serieDto.getReview_date() != null){
            review = new Review(
                    serieDto.getReviewId(),
                    serieDto.getTitle(),
                    serieDto.getReview_date(),
                    serieDto.getReview(),
                    serieDto.getRating(),
                    serieDto.getMaxRating()
            );

            reviewRepository.createOrUpdate(review);
        }

        TmdbSerie tmdbSerie = null;
        if(serieDto.getTmdbKinoId() != null){
            tmdbSerie = new TmdbSerie(
                    serieDto.getTmdbKinoId(),
                    serieDto.getPosterPath(),
                    serieDto.getOverview(),
                    serieDto.getYear(),
                    serieDto.getReleaseDate()
            );

            tmdbSerieRepository.createOrUpdate(tmdbSerie);
        }

        SerieReview serieReview = new SerieReview(
                serieDto.getKinoId(),
                tmdbSerie,
                review
        );
        serieReviewRepository.createOrUpdate(serieReview);

        return serieKinoDtoBuilder.build(serieReview);
    }

    public List<SerieDto> getAll() {
        return buildKinos(serieReviewRepository.findAll());
    }

    public List<SerieDto> getAllByRating(boolean asc) {
        return buildKinos(serieReviewRepository.findAllByRating(asc));
    }

    private List<SerieDto> buildKinos(List<SerieReview> kinos) {
        List<SerieDto> kinoDtos = new ArrayList<>();
        for (SerieReview serieReview : kinos) {
            kinoDtos.add(serieKinoDtoBuilder.build(serieReview));
        }

        return kinoDtos;
    }
}
