package com.ulicae.cinelog.io.importdb;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.services.reviews.KinoService;
import com.ulicae.cinelog.data.services.reviews.SerieService;
import com.ulicae.cinelog.data.services.tags.room.TagAsyncService;
import com.ulicae.cinelog.data.services.wishlist.MovieWishlistService;
import com.ulicae.cinelog.data.services.wishlist.SerieWishlistService;
import com.ulicae.cinelog.databinding.ActivityImportDbBinding;
import com.ulicae.cinelog.io.importdb.builder.KinoDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.SerieDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.TagDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.WishlistDtoFromRecordBuilder;
import com.ulicae.cinelog.room.AppDatabase;

public class ImportFragment extends Fragment {

    private @NonNull
    ActivityImportDbBinding binding;

    private Boolean writeStoragePermission;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(), isGranted -> {
                        writeStoragePermission = isGranted;
                    }
            );


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            writeStoragePermission = true;
        } else {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        binding = ActivityImportDbBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.importInDbToolbar.toolbar);
        binding.importInDbContent.importDbButton.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        Application app = requireActivity().getApplication();
        Context context = requireContext();
        AppDatabase db = ((KinoApplication) requireActivity().getApplication()).getDb();

        if (writeStoragePermission != null && writeStoragePermission) {
            Toast.makeText(context, getString(R.string.import_starting_toast), Toast.LENGTH_SHORT).show();

            // TODO improve this code
            try {
                new CsvImporter<>(
                        new FileReaderGetter(app),
                        new DtoImportCreator<>(context, new TagDtoFromRecordBuilder(context)),
                        new TagAsyncService(
                                db),
                        context
                ).importCsvFile("import_tags.csv");

                binding.importInDbContent.importTagsStatusWaiting.setText(R.string.import_status_success);
            } catch (ImportException e) {
                Toast.makeText(app.getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                binding.importInDbContent.importTagsStatusWaiting.setText(R.string.import_status_failed);
                binding.importInDbContent.importTagsErrorMessage.setText(e.getMessage());
            }

            try {
                new CsvImporter<>(
                        new FileReaderGetter(app),
                        new DtoImportCreator<>(context, new KinoDtoFromRecordBuilder(context)),
                        new KinoService(((KinoApplication) app).getDaoSession(), db),
                        context).importCsvFile("import_movies.csv");

                binding.importInDbContent.importMoviesStatusWaiting.setText(R.string.import_status_success);
            } catch (ImportException e) {
                Toast.makeText(app.getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                binding.importInDbContent.importMoviesStatusWaiting.setText(R.string.import_status_failed);
                binding.importInDbContent.importMoviesErrorMessage.setText(e.getMessage());
            }

            try {
                new CsvImporter<>(
                        new FileReaderGetter(app),
                        new DtoImportCreator<>(context, new SerieDtoFromRecordBuilder(context)),
                        new SerieService(((KinoApplication) app).getDaoSession(), db, context),
                        context).importCsvFile("import_series.csv");

                binding.importInDbContent.importSeriesStatusWaiting.setText(R.string.import_status_success);
            } catch (ImportException e) {
                Toast.makeText(app.getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                binding.importInDbContent.importSeriesStatusWaiting.setText(R.string.import_status_failed);
                binding.importInDbContent.importSeriesErrorMessage.setText(e.getMessage());
            }

            try {
                new CsvImporter<>(
                        new FileReaderGetter(app),
                        new DtoImportCreator<>(context, new WishlistDtoFromRecordBuilder(context)),
                        new MovieWishlistService(((KinoApplication) app).getDaoSession()),
                        context).importCsvFile("import_wishlist_movies.csv");

                binding.importInDbContent.importWishlistMoviesStatusWaiting.setText(R.string.import_status_success);
            } catch (ImportException e) {
                Toast.makeText(app.getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                binding.importInDbContent.importWishlistMoviesStatusWaiting.setText(R.string.import_status_failed);
                binding.importInDbContent.importWishlistMoviesErrorMessage.setText(e.getMessage());
            }

            try {
                new CsvImporter<>(
                        new FileReaderGetter(app),
                        new DtoImportCreator<>(context, new WishlistDtoFromRecordBuilder(context)),
                        new SerieWishlistService(((KinoApplication) app).getDaoSession()),
                        context).importCsvFile("import_wishlist_series.csv");

                binding.importInDbContent.importWishlistSeriesStatusWaiting.setText(R.string.import_status_success);
            } catch (ImportException e) {
                Toast.makeText(app.getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                binding.importInDbContent.importWishlistSeriesStatusWaiting.setText(R.string.import_status_failed);
                binding.importInDbContent.importWishlistSeriesErrorMessage.setText(e.getMessage());
            }

            Toast.makeText(app.getBaseContext(), getString(R.string.import_ending_toast), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(app.getBaseContext(), getString(R.string.import_permission_error_toast), Toast.LENGTH_LONG).show();
        }
    }

}
