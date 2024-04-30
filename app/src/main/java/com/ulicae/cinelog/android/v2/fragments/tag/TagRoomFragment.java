package com.ulicae.cinelog.android.v2.fragments.tag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.services.tags.room.TagAsyncService;
import com.ulicae.cinelog.databinding.FragmentTagListBinding;
import com.ulicae.cinelog.room.AppDatabase;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

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
public class TagRoomFragment extends Fragment {

    private FragmentTagListBinding binding;

    private TagAsyncService service;

    TagListAdapter listAdapter;

    CompositeDisposable disposable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTagListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        AppDatabase db = ((MainActivity) getActivity()).getDb();

        service = new TagAsyncService(db);
        disposable = new CompositeDisposable();

        fetchAndSetTags();

        FloatingActionButton fab = ((MainActivity) requireActivity()).getFab();
        fab.setOnClickListener(v -> ((MainActivity) requireActivity()).goToTagEdition(null));
        fab.setImageResource(R.drawable.add_tag);

        // TODO implement search for tags
        ((MainActivity) requireActivity()).getSearchView().setVisibility(View.GONE);
    }

    private void fetchAndSetTags() {
        disposable.add(
                service.findAll()
                        .subscribe(
                                tags -> {
                                    listAdapter = new TagListAdapter(requireContext(), tags, service, (MainActivity) requireActivity());
                                    binding.tagList.setAdapter(listAdapter);
                                }));
        // TODO gérer les erreurs throwable -> Log.e("coucou", "Unable to get tags", throwable)));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_simple, menu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        disposable.clear();
    }
}
