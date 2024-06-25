package com.ulicae.cinelog.room.dto.utils.from;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.room.dao.ReviewTagCrossRefDao;
import com.ulicae.cinelog.room.dto.utils.from.TagReviewCrossRefFromDtoCreator;
import com.ulicae.cinelog.room.entities.ReviewTagCrossRef;

import junit.framework.TestCase;

import org.mockito.Mock;

public class TagReviewCrossRefFromDtoCreatorTest extends TestCase {

    @Mock
    private ReviewTagCrossRefDao reviewTagCrossRefDao;

    public void testCreateRoomInstanceFromKinoDto() {
        assertEquals(
                new ReviewTagCrossRef(345L, 576L),
                new TagReviewCrossRefFromDtoCreator(
                        reviewTagCrossRefDao,
                        new KinoDto(
                                345L,
                                23542512312L,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                0,
                                null,
                                null
                        ),
                        0)
                        .createRoomInstanceFromDto(
                                new TagDto(
                                        576L,
                                        null,
                                        null,
                                        true,
                                        false
                                )
                        )
        );
    }

    public void testCreateRoomInstanceFromSerieDto() {
        assertEquals(
                new ReviewTagCrossRef(43L, 576L),
                new TagReviewCrossRefFromDtoCreator(
                        reviewTagCrossRefDao,
                        new SerieDto(
                                43L,
                                345L,
                                23542512312L,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                0,
                                null,
                                null
                        ),
                        0)
                        .createRoomInstanceFromDto(
                                new TagDto(
                                        576L,
                                        null,
                                        null,
                                        true,
                                        false
                                )
                        )
        );
    }

}