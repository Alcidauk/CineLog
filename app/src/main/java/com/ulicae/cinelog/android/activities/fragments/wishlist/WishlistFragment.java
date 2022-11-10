package com.ulicae.cinelog.android.activities.fragments.wishlist;

import android.content.DialogInterface;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.services.wishlist.WishlistService;

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
public abstract class WishlistFragment extends Fragment {

    WishlistListAdapter listAdapter;

    List<WishlistDataDto> dataDtos;

    protected WishlistService service;

    private int LIST_VIEW_STATE = 1;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!item.hasSubMenu()) {
            createListView(item.getItemId());
        }

        return super.onOptionsItemSelected(item);
    }

    protected void createListView(int orderId) {
        if (getKinoList() != null) {
            //date added
            dataDtos = getResults(orderId);

            LIST_VIEW_STATE = orderId;

            listAdapter = new WishlistListAdapter(getContext(), dataDtos);
            getKinoList().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
            getKinoList().setOnItemClickListener((view, parent, position, rowId) -> {
                ((MainActivity) requireActivity()).navigateToWishlistItem(dataDtos.get(position));
            });

            getKinoList().setAdapter(listAdapter);
        }
    }

    protected abstract ListView getKinoList();

    protected List<WishlistDataDto> getResults(int order) {
        List<WishlistDataDto> fetchedDtos;
        fetchedDtos = service.getAll();

        return new ArrayList<>(fetchedDtos);
    }
}
