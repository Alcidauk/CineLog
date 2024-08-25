package com.ulicae.cinelog.android.v2.fragments.wishlist.add;

import static io.reactivex.rxjava3.schedulers.Schedulers.io;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.fragments.wishlist.room.add.WishlistItemCallback;
import com.ulicae.cinelog.data.dto.data.MovieToWishlistDataDtoBuilder;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.services.wishlist.MovieWishlistService;
import com.ulicae.cinelog.databinding.WishlistSearchResultItemBinding;
import com.ulicae.cinelog.room.services.WishlistAsyncService;
import com.uwetrottmann.tmdb2.entities.BaseMovie;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

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
// TODO generic with WishlistTvResultsAdapter
public class WishlistMovieRoomResultsAdapter extends ArrayAdapter<BaseMovie> {

    private MovieToWishlistDataDtoBuilder movieToWishlistDataDtoBuilder;
    private WishlistAsyncService movieWishlistService;

    private final WishlistItemCallback wishlistItemCallback;

    public WishlistMovieRoomResultsAdapter(Context context, List<BaseMovie> results, WishlistItemCallback wishlistItemCallback) {
        super(context, R.layout.tmdb_item_row, results);
        this.wishlistItemCallback = wishlistItemCallback;
        this.movieToWishlistDataDtoBuilder = new MovieToWishlistDataDtoBuilder();
        this.movieWishlistService = new WishlistAsyncService(((KinoApplication) context.getApplicationContext()).getDb());
    }


    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        WishlistSearchResultItemBinding binding;
        if (convertView == null) {
            binding = WishlistSearchResultItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            convertView = binding.getRoot();
        } else {
            binding = WishlistSearchResultItemBinding.bind(convertView);
        }

        BaseMovie item = getItem(position);

        if(item != null){
            View finalConvertView = convertView;
            movieWishlistService.getByTmdbId(item.id)
                    .subscribeOn(io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext((dataDto) -> {
                        populateItem(finalConvertView, binding, dataDto);
                    });
        } else {
            populateItem(convertView, binding, movieToWishlistDataDtoBuilder.build(item));
        }

        return convertView;
    }

    private void populateItem(View convertView, WishlistSearchResultItemBinding binding, WishlistDataDto wishlistDataDto) {
        WishlistItemViewHolder holder = new WishlistItemViewHolder(binding);

        populateTitle(wishlistDataDto, holder);
        populateYear(wishlistDataDto, holder);
        populatePoster(wishlistDataDto, holder);

        final WishlistDataDto finalWishlistDataDto = wishlistDataDto;
        convertView.setOnClickListener(v -> wishlistItemCallback.call(finalWishlistDataDto));
    }

    private void populatePoster(WishlistDataDto dataDto, WishlistItemViewHolder holder) {
        if (dataDto.getPosterPath() != null) {
            Glide.with(getContext())
                    .load("https://image.tmdb.org/t/p/w185" + dataDto.getPosterPath())
                    .centerCrop()
                    .crossFade()
                    .into(holder.getPoster());
        } else {
            Glide.with(getContext())
                    .load(R.drawable.noimage_purple)
                    .centerCrop()
                    .crossFade()
                    .into(holder.getPoster());
        }
    }

    private void populateYear(WishlistDataDto wishlistDataDto, WishlistItemViewHolder holder) {
        if (wishlistDataDto.getReleaseDate() != null && !wishlistDataDto.getReleaseDate().equals("")) {
            holder.getYear().setText(String.format("%d", wishlistDataDto.getFirstYear()));
        } else {
            holder.getYear().setText("");
        }
    }

    private void populateTitle(WishlistDataDto wishlistDataDto, WishlistItemViewHolder holder) {
        holder.getTitle().setText(wishlistDataDto.getTitle() != null ? wishlistDataDto.getTitle() : "");
    }
}
