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
import com.ulicae.cinelog.io.exportdb.AutomaticExportException;
import com.ulicae.cinelog.io.importdb.builder.SerieReviewableDtoFromRecordBuilder;
import com.ulicae.cinelog.room.dto.ItemDto;
import com.ulicae.cinelog.room.services.AsyncDataService;
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

/**
 * CineLog Copyright 2025 Pierre Rognon
 * <p>
 * <p>
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 */
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
        try {
            DocumentFile choosenDirFile = DocumentFile.fromTreeUri(requireActivity(), result);
            KinoApplication app = ((KinoApplication) requireActivity().getApplication());

            importData(app, choosenDirFile);
        } catch (NullPointerException e){
            Toast.makeText(this.getContext(), getString(R.string.import_choose_file_stop), Toast.LENGTH_LONG).show();
        }

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
                new SerieReviewableDtoFromRecordBuilder(context),
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
                                    (created) -> {
                                        waitingUIZone.setText(R.string.import_status_success);
                                    },
                                    error -> {
                                        showImportError(app, waitingUIZone, errorUIZone, (Throwable) error);
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
