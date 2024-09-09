package com.ulicae.cinelog.room.dto.utils.to;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.room.entities.Review;
import com.ulicae.cinelog.room.entities.Tmdb;

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
public class ReviewToDataDtoBuilder {

    /**
     * TODO build des tags ici ?
     * @param review
     * @return
     */
    public KinoDto build(Review review) {
        if (review.getTmdb() != null) {
            return new KinoDto(
                    review.getId(),
                    review.getTmdb().getTmdbId(),
                    review.getTitle(),
                    review.getReviewDate(),
                    review.getReview(),
                    review.rating,
                    review.maxRating,
                    review.getTmdb().getPosterPath(),
                    review.getTmdb().getOverview(),
                    review.getTmdb().getYear(),
                    review.getTmdb().getReleaseDate(),
                    null // TODO voir si on reste comme ça (en buildant en doOnNext dans le find, pas ouf
            );
        }

        return new KinoDto(
                review.id,
                null,
                review.title,
                review.reviewDate,
                review.review,
                review.rating,
                review.maxRating,
                null,
                null,
                0,
                null,
                null
        );
    }

    /*private List<TagDto> buildTags(List<Tag> tags) {
        return tags.stream()
                        .map(tag -> {
                            return new TagDto(tag.id, tag.name, tag.color, tag.forMovies, tag.forSeries); // TODO return new TagDto(crossRef.tagId, null, null, null, null)
                        })
                .collect(Collectors.toList());
    }*/
}
