package com.ulicae.cinelog.room.fragments.wishlist.add;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;

public interface WishlistItemCallback {
    void call(WishlistDataDto finalWishlistDataDto);
}
