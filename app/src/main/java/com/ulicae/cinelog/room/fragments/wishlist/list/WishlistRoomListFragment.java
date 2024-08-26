package com.ulicae.cinelog.room.fragments.wishlist.list;

import static io.reactivex.rxjava3.schedulers.Schedulers.io;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.databinding.FragmentRoomListBinding;
import com.ulicae.cinelog.room.entities.ItemEntityType;
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
public class WishlistRoomListFragment extends Fragment {

    WishlistListRoomAdapter listAdapter;

    List<WishlistDataDto> dataDtos;

    private WishlistAsyncService service;

    private FragmentRoomListBinding binding;

    protected ItemEntityType itemEntityType;

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        disposables = new ArrayList<>();
        service = new WishlistAsyncService(((KinoApplication) getActivity().getApplication()).getDb());

        createListView(1);

        FloatingActionButton fab = ((MainActivity) requireActivity()).getFab();
        fab.setOnClickListener(v -> onFabClick());
        fab.setImageResource(R.drawable.add_kino);

        SearchView searchView = ((MainActivity) requireActivity()).getSearchView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapter.getFilter().filter(newText);
                return false;
            }
        });
        searchView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!item.hasSubMenu()) {
            createListView(item.getItemId());
        }

        return super.onOptionsItemSelected(item);
    }

    protected void createListView(int orderId) {
        if (getWishlistItemList() != null) {
            Disposable findDisposable = service.findAllForType(itemEntityType)
                    .subscribeOn(io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext((wishlistItems) -> {
                        this.dataDtos = wishlistItems;

                        listAdapter = new WishlistListRoomAdapter(requireContext(), dataDtos);
                        getWishlistItemList().setAdapter(listAdapter);
                    })
                    .subscribe();

            disposables.add(findDisposable);

            getWishlistItemList().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(final AdapterView<?> view, View parent, final int position, long rowId) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setMessage(R.string.delete_kino_dialog)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Delete the kino
                                    WishlistDataDto wishlistDataDto = listAdapter.getItem(position);
                                    listAdapter.remove(wishlistDataDto);

                                    Disposable removalDisposable = service.delete(wishlistDataDto)
                                            .subscribe(() -> listAdapter.notifyDataSetChanged());
                                    disposables.add(removalDisposable);
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

            getWishlistItemList().setOnItemClickListener((view, parent, position, rowId) -> {
                ((MainActivity) requireActivity()).navigateToWishlistItem(
                        listAdapter.getItem(position),
                        R.id.action_nav_wishlist_room_to_wishlistItemFragment
                );
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRoomListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    protected void onFabClick() {
        if (itemEntityType == ItemEntityType.MOVIE) {
            ((MainActivity) requireActivity()).goToTmdbMovieSearch(true);
        } else {
            ((MainActivity) requireActivity()).goToTmdbSerieSearch(true);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_simple, menu);
    }

    protected ListView getWishlistItemList() {
        return binding != null ? binding.itemList : null;
    }

    private List<Disposable> disposables;

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (this.disposables != null) {
            for (Disposable disposable : disposables) {
                disposable.dispose();
            }
        }
    }

}

