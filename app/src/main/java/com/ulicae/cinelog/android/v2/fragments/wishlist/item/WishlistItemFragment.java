package com.ulicae.cinelog.android.v2.fragments.wishlist.item;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
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

public class WishlistItemFragment extends Fragment {

    private LayoutKinoItemBinding binding;

    private WishlistDataDto wishlistDataDto;

    private SerieWishlistService serieWishlistService;
    private MovieWishlistService movieWishlistService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = LayoutKinoItemBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        DaoSession daoSession = ((KinoApplication) requireActivity().getApplicationContext()).getDaoSession();
        serieWishlistService = new SerieWishlistService(daoSession);
        movieWishlistService = new MovieWishlistService(daoSession);

        wishlistDataDto = Parcels.unwrap(requireArguments().getParcelable("dataDto"));

        initFabButton();
        initFields();
    }

    private void initFields() {
        if (wishlistDataDto.getPosterPath() != null && !"".equals(wishlistDataDto.getPosterPath())) {
            Glide.with(requireContext())
                    .load("https://image.tmdb.org/t/p/w185" + wishlistDataDto.getPosterPath())
                    .centerCrop()
                    .crossFade()
                    .into(binding.viewKinoTmdbImageLayout);
        }

        // TODO extract it in a helper
        String releaseDateLocal = wishlistDataDto.getReleaseDate();
        if (releaseDateLocal != null && !"".equals(releaseDateLocal)) {
            SimpleDateFormat frenchSdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
            try {
                Date parsedDate = frenchSdf.parse(releaseDateLocal);
                String formattedDate = DateFormat.getDateFormat(requireActivity().getBaseContext()).format(parsedDate);
                binding.viewKinoTmdbYear.setText(formattedDate);
            } catch (ParseException ignored) {
                binding.viewKinoTmdbYear.setText(String.valueOf(wishlistDataDto.getFirstYear()));
            }
        }

        binding.viewKinoTmdbOverview.setText(wishlistDataDto.getOverview());
        binding.viewKinoTmdbTitle.setText(wishlistDataDto.getTitle());
    }

    private void initFabButton() {
        FloatingActionButton fab = ((MainActivity) requireActivity()).getFab();
        fab.show();

        fab.setOnClickListener(this::onFabClick);
        // TODO change icon if review exists
        if (wishlistDataDto.getId() == null) {
            fab.setImageResource(R.drawable.add_review);
        } else {
            fab.setImageResource(R.drawable.add_kino);
        }
    }

    public void onFabClick(View view) {
        if (wishlistDataDto.getId() == null) {
            addToWishlist();
        } else {
            createReview();
        }
        // TODO process with review open case
    }

    private void addToWishlist() {
        if (wishlistDataDto.getWishlistItemType() == WishlistItemType.SERIE) {
            serieWishlistService.createSerieData(wishlistDataDto);
            Toast.makeText(requireContext(), getString(R.string.wishlist_item_added), Toast.LENGTH_LONG).show();
        } else if (wishlistDataDto.getWishlistItemType() == WishlistItemType.MOVIE) {
            movieWishlistService.createMovieData(wishlistDataDto);
            Toast.makeText(requireContext(), getString(R.string.wishlist_item_added), Toast.LENGTH_LONG).show();
        }

        ((MainActivity) requireActivity()).getFab().hide();
    }

    private void createReview() {
        KinoDto dto;
        if (wishlistDataDto.getWishlistItemType() == WishlistItemType.SERIE) {
            dto = new SerieDto(
                    null,
                    wishlistDataDto.getTmdbId() != null ? wishlistDataDto.getTmdbId().longValue() : null,
                    null,
                    wishlistDataDto.getTitle(),
                    null,
                    null,
                    null,
                    null,
                    wishlistDataDto.getPosterPath(),
                    wishlistDataDto.getOverview(),
                    wishlistDataDto.getFirstYear(),
                    wishlistDataDto.getReleaseDate(),
                    new ArrayList<>()
            );
        } else if (wishlistDataDto.getWishlistItemType() == WishlistItemType.MOVIE) {
            dto = new KinoDto(
                    null,
                    wishlistDataDto.getTmdbId() != null ? wishlistDataDto.getTmdbId().longValue() : null,
                    wishlistDataDto.getTitle(),
                    null,
                    null,
                    null,
                    null,
                    wishlistDataDto.getPosterPath(),
                    wishlistDataDto.getOverview(),
                    wishlistDataDto.getFirstYear(),
                    wishlistDataDto.getReleaseDate(),
                    new ArrayList<>()
            );
        } else {
            Toast.makeText(requireContext(), getString(R.string.wishlist_cant_create_review), Toast.LENGTH_LONG).show();
            return;
        }

        ((MainActivity) requireActivity()).navigateToReview(dto, true, R.id.action_wishlistItemFragment_to_editReviewFragment);
    }

  /*  TODO rewrite state management to get right data from editreview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ADD_REVIEW) {
            if (resultCode == Activity.RESULT_OK) {
                wishlistDataDto = Parcels.unwrap(data.getParcelableExtra("kino"));
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
