package com.ulicae.cinelog.android.v2.fragments.review.add;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.add.wishlist.WishlistItemCallback;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.services.reviews.DataService;
import com.ulicae.cinelog.databinding.ContentAddReviewBinding;
import com.ulicae.cinelog.network.TmdbServiceWrapper;
import com.ulicae.cinelog.network.task.NetworkTaskManager;
import com.uwetrottmann.tmdb2.entities.BaseRatingObject;

import java.lang.ref.WeakReference;
import java.util.List;


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
public abstract class SearchTmdbFragment<T extends BaseRatingObject> extends Fragment {

    protected ContentAddReviewBinding binding;

    protected TmdbServiceWrapper tmdbServiceWrapper;
    protected NetworkTaskManager networkTaskManager;

    protected DataService dataService;

    protected final MovieDetailsCallback movieSearchResultClickCallback =
            (kinoDto, position, inDb) -> ((MainActivity) requireActivity()).navigateToKino(kinoDto, position, inDb);
    protected final MovieReviewCreationCallback movieReviewCreationClickCallback =
            (kinoDto) -> ((MainActivity) requireActivity()).navigateToReview(kinoDto, true);
    protected final WishlistItemCallback wishlistItemCallback =
            (dataDto) -> ((MainActivity) requireActivity()).navigateToWishlistItem(dataDto);

    private Handler handler;

    private final static int TRIGGER_SERACH = 1;

    // Where did 1000 come from? It's arbitrary, since I can't find average android typing speed.
    private final static long SEARCH_TRIGGER_DELAY_IN_MS = 1000;

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        binding.kinoSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onSearchChange(charSequence, i, i1, i2);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.kinoSearchAddFromScratch.setOnClickListener(this::onFromScratchClick);

        tmdbServiceWrapper = new TmdbServiceWrapper(requireContext());

        handler = new AddReviewHandler(new WeakReference<>(this));
    }

    protected abstract void onFromScratchClick(View view);

    private void startSearchTask() {
        if (isNetworkAvailable()) {
            executeTask(binding.kinoSearch.getText().toString());
        } else {
            Toast t = Toast.makeText(requireContext(),
                    getString(R.string.addkino_error_no_network),
                    Toast.LENGTH_LONG);
            t.show();
        }
    }

    protected abstract void executeTask(String textToSearch);

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @SuppressWarnings("unused")
    public void onSearchChange(CharSequence s, int start, int before, int count) {
        if (count > 0) {
            binding.kinoSearchProgressBar.setVisibility(View.VISIBLE);
            handler.removeMessages(TRIGGER_SERACH);
            handler.sendEmptyMessageDelayed(TRIGGER_SERACH, SEARCH_TRIGGER_DELAY_IN_MS);
        } else if (count == 0) {
            clearListView();
        }
    }

    public void clearListView() {
        if (binding.kinoResults.getAdapter() != null) {
            binding.kinoResults.setAdapter(null);
        }
        binding.kinoSearchProgressBar.setVisibility(View.GONE);
    }

    public abstract void populateListView(final List<T> movies);

    static class AddReviewHandler extends Handler {
        private WeakReference<SearchTmdbFragment> addReview;

        AddReviewHandler(WeakReference<SearchTmdbFragment> addReview) {
            this.addReview = addReview;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TRIGGER_SERACH) {
                addReview.get().startSearchTask();
            }
        }
    }
}
