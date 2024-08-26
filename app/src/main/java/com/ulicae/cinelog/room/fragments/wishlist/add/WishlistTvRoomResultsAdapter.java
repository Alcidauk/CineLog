package com.ulicae.cinelog.room.fragments.wishlist.add;

import android.content.Context;

import com.ulicae.cinelog.data.dto.data.TvShowToSerieDataDtoBuilder;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.uwetrottmann.tmdb2.entities.BaseTvShow;

import java.util.List;

/**
 * CineLog Copyright 2024 Pierre Rognon
 * <p>
 * <p>
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 */
public class WishlistTvRoomResultsAdapter extends WishlistRoomResultsAdapter<BaseTvShow> {

    private TvShowToSerieDataDtoBuilder tvShowToSerieDataDtoBuilder;


    public WishlistTvRoomResultsAdapter(Context context, List<BaseTvShow> results, WishlistItemCallback wishlistItemCallback) {
        super(context, results, wishlistItemCallback);
        this.tvShowToSerieDataDtoBuilder = new TvShowToSerieDataDtoBuilder();
    }

    @Override
    protected WishlistDataDto build(BaseTvShow item) {
        return tvShowToSerieDataDtoBuilder.build(item);
    }

    @Override
    protected int getId(BaseTvShow item) {
        return item.id;
    }

}
