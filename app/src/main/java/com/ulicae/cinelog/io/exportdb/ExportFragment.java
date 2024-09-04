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
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.ItemDto;
import com.ulicae.cinelog.databinding.ActivityExportDbBinding;
import com.ulicae.cinelog.io.exportdb.exporter.ExporterFactory;
import com.ulicae.cinelog.io.exportdb.exporter.ReviewCsvExporterFactory;
import com.ulicae.cinelog.io.exportdb.exporter.TagCsvExporterFactory;
import com.ulicae.cinelog.io.exportdb.exporter.WishlistCsvExporterFactory;
import com.ulicae.cinelog.room.CinelogSchedulers;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.services.WishlistAsyncService;
import com.ulicae.cinelog.utils.ToasterWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

public class ExportFragment extends Fragment {

    private @NonNull ActivityExportDbBinding binding;

    private List<Disposable> disposableList;

    private ToasterWrapper toasterWrapper;

    private SnapshotExporterFactory snapshotExporterFactory;

    private CinelogSchedulers cinelogSchedulers;

    void setToasterWrapper(ToasterWrapper toasterWrapper) {
        this.toasterWrapper = toasterWrapper;
    }

    void setSnapshotExporterFactory(SnapshotExporterFactory snapshotExporterFactory) {
        this.snapshotExporterFactory = snapshotExporterFactory;
    }

    void setDisposableList(List<Disposable> disposableList) {
        this.disposableList = disposableList;
    }

    void setCinelogSchedulers(CinelogSchedulers cinelogSchedulers) {
        this.cinelogSchedulers = cinelogSchedulers;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        setToasterWrapper(new ToasterWrapper(getContext()));
        setSnapshotExporterFactory(
                new SnapshotExporterFactory(toasterWrapper, requireActivity().getContentResolver()));
        setDisposableList(new ArrayList<>());

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
        exportForType(
                documentFile,
                "export_wishlist_series.csv",
                new WishlistCsvExporterFactory(
                        new WishlistAsyncService(app.getDb(), ItemEntityType.SERIE)
                )

        );

        exportForType(
                documentFile,
                "export_wishlist_movies.csv",
                new WishlistCsvExporterFactory(
                        new WishlistAsyncService(app.getDb(), ItemEntityType.MOVIE)
                )
        );

        exportForType(
                documentFile,
                "export_tags.csv",
                new TagCsvExporterFactory((KinoApplication) requireActivity().getApplication())
        );

        exportForType(documentFile, "export_movies.csv", new ReviewCsvExporterFactory(app, ItemEntityType.MOVIE));
        exportForType(documentFile, "export_series.csv", new ReviewCsvExporterFactory(app, ItemEntityType.SERIE));
    }

    void exportForType(DocumentFile documentFile,
                       String exportFilename,
                       ExporterFactory<? extends ItemDto> exporterFactory) {
        toasterWrapper.toast(R.string.export_start_toast, ToasterWrapper.ToasterDuration.SHORT);

        DocumentFile exportFile = documentFile.createFile("application/text", exportFilename);
        if (exportFile == null) {
            toasterWrapper.toast(R.string.export_io_error_toast, ToasterWrapper.ToasterDuration.LONG);
            return;
        }
        try {
            disposableList.add(
                    snapshotExporterFactory
                            .makeSnapshotExporter(exporterFactory)
                            .export(exportFile.getUri())
                            .observeOn(cinelogSchedulers.androidMainThread())
                            .subscribe(
                                    success -> toasterWrapper.toast(R.string.export_succeeded_toast, ToasterWrapper.ToasterDuration.LONG),
                                    error -> toasterWrapper.toast(R.string.export_io_error_toast, ToasterWrapper.ToasterDuration.LONG)
                            )
            );
        } catch (IOException e) {
            toasterWrapper.toast(R.string.export_io_error_toast, ToasterWrapper.ToasterDuration.LONG);
       }
    }


    ActivityResultLauncher<Uri> launcher = registerForActivityResult(new ActivityResultContracts.OpenDocumentTree(), activityResultCallback);

    public void onClick(View view) {
        launcher.launch(Uri.fromFile(requireActivity().getFilesDir()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        for (Disposable disposable : disposableList) {
            disposable.dispose();
        }
    }
}
