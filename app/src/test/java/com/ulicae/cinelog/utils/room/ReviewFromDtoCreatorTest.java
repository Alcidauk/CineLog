package com.ulicae.cinelog.utils.room;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.room.dao.ReviewDao;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Review;

import junit.framework.TestCase;

import org.mockito.Mock;

import java.util.Date;

public class ReviewFromDtoCreatorTest extends TestCase {


    @Mock
    private ReviewDao reviewDao;

    public void testCreateRoomInstanceFromMovieDto() {
        Date reviewDate = new Date();
        assertEquals(
                new Review(
                        345,
                        ItemEntityType.MOVIE,
                        "a title",
                        reviewDate,
                        "a review",
                        5.0f,
                        5
                ),
                new ReviewFromDtoCreator(reviewDao, 0)
                        .createRoomInstanceFromDto(
                                new KinoDto(
                                        345L,
                                        23542512312L,
                                        "a title",
                                        reviewDate,
                                        "a review",
                                        5.0f,
                                        5,
                                        null,
                                        null,
                                        0,
                                        null,
                                        null
                                )
                        )
        );
    }

    public void testCreateRoomInstanceFromMovieDtoWithBiggestId() {
        Date reviewDate = new Date();
        assertEquals(
                new Review(
                        350,
                        ItemEntityType.MOVIE,
                        "a title",
                        reviewDate,
                        "a review",
                        5.0f,
                        5
                ),
                new ReviewFromDtoCreator(reviewDao, 5)
                        .createRoomInstanceFromDto(
                                new KinoDto(
                                        345L,
                                        23542512312L,
                                        "a title",
                                        reviewDate,
                                        "a review",
                                        5.0f,
                                        5,
                                        null,
                                        null,
                                        0,
                                        null,
                                        null
                                )
                        )
        );
    }

    public void testCreateRoomInstanceFromSerieDto() {
        Date reviewDate = new Date();
        assertEquals(
                new Review(
                        345L,
                        ItemEntityType.SERIE,
                        "a title",
                        reviewDate,
                        "a review",
                        5.0f,
                        5
                ),
                new ReviewFromDtoCreator(reviewDao, 0)
                        .createRoomInstanceFromDto(
                                new SerieDto(
                                        345L,
                                        23542512312L,
                                        45L,
                                        "a title",
                                        reviewDate,
                                        "a review",
                                        5.0f,
                                        5,
                                        null,
                                        null,
                                        0,
                                        null,
                                        null
                                )
                        )
        );
    }

    public void testCreateRoomInstanceFromSerieDtoWithBiggestId() {
        Date reviewDate = new Date();
        assertEquals(
                new Review(
                        352L,
                        ItemEntityType.SERIE,
                        "a title",
                        reviewDate,
                        "a review",
                        5.0f,
                        5
                ),
                new ReviewFromDtoCreator(reviewDao, 7)
                        .createRoomInstanceFromDto(
                                new SerieDto(
                                        345L,
                                        23542512312L,
                                        45L,
                                        "a title",
                                        reviewDate,
                                        "a review",
                                        5.0f,
                                        5,
                                        null,
                                        null,
                                        0,
                                        null,
                                        null
                                )
                        )
        );
    }

}