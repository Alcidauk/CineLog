package com.ulicae.cinelog.data.dto;

import com.ulicae.cinelog.data.dao.Review;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.data.dao.Tag;
import com.ulicae.cinelog.data.dao.TmdbSerie;

import java.util.ArrayList;
import java.util.List;

/**
 * CineLog Copyright 2022 Pierre Rognon
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
public class SerieKinoDtoBuilder {

    private final TagDtoBuilder tagDtoBuilder;

    public SerieKinoDtoBuilder() {
        this(new TagDtoBuilder());
    }

    public SerieKinoDtoBuilder(TagDtoBuilder tagDtoBuilder) {
        this.tagDtoBuilder = tagDtoBuilder;
    }

    public SerieDto build(SerieReview serieReview) {
        TmdbSerie serie = serieReview.getSerie();
        Review review = serieReview.getReview();

        List<TagDto> tagDtos = new ArrayList<>();
        if(review != null) {
            for(Tag tag : review.getTags()) {
                tagDtos.add(tagDtoBuilder.build(tag));
            }
        }

        return new SerieDto(
                serieReview.getId(),
                serie != null ? serie.getSerie_id() : null,
                review != null ? review.getId() : null,
                review != null ? review.getTitle() : null,
                review != null ? review.getReview_date() : null,
                review != null ? review.getReview() : null,
                review != null ? review.getRating() : null,
                review != null ? review.getMaxRating() : null,
                serie != null ? serie.getPoster_path() : null,
                serie != null ? serie.getOverview() : null,
                serie != null ? serie.getYear() : 0,
                serie != null ? serie.getRelease_date() : null,
                tagDtos
        );
    }
}
