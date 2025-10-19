package com.ulicae.cinelog.room.dto.utils.from;

import com.ulicae.cinelog.room.dto.KinoDto;
import com.ulicae.cinelog.room.dto.SerieDto;
import com.ulicae.cinelog.room.dao.ReviewDao;
import com.ulicae.cinelog.room.entities.ItemEntityType;
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
public class ReviewFromDtoCreator extends SyncEntityFromDtoCreator<Review, ReviewDao, KinoDto> {

    private int biggestMovieReviewId;

    public ReviewFromDtoCreator(ReviewDao dao) {
        this(dao, 0);
    }

    public ReviewFromDtoCreator(ReviewDao dao, int biggestMovieReviewId) {
        super(dao);
        this.biggestMovieReviewId = biggestMovieReviewId;
    }

    @Override
    public Review createRoomInstanceFromDto(KinoDto kinoDto) {
        return new Review(
                (kinoDto.getId() != null ? kinoDto.getId() : 0L) + this.biggestMovieReviewId,
                kinoDto instanceof SerieDto ? ItemEntityType.SERIE : ItemEntityType.MOVIE,
                kinoDto.getTitle(),
                kinoDto.getReview_date(),
                kinoDto.getReview(),
                kinoDto.getRating(),
                kinoDto.getMaxRating(),
                (kinoDto.getTmdbKinoId() != null && !kinoDto.getTmdbKinoId().equals(0L)) ?
                new Tmdb(
                        kinoDto.getTmdbKinoId(),
                        kinoDto.getPosterPath(),
                        kinoDto.getOverview(),
                        kinoDto.getYear(),
                        kinoDto.getReleaseDate()
                ) : null
        );
    }
}
