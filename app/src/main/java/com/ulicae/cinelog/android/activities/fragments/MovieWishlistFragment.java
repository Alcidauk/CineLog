package com.ulicae.cinelog.android.activities.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.view.ViewDataActivity;
import com.ulicae.cinelog.data.MovieDataService;
import com.ulicae.cinelog.data.SerieDataService;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * CineLog Copyright 2018 Pierre Rognon
 *
 *
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 *
 */
public class MovieWishlistFragment extends Fragment {

    @BindView(R.id.kino_list)
    ListView kino_list;

    WishlistListAdapter listAdapter;

    List<WishlistDataDto> dataDtos;

    protected MovieDataService service;

    static final int RESULT_VIEW_KINO = 4;

    private int LIST_VIEW_STATE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        service = new MovieDataService(((KinoApplication) getActivity().getApplication()).getDaoSession());

        createListView(1);
    }

    @Override
    public void onStart() {
        super.onStart();

        createListView(LIST_VIEW_STATE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!item.hasSubMenu()) {
            createListView(item.getItemId());
        }

        return super.onOptionsItemSelected(item);
    }

    private void createListView(int orderId) {
        if (kino_list != null) {
            //date added
            dataDtos = getResults(orderId);

            LIST_VIEW_STATE = orderId;

            listAdapter = new WishlistListAdapter(getContext(), dataDtos);
            kino_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(final AdapterView<?> view, View parent, final int position, long rowId) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(R.string.delete_kino_dialog)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Delete the kino
                                    WishlistDataDto wishlistDataDto = dataDtos.get(position);
                                    dataDtos.remove(position);

                                    //noinspection unchecked
                                    service.delete(wishlistDataDto);

                                    listAdapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    builder.create().show();
                    return true;
                }
            });
            kino_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> view, View parent, final int position, long rowId) {
                    Intent intent = new Intent(getContext(), ViewDataActivity.class);
                    intent.putExtra("dataDto", Parcels.wrap(dataDtos.get(position)));
                    intent.putExtra("isWishlist", true);
                    startActivityForResult(intent, RESULT_VIEW_KINO);
                }
            });

            kino_list.setAdapter(listAdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // TODO right menu
        inflater.inflate(R.menu.menu_movie, menu);
    }

    protected List<WishlistDataDto> getResults(int order) {
        List<WishlistDataDto> fetchedDtos;
        switch (order) {
            default:
                fetchedDtos = service.getAll();
                break;
        }

        return new ArrayList<>(fetchedDtos);
    }

}
