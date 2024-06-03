package com.ulicae.cinelog.utils.room;

import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.room.dao.TagDao;
import com.ulicae.cinelog.room.entities.Tag;

import junit.framework.TestCase;

import org.mockito.Mock;

public class TagFromDtoCreatorTest extends TestCase {

    @Mock
    private TagDao tagDao;

    public void testCreateRoomInstanceFromDto() {
        assertEquals(
                new Tag(453, "a tag", "#000000", false, true),
                new TagFromDtoCreator(tagDao).createRoomInstanceFromDto(
                        new TagDto(
                                453L,
                                "a tag",
                                "#000000",
                                false,
                                true
                        )
                )
        );
    }

    public void testCreateRoomInstanceFromDtoNullId() {
        assertEquals(
                new Tag(0, "a tag", "#000000", false, true),
                new TagFromDtoCreator(tagDao).createRoomInstanceFromDto(
                        new TagDto(
                                null,
                                "a tag",
                                "#000000",
                                false,
                                true
                        )
                )
        );
    }
}