package com.ulicae.cinelog.io.exportdb;

import android.Manifest;
import android.app.Application;
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
import com.ulicae.cinelog.data.services.wishlist.MovieWishlistService;
import com.ulicae.cinelog.data.services.wishlist.SerieWishlistService;
import com.ulicae.cinelog.databinding.ActivityExportDbBinding;
import com.ulicae.cinelog.io.exportdb.exporter.MovieCsvExporterFactory;
import com.ulicae.cinelog.io.exportdb.exporter.SerieCsvExporterFactory;
import com.ulicae.cinelog.io.exportdb.exporter.TagCsvExporterFactory;
import com.ulicae.cinelog.io.exportdb.exporter.WishlistCsvExporterFactory;

public class ExportFragment extends Fragment {

    private @NonNull ActivityExportDbBinding binding;

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

        binding = ActivityExportDbBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.exportInDbToolbar.toolbar);
        binding.exportInDbContent.exportDbButton.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        Application app = requireActivity().getApplication();
        if (writeStoragePermission != null && writeStoragePermission) {
            new SnapshotExporter(new TagCsvExporterFactory(((MainActivity) requireActivity()).getDb()), app).export("export_tags.csv");
            new SnapshotExporter(new MovieCsvExporterFactory(app), app).export("export_movies.csv");
            new SnapshotExporter(new SerieCsvExporterFactory(app), app).export("export_series.csv");
            new SnapshotExporter(new WishlistCsvExporterFactory(
                    new MovieWishlistService(((KinoApplication) app).getDaoSession())),
                    app).export("export_wishlist_movies.csv");
            new SnapshotExporter(new WishlistCsvExporterFactory(
                    new SerieWishlistService(((KinoApplication) app).getDaoSession())),
                    app).export("export_wishlist_series.csv");
        } else {
            Toast.makeText(app.getApplicationContext(), getString(R.string.export_permission_error_toast), Toast.LENGTH_LONG).show();
        }
    }

}
