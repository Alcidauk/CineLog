package com.ulicae.cinelog.room.services;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.data.dto.SerieEpisodeDto;
import com.ulicae.cinelog.data.services.AsyncDataService;
import com.ulicae.cinelog.room.CinelogSchedulers;
import com.ulicae.cinelog.room.dao.ReviewDao;
import com.ulicae.cinelog.room.dao.TmdbSerieEpisodeDao;
import com.ulicae.cinelog.room.dto.utils.to.TmdbSerieEpisodeToSerieEpisodeDtoBuilder;
import com.ulicae.cinelog.room.entities.ReviewWithEpisodes;
import com.ulicae.cinelog.room.entities.TmdbSerieEpisode;

import java.util.ArrayList;
import java.util.Date;
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
public class SerieEpisodeAsyncService implements AsyncDataService<SerieEpisodeDto> {

    private final ReviewDao reviewDao;
    private final TmdbSerieEpisodeDao tmdbSerieEpisodeDao;

    private TmdbSerieEpisodeToSerieEpisodeDtoBuilder tmdbSerieEpisodeToDataDtoBuilder;

    private CinelogSchedulers cinelogSchedulers;

    public SerieEpisodeAsyncService(KinoApplication app) {
        this(
                app.getDb().reviewDao(),
                app.getDb().tmdbSerieEpisodeDao(),
                new TmdbSerieEpisodeToSerieEpisodeDtoBuilder(),
                new CinelogSchedulers()
        );
    }

    public SerieEpisodeAsyncService(
            ReviewDao reviewDao,
            TmdbSerieEpisodeDao tmdbSerieEpisodeDao,
            TmdbSerieEpisodeToSerieEpisodeDtoBuilder tmdbSerieEpisodeToDataDtoBuilder,
            CinelogSchedulers cinelogSchedulers
    ) {
        this.reviewDao = reviewDao;
        this.tmdbSerieEpisodeDao = tmdbSerieEpisodeDao;
        this.tmdbSerieEpisodeToDataDtoBuilder = tmdbSerieEpisodeToDataDtoBuilder;
        this.cinelogSchedulers = cinelogSchedulers;
    }

    public TmdbSerieEpisode buildItem(SerieEpisodeDto serieEpisodeDto) {
        // We assume build method used by creation method, and therefore, current date is used as watching date
        serieEpisodeDto.setWatchingDate(new Date());

        return new TmdbSerieEpisode(
                serieEpisodeDto.getTmdbEpisodeId(),
                serieEpisodeDto.getReviewId(),
                serieEpisodeDto.getWatchingDate());
    }

    // TODO pas de reviewId à 0L
    public Completable delete(SerieEpisodeDto serieEpisodeDto) {
        return tmdbSerieEpisodeDao.delete(
                        new TmdbSerieEpisode(
                                serieEpisodeDto.getTmdbEpisodeId(),
                                0L,
                                null
                        )
                )
                .subscribeOn(cinelogSchedulers.io())
                .observeOn(cinelogSchedulers.androidMainThread());
    }

    @Override
    public Flowable<List<SerieEpisodeDto>> findAll() {
        return tmdbSerieEpisodeDao.findAll().map(this::getDtoFromDaos);
    }

    public Flowable<ReviewWithEpisodes> findSerieEpisodes(Long tmdbId) {
        return reviewDao.getReviewWithEpisodes(tmdbId);
    }

    private List<SerieEpisodeDto> getDtoFromDaos(List<TmdbSerieEpisode> items) {
        List<SerieEpisodeDto> serieEpisodeDtos = new ArrayList<>();
        for (TmdbSerieEpisode item : items) {
            serieEpisodeDtos.add(tmdbSerieEpisodeToDataDtoBuilder.build(item));
        }
        return serieEpisodeDtos;
    }

    public Flowable<SerieEpisodeDto> findById(Long id) {
        return tmdbSerieEpisodeDao
                .find(id)
                .map(tmdbSerieEpisode ->
                        tmdbSerieEpisodeToDataDtoBuilder.build(tmdbSerieEpisode));
    }

    @Override
    public Single<Long> createOrUpdate(SerieEpisodeDto dtoObject) {
        return tmdbSerieEpisodeDao.insert(buildItem(dtoObject));
    }

    @Override
    public Single createOrUpdate(List<SerieEpisodeDto> dtos) {
        List<TmdbSerieEpisode> items = new ArrayList<>();
        for (SerieEpisodeDto dto : dtos) {
            items.add(buildItem(dto));
        }

        return tmdbSerieEpisodeDao.insertAll(items);
    }


    /* TODO reintroduce this code
    public List<SerieEpisodeDto> getDtoEpisodes(List<TvEpisode> tvEpisodes, Long tmdbSerieId) {

        List<SerieEpisode> existingEpisodes = this.serieEpisodeRepository.findByTmdbSerieId(tmdbSerieId);

        List<SerieEpisodeDto> episodeDtos = new ArrayList<>();
        for (TvEpisode tvEpisode : tvEpisodes) {
            episodeDtos.add(getEpisodeAsDto(existingEpisodes, tvEpisode, tmdbSerieId));
        }

        return episodeDtos;
    }

    private SerieEpisodeDto getEpisodeAsDto(List<SerieEpisode> existingEpisodes,
                                            TvEpisode tvEpisode,
                                            Long tmdbSerieId) {
        for (SerieEpisode episode : existingEpisodes) {
            if (episode.getTmdbEpisodeId().equals(tvEpisode.id)) {
                return serieEpisodeDtoBuilder.buildFromTvAndDb(episode, tvEpisode);
            }
        }

        return serieEpisodeDtoBuilder.build(tvEpisode, tmdbSerieId);
    }*/

}
