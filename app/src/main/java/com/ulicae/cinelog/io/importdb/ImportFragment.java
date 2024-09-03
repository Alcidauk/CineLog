package com.ulicae.cinelog.io.importdb;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ulicae.cinelog.data.services.AsyncDataService;
import com.ulicae.cinelog.data.services.reviews.ItemService;
import com.ulicae.cinelog.data.services.reviews.KinoService;
import com.ulicae.cinelog.data.services.reviews.SerieService;
import com.ulicae.cinelog.room.services.TagAsyncService;
import com.ulicae.cinelog.databinding.ActivityImportDbBinding;
import com.ulicae.cinelog.io.importdb.builder.DtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.KinoDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.SerieDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.TagDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.WishlistDtoFromRecordBuilder;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.services.WishlistAsyncService;

import java.io.IOException;

public class ImportFragment extends Fragment {

    private @NonNull
    ActivityImportDbBinding binding;

    public ImportFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        binding = ActivityImportDbBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.importInDbToolbar.toolbar);
        binding.importInDbContent.importDbButton.setOnClickListener(this::onClick);
    }

    private final ActivityResultCallback<Uri> activityResultCallback = result -> {
        DocumentFile choosenDirFile = DocumentFile.fromTreeUri(requireActivity(), result);
        KinoApplication app = ((KinoApplication) requireActivity().getApplication());

        importData(app, choosenDirFile);
    };

    ActivityResultLauncher<Uri> launcher = registerForActivityResult(new ActivityResultContracts.OpenDocumentTree(), activityResultCallback);

    public void onClick(View view) {
        launcher.launch(Uri.fromFile(requireActivity().getFilesDir()));
    }

    private void importData(KinoApplication app, DocumentFile choosenDirFile) {
        Context context = requireContext();
        AppDatabase db = app.getDb();
        // TODO ToasterWrapper toasterWrapper = new ToasterWrapper(getContext());

        Toast.makeText(context, getString(R.string.import_starting_toast), Toast.LENGTH_SHORT).show();

        asyncImportForType(
                app,
                context,
                choosenDirFile,
                "import_tags.csv",
                new TagAsyncService(db),
                new TagDtoFromRecordBuilder(context),
                binding.importInDbContent.importTagsStatusWaiting,
                binding.importInDbContent.importTagsErrorMessage
        );

        importForType(
                app,
                context,
                choosenDirFile,
                "import_movies.csv",
                new KinoService(app.getDaoSession(), db),
                new KinoDtoFromRecordBuilder(context),
                binding.importInDbContent.importMoviesStatusWaiting,
                binding.importInDbContent.importMoviesErrorMessage
        );

        importForType(
                app,
                context,
                choosenDirFile,
                "import_series.csv",
                new SerieService(app.getDaoSession(), db, context),
                new SerieDtoFromRecordBuilder(context),
                binding.importInDbContent.importSeriesStatusWaiting,
                binding.importInDbContent.importSeriesErrorMessage
        );

        asyncImportForType(
                app,
                context,
                choosenDirFile,
                "import_wishlist_movies.csv",
                new WishlistAsyncService(app.getDb(), ItemEntityType.MOVIE),
                new WishlistDtoFromRecordBuilder(context),
                binding.importInDbContent.importWishlistMoviesStatusWaiting,
                binding.importInDbContent.importWishlistMoviesErrorMessage
                );

        asyncImportForType(
                app,
                context,
                choosenDirFile,
                "import_wishlist_series.csv",
                new WishlistAsyncService(app.getDb(), ItemEntityType.SERIE),
                new WishlistDtoFromRecordBuilder(context),
                binding.importInDbContent.importWishlistSeriesStatusWaiting,
                binding.importInDbContent.importWishlistSeriesErrorMessage
        );

        Toast.makeText(app.getBaseContext(), getString(R.string.import_ending_toast), Toast.LENGTH_SHORT).show();

    }

    private void importForType(KinoApplication app,
                               Context context,
                               DocumentFile choosenDirFile, String importFilename,
                               ItemService itemService,
                               DtoFromRecordBuilder dtoFromRecordBuilder,
                               TextView waitingUIZone,
                               TextView errorUIZone) {
        try {
            new CsvImporter<>(
                    new FileReaderGetter(app),
                    new DtoImportCreator<>(context, dtoFromRecordBuilder),
                    itemService,
                    context
            ).importCsvFile(importFilename);

            waitingUIZone.setText(R.string.import_status_success);
        } catch (ImportException e) {
            Toast.makeText(app.getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            waitingUIZone.setText(R.string.import_status_failed);
            errorUIZone.setText(e.getMessage());
        }
    }

    public void asyncImportForType(KinoApplication app,
                                   Context context,
                                   DocumentFile choosenDirFile,
                                   String importFilename,
                                   AsyncDataService asyncDataService,
                                   DtoFromRecordBuilder dtoFromRecordBuilder,
                                   TextView waitingUIZone,
                                   TextView errorUIZone) {
        try {
            new AsyncCsvImporter<>(
                    new FileReaderGetter(app),
                    new DtoImportCreator<>(context, dtoFromRecordBuilder),
                    asyncDataService,
                    context
            ).importCsvFile(choosenDirFile, importFilename);

            waitingUIZone.setText(R.string.import_status_success);
        } catch (ImportException e) {
            Toast.makeText(app.getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            waitingUIZone.setText(R.string.import_status_failed);
            errorUIZone.setText(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
