package com.ulicae.cinelog.utils.room;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.room.dao.WishlistItemDao;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.WishlistItem;

public class WishlistFromDtoCreator extends EntityFromDtoCreator<WishlistItem, WishlistItemDao, WishlistDataDto> {
    private final ItemEntityType itemEntityType;

    public WishlistFromDtoCreator(WishlistItemDao dao, ItemEntityType itemEntityType) {
        super(dao);
        this.itemEntityType = itemEntityType;
    }

    @Override
    WishlistItem createRoomInstanceFromDto(WishlistDataDto itemDto) {
        return new WishlistItem(
                Math.toIntExact(itemEntityType == ItemEntityType.SERIE ? (10000 + itemDto.getId()) : itemDto.getId()),
                itemEntityType,
                itemDto.getTitle()
        );
    }
}
