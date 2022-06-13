package com.ulicae.cinelog.data.dto;

import com.ulicae.cinelog.data.dao.LocalKino;
import com.ulicae.cinelog.data.dao.Tag;
import com.ulicae.cinelog.data.dao.TmdbKino;

import java.util.ArrayList;
import java.util.List;

/**
 * CineLog Copyright 2022 Pierre Rognon
 *
 *
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 *
 */
public class KinoDtoBuilder {

    TagDtoBuilder tagDtoBuilder;

    public KinoDtoBuilder() {
        this(new TagDtoBuilder());
    }

    public KinoDtoBuilder(TagDtoBuilder tagDtoBuilder) {
        this.tagDtoBuilder = tagDtoBuilder;
    }

    public KinoDto build(LocalKino localKino) {
        Long tmdbId = null;
        String posterPath = null;
        String overview = null;
        int year = 0;
        String releaseDate = null;

        TmdbKino kino = localKino.getKino();
        if (kino != null) {
            tmdbId = kino.getMovie_id();
            posterPath = kino.getPoster_path();
            overview = kino.getOverview();
            year = kino.getYear();
            releaseDate = kino.getRelease_date();
        }

        List<TagDto> tagDtos = new ArrayList<>();
        for(Tag tag : localKino.getTags()) {
            tagDtos.add(tagDtoBuilder.build(tag));
        }

        return new KinoDto(
                localKino.getId(),
                tmdbId,
                localKino.getTitle(),
                localKino.getReview_date(),
                localKino.getReview(),
                localKino.getRating(),
                localKino.getMaxRating(),
                posterPath,
                overview,
                year,
                releaseDate,
                tagDtos
        );
    }
}
