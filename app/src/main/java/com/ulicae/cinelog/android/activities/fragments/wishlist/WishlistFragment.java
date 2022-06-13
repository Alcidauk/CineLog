package com.ulicae.cinelog.android.activities.fragments.wishlist;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.view.ViewDataActivity;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.services.wishlist.WishlistService;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WishlistFragment extends Fragment {

    @BindView(R.id.kino_list)
    ListView kino_list;

    WishlistListAdapter listAdapter;

    List<WishlistDataDto> dataDtos;

    protected WishlistService service;

    static final int RESULT_VIEW_KINO = 4;

    private int LIST_VIEW_STATE = 1;

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

    protected void createListView(int orderId) {
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
                    startActivityForResult(intent, RESULT_VIEW_KINO);
                }
            });

            kino_list.setAdapter(listAdapter);
        }
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
