package com.ulicae.cinelog.android.v2.fragments.wishlist.add;

import android.widget.ImageView;
import android.widget.TextView;

import com.ulicae.cinelog.databinding.WishlistSearchResultItemBinding;

/**
 * CineLog Copyright 2022 Pierre Rognon
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
public class WishlistItemViewHolder {

    private final WishlistSearchResultItemBinding binding;

    WishlistItemViewHolder(WishlistSearchResultItemBinding binding) {
        this.binding = binding;
    }

    public TextView getTitle() {
        return binding.kinoTitle;
    }

    public TextView getYear() {
        return binding.kinoYear;
    }

    public ImageView getPoster() {
        return binding.kinoPoster;
    }

}
