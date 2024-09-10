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
import com.ulicae.cinelog.data.dto.ItemDto;
import com.ulicae.cinelog.data.services.AsyncDataService;
import com.ulicae.cinelog.databinding.ActivityImportDbBinding;
import com.ulicae.cinelog.io.importdb.builder.DtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.ReviewableDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.TagDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.WishlistDtoFromRecordBuilder;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.CinelogSchedulers;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.services.ReviewAsyncService;
import com.ulicae.cinelog.room.services.TagAsyncService;
import com.ulicae.cinelog.room.services.WishlistAsyncService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;

public class ImportFragment extends Fragment {

    private @NonNull
    ActivityImportDbBinding binding;

    private CinelogSchedulers cinelogSchedulers;
    private List<Disposable> disposables;

    public ImportFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        cinelogSchedulers = new CinelogSchedulers();
        disposables = new ArrayList<>();

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        for(Disposable disposable : disposables) {
            disposable.dispose();
        }
    }

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

        asyncImportForType(
                app,
                context,
                choosenDirFile,
                "import_movies.csv",
                new ReviewAsyncService(app, ItemEntityType.MOVIE),
                new ReviewableDtoFromRecordBuilder(context),
                binding.importInDbContent.importMoviesStatusWaiting,
                binding.importInDbContent.importMoviesErrorMessage
        );

        asyncImportForType(
                app,
                context,
                choosenDirFile,
                "import_series.csv",
                new ReviewAsyncService(app, ItemEntityType.SERIE),
                new ReviewableDtoFromRecordBuilder(context),
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

    public void asyncImportForType(KinoApplication app,
                                   Context context,
                                   DocumentFile choosenDirFile,
                                   String importFilename,
                                   AsyncDataService<? extends ItemDto> asyncDataService,
                                   DtoFromRecordBuilder dtoFromRecordBuilder,
                                   TextView waitingUIZone,
                                   TextView errorUIZone) {
        try {
            disposables.add(
                    new AsyncCsvImporter<>(
                            new FileReaderGetter(app),
                            new DtoImportCreator<>(context, dtoFromRecordBuilder),
                            asyncDataService,
                            context
                    ).importCsvFile(choosenDirFile, importFilename)
                            .subscribeOn(cinelogSchedulers.io())
                            .observeOn(cinelogSchedulers.androidMainThread())
                            .subscribe(
                                    () -> {
                                        waitingUIZone.setText(R.string.import_status_success);
                                    },
                                    error -> {
                                        showImportError(app, waitingUIZone, errorUIZone, error);
                                    }
                            )
            );

        } catch (ImportException e) {
            showImportError(app, waitingUIZone, errorUIZone, e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showImportError(KinoApplication app, TextView waitingUIZone, TextView errorUIZone, Throwable error) {
        Toast.makeText(app.getBaseContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        waitingUIZone.setText(R.string.import_status_failed);
        errorUIZone.setText(error.getMessage());
    }
}
