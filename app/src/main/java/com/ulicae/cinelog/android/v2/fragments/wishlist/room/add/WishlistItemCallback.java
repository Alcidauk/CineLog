package com.ulicae.cinelog.android.v2.fragments.wishlist.room.add;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;

public interface WishlistItemCallback {
    void call(WishlistDataDto finalWishlistDataDto);
}
