package com.ulicae.cinelog.android.v2.fragments.review.list;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.services.reviews.DataService;
import com.ulicae.cinelog.utils.PreferencesWrapper;

import org.parceler.Parcels;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
public abstract class ReviewListFragment extends Fragment {

    ReviewListAdapter kino_adapter;

    List<KinoDto> kinos;

    protected DataService service;

    private static final int RESULT_ADD_KINO = 2;
    static final int RESULT_VIEW_KINO = 4;

    private int LIST_VIEW_STATE = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        createService();

        createListView(1);

        // TODO get review creation result
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                String result = bundle.getString("bundleKey");
                // Do something with the result
            }
        });

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
        super.onActivityResult(requestCode, resultCode, data);
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
        if (getKinoList() != null) {
            LIST_VIEW_STATE = orderId;

            kinos = getResults(orderId);

            final List<Object> objects = initialiseAdapter(orderId);
            getKinoList().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
            getKinoList().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> view, View parent, final int position, long rowId) {
                    Object item = objects.get(position);
                    // TODO callback ?
                    ((MainActivity) requireActivity()).navigateToItem(
                            (KinoDto) item, position, true, false
                    );
                }
            });


            getKinoList().setAdapter(kino_adapter);
        }
    }

    private int getThemeId() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> wrapper = Context.class;
        Method method = wrapper.getMethod("getThemeResId");
        method.setAccessible(true);
        return (Integer) method.invoke(getActivity());
    }

    @NonNull
    private List<Object> initialiseAdapter(int orderId) {
        List<Object> objects = new ArrayList<Object>(kinos);
        if(orderId == R.id.order_by_date_added_newest_first || orderId == R.id.order_by_date_added_oldest_first) {
           objects = new ReviewDateHeaderListTransformer(getContext(), kinos).transform();
        }

        kino_adapter = new ReviewListAdapter(getContext(), objects);
        return objects;
    }

    protected abstract String getDtoType();

    protected abstract List<KinoDto> getResults(int order);

    protected abstract ListView getKinoList();

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
}