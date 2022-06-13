package com.ulicae.cinelog.android.activities.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.services.tags.TagService;

import java.util.ArrayList;
import java.util.List;

/**
 * CineLog Copyright 2022 Pierre Rognon
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
        for(TagDto dto : allTags) {
            allTagNames.add(dto.getName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tags")
                .setMultiChoiceItems(
                        allTagNames.toArray(new CharSequence[allTagNames.size()]),
                        selectedTags,
                        (dialog, which, isChecked) -> selectedTags[which] = isChecked
                )
                .setPositiveButton("Valider", (dialog, id) -> updateTagJoin());

        return builder.create();

    }

    private void populateTagList() {
        allTags = getTags();
        selectedTags = new boolean[allTags.size()];

        for (int i = 0; i < allTags.size(); i++) {
            selectedTags[i] = kinoDto.getTags().contains(allTags.get(i));
        }
    }

    private List<TagDto> getTags() {
        return kinoDto instanceof SerieDto ? tagService.getSeriesTags() : tagService.getMovieTags();
    }

    private void updateTagJoin() {
        for (int i = 0; i < selectedTags.length; i++) {
            TagDto tag = allTags.get(i);
            if(selectedTags[i]){
                tagService.addTagToItemIfNotExists(tag, kinoDto);
                if(!kinoDto.getTags().contains(tag)) {
                    kinoDto.getTags().add(tag);
                }
            } else {
                tagService.removeTagFromItemIfExists(tag, kinoDto);
                kinoDto.getTags().remove(tag);
            }
        }
    }
}

