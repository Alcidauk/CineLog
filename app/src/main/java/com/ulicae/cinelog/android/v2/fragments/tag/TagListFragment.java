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
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.services.tags.TagService;
import com.ulicae.cinelog.databinding.FragmentTagListBinding;

import java.util.List;

public class TagListFragment extends Fragment {

    FragmentTagListBinding binding;

    TagListAdapter listAdapter;
    protected TagService service;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        binding = FragmentTagListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        service = new TagService(((KinoApplication) requireActivity().getApplication()).getDaoSession());

        fetchAndSetTags();

        FloatingActionButton fab = ((MainActivity) requireActivity()).getFab();
        fab.setOnClickListener(v -> ((MainActivity) requireActivity()).goToTagEdition(null));
        fab.setImageResource(R.drawable.add_tag);

        // TODO implement search for tags
        ((MainActivity) requireActivity()).getSearchView().setVisibility(View.GONE);
    }

    private void fetchAndSetTags() {
        List<TagDto> dataDtos = service.getAll();

        listAdapter = new TagListAdapter(requireContext(), dataDtos, service, (MainActivity) requireActivity());
        binding.tagList.setAdapter(listAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_simple, menu);
    }

}
