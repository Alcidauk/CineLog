package com.ulicae.cinelog.android.activities.add.wishlist;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.view.ViewDataActivity;
import com.ulicae.cinelog.data.dto.data.TvShowToSerieDataDtoBuilder;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.services.wishlist.SerieWishlistService;
import com.uwetrottmann.tmdb2.entities.BaseTvShow;

import org.parceler.Parcels;

import java.util.List;

/**
 * CineLog Copyright 2018 Pierre Rognon
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
public class WishlistTvResultsAdapter extends ArrayAdapter<BaseTvShow> {

    private TvShowToSerieDataDtoBuilder tvShowToSerieDataDtoBuilder;
    private SerieWishlistService serieWishlistService;

    public WishlistTvResultsAdapter(Context context, List<BaseTvShow> results) {
        super(context, R.layout.search_result_item, results);
        this.tvShowToSerieDataDtoBuilder = new TvShowToSerieDataDtoBuilder();
        this.serieWishlistService = new SerieWishlistService(((KinoApplication) context.getApplicationContext()).getDaoSession());
    }


    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.wishlist_search_result_item, parent, false);
        }

        BaseTvShow item = getItem(position);

        WishlistDataDto wishlistDataDto = item != null ? serieWishlistService.getByTmdbId(item.id) : null;
        if(wishlistDataDto == null) {
            wishlistDataDto = tvShowToSerieDataDtoBuilder.build(item);
        }

        WishlistItemViewHolder holder = new WishlistItemViewHolder(convertView);

        populateTitle(wishlistDataDto, holder);
        populateYear(wishlistDataDto, holder);
        populatePoster(wishlistDataDto, holder);

        final WishlistDataDto finalWishlistDataDto = wishlistDataDto;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewDataActivity.class);
                intent.putExtra("dataDto", Parcels.wrap(finalWishlistDataDto));

                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    private void populatePoster(WishlistDataDto kinoDto, WishlistItemViewHolder holder) {
        if (kinoDto.getPosterPath() != null) {
            Glide.with(getContext())
                    .load("https://image.tmdb.org/t/p/w185" + kinoDto.getPosterPath())
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
