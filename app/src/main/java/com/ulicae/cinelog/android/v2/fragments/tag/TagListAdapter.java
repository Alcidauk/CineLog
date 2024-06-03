package com.ulicae.cinelog.android.v2.fragments.tag;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.services.tags.room.TagAsyncService;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
class TagListAdapter extends ArrayAdapter<TagDto> {

    private final TagAsyncService tagService;
    private final MainActivity activity;

    TagListAdapter(@NonNull Context context, @NonNull List<TagDto> objects, TagAsyncService tagService, MainActivity activity) {
        super(context, R.layout.tag_item, objects);
        this.tagService = tagService;
        this.activity = activity;
    }

    public long getItemId(int position) {
        return 0;
    }

    // createOrUpdate a new RelativeView for each item referenced by the Adapter
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag_item, parent, false);
        }

        TextView tagNameTextView = convertView.findViewById(R.id.tag_name);
        View tagColorLayout = convertView.findViewById(R.id.tag_color);

        TagDto dataDto = getItem(position);
        if (dataDto != null) {
            tagNameTextView.setText(dataDto.getName());
            tagColorLayout.setBackgroundColor(Color.parseColor(dataDto.getColor()));
        }

        convertView.setOnClickListener(v -> {
            activity.goToTagEdition(dataDto);
        });

        convertView.setOnLongClickListener(v -> {
            removeTag(dataDto);
            return true;
        });


        return convertView;
    }

    private void removeTag(TagDto dataDto) {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.delete_tag_dialog)
                .setPositiveButton(R.string.yes, (dialog, id) -> {
                    tagService.delete(dataDto)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> {
                                        Toast.makeText(getContext(), R.string.tag_deleted, Toast.LENGTH_SHORT).show();
                                    });

                    remove(dataDto);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                })
                .create()
                .show();
    }

}
