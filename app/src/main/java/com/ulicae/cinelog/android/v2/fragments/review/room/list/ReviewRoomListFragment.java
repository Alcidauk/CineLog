package com.ulicae.cinelog.android.v2.fragments.review.room.list;

import static io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.room.dto.KinoDto;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.services.ReviewAsyncService;
import com.ulicae.cinelog.utils.PreferencesWrapper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
public abstract class ReviewRoomListFragment extends Fragment {

    ItemEntityType itemEntityType;

    ReviewListAdapter kino_adapter;

    protected ReviewAsyncService service;

    private int LIST_VIEW_STATE = -1;

    protected List<Disposable> disposables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        createService();

        this.disposables = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = ((MainActivity) requireActivity()).getFab();
        fab.setOnClickListener(v -> onFabClick());
        fab.setImageResource(R.drawable.add_kino);
        fab.show();

        SearchView searchView = ((MainActivity) requireActivity()).getSearchView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                kino_adapter.getFilter().filter(newText);
                return false;
            }
        });
        searchView.setVisibility(View.VISIBLE);

        createListView(LIST_VIEW_STATE, view.getContext());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!item.hasSubMenu()) {
            createListView(item.getItemId(), requireContext());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        for(Disposable disposable : disposables) {
            disposable.dispose();
        }
    }

    private void createListView(int orderId, Context context) {
        if (getKinoList() != null) {
            LIST_VIEW_STATE = orderId;

            disposables.add(
                    getResults(orderId)
                            .observeOn(mainThread())
                            .subscribeOn(Schedulers.io())
                            .doOnError(
                                    (error) ->
                                            Toast.makeText(
                                                    getContext(),
                                                    getString(R.string.list_loading_failed),
                                                    Toast.LENGTH_LONG
                                            ).show())
                            .subscribe((kinos) -> {
                                initialiseAdapter(kinos, orderId, context);
                                applyListeners();

                                getKinoList().setAdapter(kino_adapter);
                            })
            );
        }
    }

    private void applyListeners() {
        getKinoList().setOnItemLongClickListener((view, parent, position, rowId) -> {
            Object item = kino_adapter.getItem(position);
            if (!(item instanceof KinoDto)) {
                return false;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setMessage(R.string.delete_kino_dialog)
                    .setPositiveButton(R.string.yes, (dialog, id) -> {
                        if (item instanceof KinoDto) {
                            kino_adapter.remove(item);
                            //noinspection unchecked
                            service.delete((KinoDto) item);

                            kino_adapter.notifyDataSetInvalidated();
                        }
                    })
                    .setNegativeButton(R.string.cancel, (dialog, id) -> {
                        // User cancelled the dialog
                    });
            builder.create().show();
            return true;
        });
        getKinoList().setOnItemClickListener((view, parent, position, rowId) -> {
            Object item = kino_adapter.getItem(position);
            if (!(item instanceof KinoDto)) {
                return;
            }
            // TODO callback ?
            navigateToItem((KinoDto) item, position, true, false);
        });
    }

    protected abstract void navigateToItem(KinoDto item, int position, boolean inDb, boolean fromSearch);

    @NonNull
    private void initialiseAdapter(List<KinoDto> kinos, int orderId, Context context) {
        List<Object> objects = new ArrayList<>(kinos);
        if (orderId == R.id.order_by_date_added_newest_first || orderId == R.id.order_by_date_added_oldest_first) {
            objects = new ReviewDateHeaderListTransformer(context, kinos).transform();
        }

        kino_adapter = new ReviewListAdapter(context, objects);
    }

    protected abstract int getOrderFromPreferences();

    protected int getOrderFromPreferences(String arrayKey) {
        String defaultSortType = new PreferencesWrapper().getStringPreference(
                getContext(),
                arrayKey,
                null
        );

        return defaultSortType == null ? LIST_VIEW_STATE :
                getResources().getIdentifier(
                        defaultSortType, "id", getContext().getPackageName()
                );
    }

    protected Flowable<List<KinoDto>> getResults(int order) {
        if (order == -1) {
            order = getOrderFromPreferences();
        }

        Flowable<List<KinoDto>> flowableDtos;
        switch (order) {
            case R.id.order_by_title_asc:
                flowableDtos = service.findByTitle(true);
                break;
            case R.id.order_by_title_desc:
                flowableDtos = service.findByTitle(false);
                break;
            case R.id.order_by_date_added_newest_first:
                return service.findByReviewDate(false);
            case R.id.order_by_date_added_oldest_first:
                return service.findByReviewDate(true);
            case R.id.order_by_rating_highest_first:
                flowableDtos = service.findByRating(false);
                break;
            case R.id.order_by_rating_lowest_first:
                flowableDtos = service.findByRating(true);
                break;
            case R.id.order_by_year_newest_first:
                return service.findByYear(false);
            case R.id.order_by_year_oldest_first:
                return service.findByYear(true);
            default:
                flowableDtos = service.findAll();
                break;
        }

        return flowableDtos;
    }

    protected abstract ListView getKinoList();

    protected abstract void onFabClick();

    protected abstract void createService();
}
