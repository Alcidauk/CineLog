package com.ulicae.cinelog.data.services.tags.room;

import android.app.Application;

import androidx.room.Room;

import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.services.reviews.DataService;
import com.ulicae.cinelog.data.services.reviews.ItemService;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.dao.ReviewTagCrossRefDao;
import com.ulicae.cinelog.room.dao.TagDao;
import com.ulicae.cinelog.room.entities.Tag;

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
public class TagService implements ItemService<TagDto>, DataService<TagDto> {

    private AppDatabase db;

    public TagService(Application application) {
        db = Room.databaseBuilder(application.getApplicationContext(), AppDatabase.class, "database-cinelog").build();
    }

    /*
    TODO avoid blockingfirst and make return async
     */
    @Override
    public List<TagDto> getAll() {
        TagDao tagDao = db.tagDao();
        ReviewTagCrossRefDao reviewTagCrossRefDao = db.reviewTagCrossRefDao();

        List<Tag> all1 = tagDao.findAll().blockingFirst();
        List<TagDto> tags = new ArrayList<>();
        for (Tag tag : all1) {
            //TODO ajouter le fetch dees liens avec des reviews
            tags.add(new TagDto(
                    (long) tag.id,
                    tag.name,
                    tag.color,
                    tag.forMovies,
                    tag.forSeries
            ));
        }

        return tags;
    }

    @Override
    public void createOrUpdateFromImport(List<TagDto> kinoDtos) {

    }

    @Override
    public void delete(TagDto dtoObject) {

    }

    @Override
    public TagDto getWithTmdbId(long tmdbId) {
        return null;
    }

    @Override
    public TagDto createOrUpdate(TagDto dtoObject) {
        return null;
    }
}
