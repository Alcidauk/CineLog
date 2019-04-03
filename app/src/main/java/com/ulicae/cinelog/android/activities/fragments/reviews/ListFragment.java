package com.ulicae.cinelog.android.activities.fragments.reviews;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.ViewKino;
import com.ulicae.cinelog.data.services.reviews.DataService;
import com.ulicae.cinelog.data.dto.KinoDto;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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
public abstract class ListFragment extends Fragment {

    @BindView(R.id.kino_list)
    ListView kino_list;

    KinoListAdapter kino_adapter;

    List<KinoDto> kinos;

    protected DataService service;

    private static final int RESULT_ADD_KINO = 2;
    static final int RESULT_VIEW_KINO = 4;

    private int LIST_VIEW_STATE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        createService();

        createListView(1);
    }

    protected abstract void createService();

    @Override
    public void onStart() {
        super.onStart();
        createListView(LIST_VIEW_STATE);
        System.out.println("onStart");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_ADD_KINO) {
            if (resultCode == Activity.RESULT_OK) {
                System.out.println("Result Ok");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("Result Cancelled");
            }
        }
        if (requestCode == RESULT_VIEW_KINO) {
            if (resultCode == Activity.RESULT_OK) {
                int pos = data.getIntExtra("kino_position", -1);

                kinos.set(pos, (KinoDto) Parcels.unwrap(data.getParcelableExtra("kino")));
                kino_adapter.notifyDataSetChanged();
                System.out.println("Result Ok");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("Result Cancelled");
            }
        }
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
            kinos = getResults(orderId);

            LIST_VIEW_STATE = orderId;

            final List<Object> objects = initialiseAdapter(orderId);
            kino_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(final AdapterView<?> view, View parent, final int position, long rowId) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(R.string.delete_kino_dialog)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Object item = objects.get(position);
                                    if(item instanceof KinoDto) {
                                        objects.remove(position);
                                        //noinspection unchecked
                                        service.delete((KinoDto) item);

                                        kino_adapter.notifyDataSetInvalidated();
                                    }
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
                    Object item = objects.get(position);
                    if(item instanceof KinoDto) {
                        Intent intent = new Intent(view.getContext(), ViewKino.class);
                        intent.putExtra("kino", Parcels.wrap(item));
                        intent.putExtra("kino_position", position);
                        intent.putExtra("dtoType", getDtoType());
                        startActivityForResult(intent, RESULT_VIEW_KINO);
                    }
                }
            });


            kino_list.setAdapter(kino_adapter);
        }
    }

    @NonNull
    private List<Object> initialiseAdapter(int orderId) {
        List<Object> objects = new ArrayList<Object>(kinos);
        if(orderId == R.id.order_by_date_added_newest_first || orderId == R.id.order_by_date_added_oldest_first) {
           objects = new ReviewDateHeaderListTransformer(getContext(), kinos).transform();
        }

        kino_adapter = new KinoListAdapter(getContext(), objects);
        return objects;
    }

    protected abstract String getDtoType();

    protected abstract List<KinoDto> getResults(int order);
}
