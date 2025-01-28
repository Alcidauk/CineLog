package com.ulicae.cinelog.android.v2.fragments.review.edit;

import static io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.databinding.FragmentReviewEditionBinding;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.dto.KinoDto;
import com.ulicae.cinelog.room.dto.SerieDto;
import com.ulicae.cinelog.room.dto.TagDto;
import com.ulicae.cinelog.room.services.AsyncServiceFactory;
import com.ulicae.cinelog.room.services.ReviewAsyncService;
import com.ulicae.cinelog.room.services.TagAsyncService;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ReviewEditionFragment extends Fragment {

    private FragmentReviewEditionBinding binding;

    KinoDto kino;

    private ReviewAsyncService reviewAsyncService;
    private TagAsyncService tagService;

    private WishlistItemDeleter wishlistItemDeleter;

    TagChooserDialog tagDialog;

    private List<Disposable> disposables;

    // TODO when this flag is true, on a return button touch, remove the review that was just created coming from a unregistered item
    private boolean creation;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewEditionBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AppDatabase appDb = ((KinoApplication) getActivity().getApplication()).getDb();

        wishlistItemDeleter = new WishlistItemDeleter(appDb);

        String dtoType = requireArguments().getString("dtoType");

        reviewAsyncService = (ReviewAsyncService) new AsyncServiceFactory().create(
                dtoType,
                ((KinoApplication) getActivity().getApplication())
        );

        tagService = new TagAsyncService(appDb);

        disposables = new ArrayList<>();

        kino = Parcels.unwrap(requireArguments().getParcelable("kino"));
        creation = requireArguments().getBoolean("creation");

        initRating();
        initReview();
        initKinoTitle();

        binding.reviewTagEdit.setOnClickListener(onReviewTagEdit());
        binding.kinoReviewDateButton.setOnClickListener(this::showTimePickerDialog);

        FloatingActionButton fab = ((MainActivity) requireActivity()).getFab();
        fab.setOnClickListener(fabView -> onFabClick());
        fab.setImageResource(R.drawable.save_kino);
        fab.show();

        ((MainActivity) requireActivity()).getSearchView().setVisibility(View.GONE);
    }

    private void initReview() {
        if (kino.getReview() != null) {
            binding.kinoReviewText.setText(kino.getReview());
        }

        if (kino.getReview_date() != null) {
            String review_date_as_string = null;
            if (kino.getReview_date() != null) {
                review_date_as_string = DateFormat.getDateFormat(requireContext()).format(kino.getReview_date());
            }
            binding.kinoReviewDateButton.setText(review_date_as_string);
        }
    }

    @NonNull
    private View.OnClickListener onReviewTagEdit() {
        return view -> {
            // TODO async instead of blocking first
            List<TagDto> tagList = kino instanceof SerieDto ? tagService.findSerieTags() : tagService.findMovieTags();

            tagDialog = new TagChooserDialog(kino, tagList);
            tagDialog.show(requireActivity().getSupportFragmentManager(), "NoticeDialogFragment");
        };
    }

    private void initRating() {
        int maxRating = getMaxRating();
        String[] displayedValues = getDisplayedValues(maxRating);

        initRatingPicker(maxRating, displayedValues);
        initDisplayedRating(displayedValues);
    }

    private int getMaxRating() {
        int maxRating;
        // TODO kino peut être null, parce qu'on est avec un item en base qu'on doit aller refetcher
        if (kino.getMaxRating() == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
            String defaultMaxRateValue = prefs.getString("default_max_rate_value", "5");
            maxRating = Integer.parseInt(defaultMaxRateValue);
        } else {
            maxRating = kino.getMaxRating();
        }
        return maxRating;
    }

    private void initRatingPicker(int maxRating, String[] displayedValues) {
        binding.ratingPicker.setMinValue(0);
        binding.ratingPicker.setMaxValue(maxRating * 2);

        binding.ratingPicker.setDisplayedValues(displayedValues);

        binding.ratingPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            String[] displayedValues1 = picker.getDisplayedValues();
            float rating = Float.parseFloat(displayedValues1[newVal]);

            binding.reviewEditRatingBarAsText.setText(String.format("%s", rating));
            kino.setRating(rating);
        });
    }

    private void initKinoTitle() {
        binding.viewKinoTitle.setText(kino.getTitle());
        binding.viewKinoTitle.setEnabled(kino.getTmdbKinoId() == null);
    }

    private void initDisplayedRating(String[] displayedValues) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());

        int maxRating;
        if (kino.getMaxRating() == null) {
            String defaultMaxRateValue = prefs.getString("default_max_rate_value", "5");
            maxRating = Integer.parseInt(defaultMaxRateValue);
        } else {
            maxRating = kino.getMaxRating();
        }

        if (kino.getRating() != null) {
            binding.ratingPicker.setValue(getValueToDisplay(displayedValues, kino.getRating()));
            binding.reviewEditRatingBarAsText.setText(String.format("%s", kino.getRating()));
        }

        binding.reviewEditRatingBarMaxAsText.setText(String.format("/%s", maxRating));
    }

    private int getValueToDisplay(String[] displayedValues, float rating) {
        int i = 0;
        for (String value : displayedValues) {
            if (Float.parseFloat(value) == rating) {
                return i;
            }

            i++;
        }

        return 0;
    }

    @NonNull
    private String[] getDisplayedValues(int maxRating) {
        List<String> displayedValues = new ArrayList<>();

        for (int i = 0; i <= maxRating; i++) {
            displayedValues.add(String.valueOf(i));

            if (i != maxRating) {
                displayedValues.add(i + ".5");
            }
        }

        return displayedValues.toArray(new String[0]);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showTimePickerDialog(View view) {
        new DatePickerFragment().show(getChildFragmentManager(), "timePicker");
    }

    public void onFabClick() {
        kino.setReview(binding.kinoReviewText.getText().toString());

        if (kino.getTmdbKinoId() == null) {
            kino.setTitle(binding.viewKinoTitle.getText().toString());
        }

        if (kino.getMaxRating() == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
            String maxRating = prefs.getString("default_max_rate_value", "5");
            int maxRatingAsInt = Integer.parseInt(maxRating);
            kino.setMaxRating(maxRatingAsInt);
        }

        disposables.add(
                reviewAsyncService.createOrUpdate(kino)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                (createdKino) -> {
                                    kino.setId(createdKino);

                                    long wishlistId = requireArguments().getLong("wishlistId", 0L);
                                    if (wishlistId != 0L) {
                                        // TODO do we need to know if it is a serie or a movie ?
                                        //  wishlistItemDeleter.deleteWishlistItem(wishlistId, requireArguments().getString("dtoType"));
                                        wishlistItemDeleter.deleteWishlistItem(wishlistId); // TODO adapter ça
                                    }

                                    updateTags();

                                    ((MainActivity) requireActivity()).navigateBackToReviewList(kino);
                                },
                                error -> {
                                    Toast.makeText(getContext(), getString(R.string.automatic_export_movie_toast), Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    private void updateTags() {
        if (tagDialog == null) return;

        this.disposables.add(Observable.just(tagDialog)
                .subscribeOn(Schedulers.io())
                .subscribe(tagChooserDialog -> {
                    for (int i = 0; i < tagChooserDialog.selectedTags.length; i++) {
                        TagDto tag = tagChooserDialog.allTags.get(i);

                        if (tagChooserDialog.selectedTags[i]) {
                            tagService.addTagToItemIfNotExists(
                                            Math.toIntExact(kino.getId()),
                                            Math.toIntExact(tag.getId()))
                                    .observeOn(mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .doOnError((error) -> {
                                        Toast.makeText(getContext(), getString(R.string.cant_link_tag_to_review), Toast.LENGTH_SHORT).show();
                                    })
                                    .subscribe();

                            if (kino.getTags() == null) {
                                kino.setTags(new ArrayList<>());
                            }

                            if (!kino.getTags().contains(tag)) {
                                kino.getTags().add(tag);
                            }
                        } else {
                            tagService.removeTagFromItemIfExists(
                                            Math.toIntExact(kino.getId()), Math.toIntExact(tag.getId()))
                                    .observeOn(mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .doOnError((error) -> {
                                        Toast.makeText(getContext(), getString(R.string.cant_unlink_tag_to_review), Toast.LENGTH_SHORT).show();
                                    })
                                    .subscribe();

                            kino.getTags().remove(tag);
                        }
                    }
                }));

        // TODO avoid call this a second time. For now, it is used to refresh kino tags
        // kino = (KinoDto) dtoService.createOrUpdate(kino);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            final Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);

            KinoDto kino = ((ReviewEditionFragment) requireParentFragment()).kino;
            kino.setReview_date(c.getTime());
            String review_date_as_string = null;
            if (kino.getReview_date() != null) {
                review_date_as_string = DateFormat.getDateFormat(requireContext()).format(kino.getReview_date());
            }
            ((ReviewEditionFragment) requireParentFragment()).binding.kinoReviewDateButton.setText(review_date_as_string);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.disposables != null) {
            this.disposables.clear();
        }
    }
}
