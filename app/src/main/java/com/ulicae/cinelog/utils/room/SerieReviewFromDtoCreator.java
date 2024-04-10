package com.ulicae.cinelog.utils.room;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.room.dao.ReviewDao;
import com.ulicae.cinelog.room.entities.Review;
import com.ulicae.cinelog.room.entities.ReviewEntityType;

public class SerieReviewFromDtoCreator extends EntityFromDtoCreator<Review, ReviewDao, SerieDto> {
    public SerieReviewFromDtoCreator(ReviewDao dao) {
        super(dao);
    }

    @Override
    Review createRoomInstanceFromDto(SerieDto itemDto) {
            return new Review(
                    Math.toIntExact(10000 + itemDto.getId()),
                    ReviewEntityType.SERIE,
                    itemDto.getTitle(),
                    itemDto.getReview_date(),
                    itemDto.getReview(),
                    itemDto.getRating(),
                    itemDto.getMaxRating()
            );
    }
}
