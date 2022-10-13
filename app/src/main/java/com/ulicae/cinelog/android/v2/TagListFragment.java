package com.ulicae.cinelog.android.v2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.TagsActivity;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.services.tags.TagService;
import com.ulicae.cinelog.databinding.ActivityTagsBinding;

import java.util.List;

public class TagListFragment extends Fragment {

    ActivityTagsBinding binding;

    TagListAdapter listAdapter;
    protected TagService service;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        binding = ActivityTagsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        service = new TagService(((KinoApplication) requireActivity().getApplication()).getDaoSession());

        binding.fabTags.setOnClickListener(v -> ((TagsActivity) requireActivity()).goToTagEdition());

        initToolbar();
        initNavigation();
        fetchAndSetTags();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_tags, menu);
    }

    private void initToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.tagsToolbar.toolbar);
        ActionBar actionbar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.menu);
            actionbar.setTitle(R.string.tags_title);
            actionbar.setSubtitle(R.string.app_name);
        }
    }

    private void fetchAndSetTags() {
        List<TagDto> dataDtos = service.getAll();

        listAdapter = new TagListAdapter(requireContext(), dataDtos, service);
        binding.tagList.setAdapter(listAdapter);
    }

    private void initNavigation() {
        binding.navView.setCheckedItem(R.id.nav_tags);
        binding.navView.setNavigationItemSelectedListener(this::navigateToActivity);
    }

    private boolean navigateToActivity(MenuItem menuItem) {
        binding.drawerLayout.closeDrawers();

        if (menuItem.getItemId() == R.id.nav_reviews) {
            ((TagsActivity) requireActivity()).goToReviews();
        } else if (menuItem.getItemId() == R.id.nav_wishlist) {
            ((TagsActivity) requireActivity()).goToWishlist();
        } else {
            return false;
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_export) {
            ((TagsActivity) requireActivity()).goToExport();
        } else if (item.getItemId() == R.id.action_import) {
            ((TagsActivity) requireActivity()).goToImport();
        } else if (item.getItemId() == R.id.action_settings) {
            ((TagsActivity) requireActivity()).goToSettings();
        } else if (item.getItemId() == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
