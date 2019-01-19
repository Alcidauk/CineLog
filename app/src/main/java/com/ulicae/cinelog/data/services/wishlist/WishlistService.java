package com.ulicae.cinelog.data.services.wishlist;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;

import java.util.List;

public interface WishlistService {

    List<WishlistDataDto> getAll();

    void delete(WishlistDataDto wishlistDataDto);
}
