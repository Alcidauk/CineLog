package com.ulicae.cinelog.android.v2.fragments.review.edit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.services.tags.TagService;

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
public class TagChooserDialog extends DialogFragment {

    private final TagService tagService;
    private final KinoDto kinoDto;

    boolean[] selectedTags;
    List<TagDto> allTags = new ArrayList<>();

    public TagChooserDialog(TagService tagService, KinoDto kinoDto) {
        this.tagService = tagService;
        this.kinoDto = kinoDto;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.populateTagList();

        List<String> allTagNames = new ArrayList<>();
        for (TagDto dto : allTags) {
            allTagNames.add(dto.getName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.tags_title);

        if (allTags.size() == 0) {
            builder.setMessage(R.string.no_tags_found_label)
                    .setNegativeButton(R.string.go_back, (dialog, id) -> nothingToDo());
        } else {
            builder.setMultiChoiceItems(
                            allTagNames.toArray(new CharSequence[allTagNames.size()]),
                            selectedTags,
                            (dialog, which, isChecked) -> selectedTags[which] = isChecked)
                    .setPositiveButton(R.string.validate, (dialog, id) -> nothingToDo());
        }

        return builder.create();

    }

    private void populateTagList() {
        allTags = getTags();
        selectedTags = new boolean[allTags.size()];

        for (int i = 0; i < allTags.size(); i++) {
            List<TagDto> kinoTags = kinoDto.getTags();
            selectedTags[i] = kinoTags != null && kinoTags.contains(allTags.get(i));
        }
    }

    private List<TagDto> getTags() {
        return kinoDto instanceof SerieDto ? tagService.getSeriesTags() : tagService.getMovieTags();
    }

    private void nothingToDo() {
    }
}

