package com.ulicae.cinelog.room.fragments.wishlist.item;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.android.v2.fragments.ShareableFragment;
import com.ulicae.cinelog.room.dto.KinoDto;
import com.ulicae.cinelog.room.dto.SerieDto;
import com.ulicae.cinelog.room.dto.data.WishlistDataDto;
import com.ulicae.cinelog.room.dto.data.WishlistItemType;
import com.ulicae.cinelog.databinding.LayoutKinoItemBinding;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.services.WishlistAsyncService;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WishlistItemRoomFragment extends ShareableFragment<WishlistDataDto> {

    private List<Disposable> disposables;
    private LayoutKinoItemBinding binding;

    private WishlistAsyncService wishlistAsyncService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.addOptionMenu();
        this.disposables = new ArrayList<>();
        binding = LayoutKinoItemBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        AppDatabase appDb = ((KinoApplication) getActivity().getApplication()).getDb();

        wishlistAsyncService = new WishlistAsyncService(appDb);

        Long itemId = requireArguments().getLong("wishlistItemId");
        if (itemId != null && !itemId.equals(0L)) {
            this.disposables.add(
                    this.fetchWishlistItem(itemId)
                    .subscribe((item) -> {
                        this.item = item;
                        initItem();
                    })
            );
        } else {
            this.item = Parcels.unwrap(requireArguments().getParcelable("dataDto"));
            initItem();
        }

        ((MainActivity) requireActivity()).getSearchView().setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
    }

    private Flowable<WishlistDataDto> fetchWishlistItem(long itemId) {
        return wishlistAsyncService.findById(itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void initItem() {
        this.initFields();
        this.initFabButton();

        String tmdbType = item.getWishlistItemType() == WishlistItemType.SERIE ? "tv" : "movie";
        setLinkBaseUrl(String.format("https://www.themoviedb.org/%s/", tmdbType));
    }

    private void initFields() {
        if (item.getPosterPath() != null && !"".equals(item.getPosterPath())) {
            Glide.with(requireContext())
                    .load("https://image.tmdb.org/t/p/w185" + item.getPosterPath())
                    .centerCrop()
                    .crossFade()
                    .into(binding.viewKinoTmdbImageLayout);
        }

        // TODO extract it in a helper
        String releaseDateLocal = item.getReleaseDate();
        if (releaseDateLocal != null && !"".equals(releaseDateLocal)) {
            SimpleDateFormat frenchSdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
            try {
                Date parsedDate = frenchSdf.parse(releaseDateLocal);
                String formattedDate = DateFormat.getDateFormat(requireActivity().getBaseContext()).format(parsedDate);
                binding.viewKinoTmdbYear.setText(formattedDate);
            } catch (ParseException ignored) {
                binding.viewKinoTmdbYear.setText(String.valueOf(item.getFirstYear()));
            }
        }

        binding.viewKinoTmdbOverview.setText(item.getOverview());
        binding.viewKinoTmdbTitle.setText(item.getTitle());
    }

    private void initFabButton() {
        FloatingActionButton fab = ((MainActivity) requireActivity()).getFab();

        fab.setOnClickListener(this::onFabClick);
        // TODO change icon if review exists
        if (item.getId() == null) {
            fab.setImageResource(R.drawable.add_review);
        } else {
            fab.setImageResource(R.drawable.add_kino);
        }
        fab.show();
    }

    public void onFabClick(View view) {
        if (item.getId() == null) {
            addToWishlist();
        } else {
            createReview();
        }
        // TODO process with review open case
    }

    private void addToWishlist() {
        wishlistAsyncService.createOrUpdate(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        Toast.makeText(requireContext(), getString(R.string.wishlist_item_added), Toast.LENGTH_LONG).show();

        ((MainActivity) requireActivity()).navigateBackToWishlist(item.getWishlistItemType());
    }

    private void createReview() {
        KinoDto dto;
        if (item.getWishlistItemType() == WishlistItemType.SERIE) {
            dto = new SerieDto(
                    null,
                    item.getTmdbId() != null ? item.getTmdbId().longValue() : null,
                    null,
                    item.getTitle(),
                    null,
                    null,
                    null,
                    null,
                    item.getPosterPath(),
                    item.getOverview(),
                    item.getFirstYear(),
                    item.getReleaseDate(),
                    new ArrayList<>(),
                    new ArrayList<>()
            );
        } else if (item.getWishlistItemType() == WishlistItemType.MOVIE) {
            dto = new KinoDto(
                    null,
                    item.getTmdbId() != null ? item.getTmdbId().longValue() : null,
                    item.getTitle(),
                    null,
                    null,
                    null,
                    null,
                    item.getPosterPath(),
                    item.getOverview(),
                    item.getFirstYear(),
                    item.getReleaseDate(),
                    new ArrayList<>()
            );
        } else {
            Toast.makeText(requireContext(), getString(R.string.wishlist_cant_create_review), Toast.LENGTH_LONG).show();
            return;
        }

        Bundle args = new Bundle();
        args.putString("dtoType", dto instanceof SerieDto ? "serie" : "kino");
        args.putParcelable("kino", Parcels.wrap(dto));
        args.putBoolean("creation", true);
        args.putLong("wishlistId", item.getId());
        ((MainActivity) requireActivity()).navigateToReview(
                R.id.action_wishlistItemRoomFragment_to_editReviewFragment,
                args
        );
    }

  /*  TODO rewrite state management to get right data from editreview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ADD_REVIEW) {
            if (resultCode == Activity.RESULT_OK) {
                item = Parcels.unwrap(data.getParcelableExtra("kino"));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}