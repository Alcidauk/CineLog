package com.ulicae.cinelog.room.fragments.wishlist.add;

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
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.databinding.WishlistSearchResultItemBinding;
import com.ulicae.cinelog.room.services.WishlistAsyncService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

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
// TODO generic with WishlistTvResultsAdapter
public abstract class WishlistRoomResultsAdapter<T> extends ArrayAdapter<T> {

    private WishlistAsyncService wishlistService;

    private final WishlistItemCallback wishlistItemCallback;

    private List<Disposable> disposables;

    public WishlistRoomResultsAdapter(Context context, List<T> results, WishlistItemCallback wishlistItemCallback) {
        super(context, R.layout.tmdb_item_row, results);
        this.wishlistItemCallback = wishlistItemCallback;
        this.wishlistService = new WishlistAsyncService(((KinoApplication) context.getApplicationContext()).getDb());
        this.disposables = new ArrayList<>();
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

        T item = getItem(position);

        View finalConvertView = convertView;
        Disposable disposable = wishlistService.getByTmdbId(getId(item))
                .subscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((dataDto) -> {
                    // TODO mieux faire graphiquement
                    WishlistItemRoomViewHolder holder = new WishlistItemRoomViewHolder(binding);
                    holder.getCardView().setBackgroundColor(getContext().getColor(R.color.lightenGray));

                    finalConvertView.setOnClickListener(null);
                });
        disposables.add(disposable);

        populateItem(finalConvertView, binding, build(item));

        return convertView;
    }

    protected abstract WishlistDataDto build(T item);

    protected abstract int getId(T item);

    protected void populateItem(View convertView, WishlistSearchResultItemBinding binding, WishlistDataDto wishlistDataDto) {
        WishlistItemRoomViewHolder holder = new WishlistItemRoomViewHolder(binding);

        populateTitle(wishlistDataDto, holder);
        populateYear(wishlistDataDto, holder);
        populatePoster(wishlistDataDto, holder);

        final WishlistDataDto finalWishlistDataDto = wishlistDataDto;
        convertView.setOnClickListener(v -> wishlistItemCallback.call(finalWishlistDataDto));
    }

    protected void populatePoster(WishlistDataDto dataDto, WishlistItemRoomViewHolder holder) {
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

    protected void populateYear(WishlistDataDto wishlistDataDto, WishlistItemRoomViewHolder holder) {
        if (wishlistDataDto.getReleaseDate() != null && !wishlistDataDto.getReleaseDate().equals("")) {
            holder.getYear().setText(String.format("%d", wishlistDataDto.getFirstYear()));
        } else {
            holder.getYear().setText("");
        }
    }

    protected void populateTitle(WishlistDataDto wishlistDataDto, WishlistItemRoomViewHolder holder) {
        holder.getTitle().setText(wishlistDataDto.getTitle() != null ? wishlistDataDto.getTitle() : "");
    }

    public void clean() {
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
    }
}
