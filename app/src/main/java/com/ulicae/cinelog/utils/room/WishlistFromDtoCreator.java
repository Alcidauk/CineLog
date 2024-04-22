package com.ulicae.cinelog.utils.room;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.room.dao.WishlistItemDao;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.WishlistItem;

public class WishlistFromDtoCreator extends EntityFromDtoCreator<WishlistItem, WishlistItemDao, WishlistDataDto> {
    private final ItemEntityType itemEntityType;
    private final int biggestMovieReviewId;

    public WishlistFromDtoCreator(WishlistItemDao dao, ItemEntityType itemEntityType, int biggestMovieReviewId) {
        super(dao);
        this.itemEntityType = itemEntityType;
        this.biggestMovieReviewId = biggestMovieReviewId;
    }

    @Override
    WishlistItem createRoomInstanceFromDto(WishlistDataDto itemDto) {
        return new WishlistItem(
                Math.toIntExact(itemEntityType == ItemEntityType.SERIE ? (biggestMovieReviewId + itemDto.getId()) : itemDto.getId()),
                itemEntityType,
                itemDto.getTitle()
        );
    }
}