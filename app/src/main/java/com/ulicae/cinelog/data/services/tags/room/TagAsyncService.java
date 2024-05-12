package com.ulicae.cinelog.data.services.tags.room;

import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.services.AsyncDataService;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.entities.ReviewTagCrossRef;
import com.ulicae.cinelog.room.entities.Tag;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
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
public class TagAsyncService implements AsyncDataService<TagDto> {

    private final AppDatabase db;

    public TagAsyncService(AppDatabase db) {
        this.db = db;
    }

    @Override
    public Completable createOrUpdate(TagDto dtoObject) {
        return db.tagDao().insert(
                new Tag(
                        dtoObject.getId() != null ? Math.toIntExact(dtoObject.getId()) : 0,
                        dtoObject.getName(),
                        dtoObject.getColor(),
                        dtoObject.isForMovies(),
                        dtoObject.isForSeries()
                ));
    }

    @Override
    public void delete(TagDto dtoObject) {
        // TODO how to delete without building an object
        Tag tagToDelete = new Tag(Math.toIntExact(dtoObject.getId()), null, null, false, false);

        db.tagDao().delete(tagToDelete);
    }

    @Override
    public Flowable<List<TagDto>> findAll() {
        return db.tagDao().findAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::getDtoFromDaos);
    }

    public List<TagDto> findMovieTags() {
        // TODO avoid blocking first
        return db.tagDao().findMovieTags()
                .subscribeOn(Schedulers.io())
                .map(this::getDtoFromDaos)
                .blockingFirst();
    }

    public List<TagDto> findSerieTags() {
        // TODO avoid blocking first
        return db.tagDao().findSerieTags()
                .subscribeOn(Schedulers.io())
                .map(this::getDtoFromDaos)
                .blockingFirst();
    }

    public Completable addTagToItemIfNotExists(int reviewId, int tagId) {
        return db.reviewTagCrossRefDao().insert(
                new ReviewTagCrossRef(reviewId, tagId)
        );
    }

    public void removeTagFromItemIfExists(int reviewId, int tagId) {
        db.reviewTagCrossRefDao().delete(new ReviewTagCrossRef(reviewId, tagId));
    }

    private List<TagDto> getDtoFromDaos(List<Tag> coucou) {
        List<TagDto> tagDtos = new ArrayList<>();
        for (Tag tag : coucou) {
            tagDtos.add(new TagDto(
                    (long) tag.id, tag.name, tag.color, tag.forMovies, tag.forSeries
            ));
        }
        return tagDtos;
    }
}
