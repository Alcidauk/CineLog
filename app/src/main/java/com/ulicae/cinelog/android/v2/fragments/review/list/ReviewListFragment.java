package com.ulicae.cinelog.android.v2.fragments.review.list;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.dto.ItemDto;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.services.reviews.DataService;
import com.ulicae.cinelog.utils.PreferencesWrapper;

import java.util.ArrayList;
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
public abstract class ReviewListFragment<T extends ItemDto> extends Fragment {

    ReviewListAdapter kino_adapter;

    List<T> kinos;

    protected DataService service;

    private int LIST_VIEW_STATE = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        createService();
        createListView(1);
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
    }

    @Override
    public void onStart() {
        super.onStart();
        createListView(LIST_VIEW_STATE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!item.hasSubMenu()) {
            createListView(item.getItemId());
        }

        return super.onOptionsItemSelected(item);
    }

    private void createListView(int orderId) {
        if (getKinoList() != null) {
            LIST_VIEW_STATE = orderId;

            kinos = getResults(orderId);

            initialiseAdapter(kinos, orderId);
            applyListeners();

            getKinoList().setAdapter(kino_adapter);
        }
    }

    private void applyListeners() {
        getKinoList().setOnItemLongClickListener((view, parent, position, rowId) -> {
            Object item = kino_adapter.getItem(position);
            if(!(item instanceof KinoDto)){
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
            if(!(item instanceof KinoDto)){
                return;
            }
            // TODO callback ?
            ((MainActivity) requireActivity()).navigateToItem(
                    (KinoDto) item, position, true, false
            );
        });
    }

    @NonNull
    private void initialiseAdapter(List<T> kinos, int orderId) {
        List<Object> objects = new ArrayList<>(kinos);
        if (orderId == R.id.order_by_date_added_newest_first || orderId == R.id.order_by_date_added_oldest_first) {
            objects = new ReviewDateHeaderListTransformer(getContext(), kinos).transform();
        }

        kino_adapter = new ReviewListAdapter(getContext(), objects);
    }

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

    protected abstract List<T> getResults(int order);

    protected abstract ListView getKinoList();

    protected abstract void onFabClick();

    protected abstract void createService();
}
