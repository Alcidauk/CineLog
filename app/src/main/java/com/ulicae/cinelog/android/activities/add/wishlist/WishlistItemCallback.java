package com.ulicae.cinelog.android.activities.add.wishlist;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;

public interface WishlistItemCallback {
    void call(WishlistDataDto finalWishlistDataDto);
}
