package com.ulicae.cinelog.android.activities.fragments.wishlist;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.utils.image.ImageCacheDownloader;

import java.util.List;

/**
 * CineLog Copyright 2019 Pierre Rognon
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
class WishlistListAdapter extends ArrayAdapter<WishlistDataDto> {

    WishlistListAdapter(@NonNull Context context, @NonNull List<WishlistDataDto> objects) {
        super(context, R.layout.wishlist_item, objects);
    }

    public long getItemId(int position) {
        return 0;
    }

    // createOrUpdate a new RelativeView for each item referenced by the Adapter
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.wishlist_item, parent, false);
        }

        TextView kinoTitleTextView = (TextView) convertView.findViewById(R.id.item_title);
        TextView kinoYearTextView = (TextView) convertView.findViewById(R.id.item_year);
        ImageView posterView = (ImageView) convertView.findViewById(R.id.item_poster);

        WishlistDataDto dataDto = getItem(position);
        if (dataDto != null) {
            kinoTitleTextView.setText(dataDto.getTitle());

            if (dataDto.getFirstYear() != 0) {
                kinoYearTextView.setText(String.valueOf(dataDto.getFirstYear()));
            } else {
                kinoYearTextView.setText("");
            }

            if (dataDto.getPosterPath() != null) {
                Glide.with(getContext())
                        .load(
                                new ImageCacheDownloader(
                                        getContext().getExternalCacheDir(),
                                        dataDto.getPosterPath())
                                        .getPosterFinder()
                                        .getImage(dataDto.getPosterPath()))
                        .centerCrop()
                        .crossFade()
                        .into(posterView);
            } else {
                Glide.with(getContext())
                        .load(R.drawable.noimage_purple)
                        .centerCrop()
                        .crossFade()
                        .into(posterView);
            }
        }

        return convertView;
    }

}
