package com.ulicae.cinelog.utils.room;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.room.dao.ReviewDao;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Review;

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
public class ReviewFromDtoCreator extends EntityFromDtoCreator<Review, ReviewDao, KinoDto> {

    private final ItemEntityType itemEntityType;
    private int biggestMovieReviewId;

    public ReviewFromDtoCreator(ReviewDao dao, ItemEntityType itemEntityType) {
        this(dao, itemEntityType, 0);
    }

    public ReviewFromDtoCreator(ReviewDao dao, ItemEntityType itemEntityType, int biggestMovieReviewId) {
        super(dao);
        this.itemEntityType = itemEntityType;
        this.biggestMovieReviewId = biggestMovieReviewId;
    }

    @Override
    public Review createRoomInstanceFromDto(KinoDto kinoDto) {
        return new Review(
                Math.toIntExact(kinoDto.getId()) + this.biggestMovieReviewId,
                this.itemEntityType,
                kinoDto.getTitle(),
                kinoDto.getReview_date(),
                kinoDto.getReview(),
                kinoDto.getRating(),
                kinoDto.getMaxRating()
        );
    }
}
