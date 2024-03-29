package com.ulicae.cinelog.android.v2.fragments.tag;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.services.tags.TagService;
import com.ulicae.cinelog.databinding.FragmentEditTagBinding;

import org.parceler.Parcels;

public class EditTagFragment extends Fragment {

    private FragmentEditTagBinding binding;

    TagDto tag;
    private TagService tagDtoService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        binding = FragmentEditTagBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        tagDtoService = new TagService(((KinoApplication) requireActivity().getApplication()).getDaoSession());

        tag = Parcels.unwrap(requireArguments().getParcelable("tag"));
        if (tag == null) {
            createNewTag();
        } else {
            bindExistingTag();
        }

        ((MainActivity) requireActivity()).getFab().setOnClickListener(fabView -> onFabClick());
        ((MainActivity) requireActivity()).getSearchView().setVisibility(View.GONE);

        binding.tagFilms.setOnCheckedChangeListener((compoundButton, b) -> onFilmsCheckedChanged(b));
        binding.tagSeries.setOnCheckedChangeListener((compoundButton, b) -> onSeriesCheckedChanged(b));
        binding.tagColorUpdate.setOnClickListener(this::onTagColorUpdate);

        fetchColor();
    }

    private void bindExistingTag() {
        binding.tagName.setText(tag.getName());
        binding.tagFilms.setChecked(tag.isForMovies());
        binding.tagSeries.setChecked(tag.isForSeries());
    }

    @SuppressLint("ResourceType")
    private void createNewTag() {
        tag = new TagDto();
        tag.setColor(getString(R.color.colorPrimary));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                requireActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onFilmsCheckedChanged(boolean checked) {
        tag.setForMovies(checked);
    }

    public void onSeriesCheckedChanged(boolean checked) {
        tag.setForSeries(checked);
    }

    private void fetchColor() {
        if (tag.getColor() != null) {
            binding.tagColorCurrent.setBackgroundColor(Color.parseColor(tag.getColor()));
        }
    }

    public void onTagColorUpdate(View view) {
        new ColorPickerDialog.Builder(requireContext())
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(getString(R.string.save),
                        (ColorEnvelopeListener) (envelope, fromUser) -> {
                            tag.setColor("#" + envelope.getHexCode().substring(2));
                            fetchColor();
                        })
                .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss())
                .attachAlphaSlideBar(false)
                .attachBrightnessSlideBar(false)
                .setBottomSpace(12)
                .show();
    }

    public void onFabClick() {
        tag.setName(binding.tagName.getText().toString());

        if (tag.getName() == null || tag.getName().isEmpty()) {
            Toast.makeText(
                            requireContext(),
                            requireContext().getString(R.string.edit_tag_name_not_filled),
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        tagDtoService.createOrUpdate(tag);

        ((MainActivity) requireActivity()).navigateBack();
    }
}
