package com.ulicae.cinelog.utils.room;

import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.room.dao.ReviewDao;
import com.ulicae.cinelog.room.entities.Review;
import com.ulicae.cinelog.room.entities.ItemEntityType;

public class SerieReviewFromDtoCreator extends EntityFromDtoCreator<Review, ReviewDao, SerieDto> {

    private final int biggestMovieReviewId;

    public SerieReviewFromDtoCreator(ReviewDao dao, int biggestMovieReviewId) {
        super(dao);
        this.biggestMovieReviewId = biggestMovieReviewId;
    }

    @Override
    Review createRoomInstanceFromDto(SerieDto itemDto) {
            return new Review(
                    Math.toIntExact(biggestMovieReviewId + itemDto.getId()),
                    ItemEntityType.SERIE,
                    itemDto.getTitle(),
                    itemDto.getReview_date(),
                    itemDto.getReview(),
                    itemDto.getRating(),
                    itemDto.getMaxRating()
            );
    }
}
