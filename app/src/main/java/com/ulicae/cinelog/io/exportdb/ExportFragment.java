package com.ulicae.cinelog.io.exportdb;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.databinding.ActivityExportDbBinding;
import com.ulicae.cinelog.io.exportdb.exporter.ExporterFactory;
import com.ulicae.cinelog.io.exportdb.exporter.ReviewCsvExporterFactory;
import com.ulicae.cinelog.io.exportdb.exporter.TagCsvExporterFactory;
import com.ulicae.cinelog.utils.ToasterWrapper;
import com.ulicae.cinelog.io.exportdb.exporter.WishlistCsvExporterFactory;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.services.WishlistAsyncService;

public class ExportFragment extends Fragment {

    private @NonNull ActivityExportDbBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        binding = ActivityExportDbBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.exportInDbToolbar.toolbar);
        binding.exportInDbContent.exportDbButton.setOnClickListener(this::onClick);
    }

    private final ActivityResultCallback<Uri> activityResultCallback = result -> {
        DocumentFile choosenDirFile = DocumentFile.fromTreeUri(requireActivity(), result);
        KinoApplication app = ((KinoApplication) requireActivity().getApplication());

        exportData(app, choosenDirFile);
    };

    private void exportData(KinoApplication app, DocumentFile documentFile) {
        ToasterWrapper toasterWrapper = new ToasterWrapper(getContext());
        exportForType(
                app,
                documentFile,
                "export_wishlist_series.csv",
                new WishlistCsvExporterFactory(
                        new WishlistAsyncService(app.getDb(), ItemEntityType.SERIE)
                )

        );

        exportForType(
                app,
                documentFile,
                "export_wishlist_movies.csv",
                new WishlistCsvExporterFactory(
                        new WishlistAsyncService(app.getDb(), ItemEntityType.MOVIE)
                )
        );

        exportForType(
                app,
                documentFile,
                "export_tags.csv",
                new TagCsvExporterFactory((KinoApplication) requireActivity().getApplication())
        );

        exportForType(app, documentFile, "export_movies.csv", new ReviewCsvExporterFactory(app, ItemEntityType.MOVIE));
        exportForType(app, documentFile, "export_series.csv", new ReviewCsvExporterFactory(app, ItemEntityType.SERIE));
   }

    private void exportForType(KinoApplication app, DocumentFile documentFile,
                               String exportFilename, ExporterFactory exporterFactory) {
        DocumentFile exportFile = documentFile.createFile("application/text", exportFilename);
        if(exportFile == null){
            return;
        }

        new SnapshotExporter(
                exporterFactory,
                new ToasterWrapper(getContext()),
                requireActivity().getContentResolver()
        ).export(exportFile.getUri());
    }


    ActivityResultLauncher<Uri> launcher = registerForActivityResult(new ActivityResultContracts.OpenDocumentTree(), activityResultCallback);

    public void onClick(View view) {
        launcher.launch(Uri.fromFile(requireActivity().getFilesDir()));
    }

}
