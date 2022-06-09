package com.ulicae.cinelog.android.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.view.ViewDataActivity;
import com.ulicae.cinelog.data.dto.TagDto;

import org.parceler.Parcels;

import java.util.List;

/**
 * CineLog Copyright 2019 Pierre Rognon
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

    TagListAdapter(@NonNull Context context, @NonNull List<TagDto> objects) {
        super(context, R.layout.tag_item, objects);
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

        TextView tagNameTextView = (TextView) convertView.findViewById(R.id.tag_name);
        View tagColorLayout = (View) convertView.findViewById(R.id.tag_color);

        TagDto dataDto = getItem(position);
        if (dataDto != null) {
            tagNameTextView.setText(dataDto.getName());
            tagColorLayout.setBackgroundColor(Color.parseColor(dataDto.getColor()));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditTag.class);
                intent.putExtra("tag", Parcels.wrap(dataDto));

                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

}
