package com.ulicae.cinelog.android.v2.fragments.wishlist.item;

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
import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.data.services.wishlist.MovieWishlistService;
import com.ulicae.cinelog.data.services.wishlist.SerieWishlistService;
import com.ulicae.cinelog.databinding.LayoutKinoItemBinding;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WishlistItemFragment extends ShareableFragment<WishlistDataDto> {

    private LayoutKinoItemBinding binding;

    private SerieWishlistService serieWishlistService;
    private MovieWishlistService movieWishlistService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.addOptionMenu();
        binding = LayoutKinoItemBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        DaoSession daoSession = ((KinoApplication) requireActivity().getApplicationContext()).getDaoSession();
        serieWishlistService = new SerieWishlistService(daoSession);
        movieWishlistService = new MovieWishlistService(daoSession);

        item = Parcels.unwrap(requireArguments().getParcelable("dataDto"));

        String tmdbType =  item.getWishlistItemType() == WishlistItemType.SERIE ? "tv" : "movie";
        setLinkBaseUrl(String.format("https://www.themoviedb.org/%s/", tmdbType));

        initFabButton();
        initFields();

        ((MainActivity) requireActivity()).getSearchView().setVisibility(View.GONE);
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
        if (item.getWishlistItemType() == WishlistItemType.SERIE) {
            serieWishlistService.createSerieData(item);
            Toast.makeText(requireContext(), getString(R.string.wishlist_item_added), Toast.LENGTH_LONG).show();
        } else if (item.getWishlistItemType() == WishlistItemType.MOVIE) {
            movieWishlistService.createMovieData(item);
            Toast.makeText(requireContext(), getString(R.string.wishlist_item_added), Toast.LENGTH_LONG).show();
        }

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
                R.id.action_wishlistItemFragment_to_editReviewFragment,
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