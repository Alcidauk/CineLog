package com.ulicae.cinelog.io.importdb;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.ulicae.cinelog.databinding.ActivityImportDbBinding;
import com.ulicae.cinelog.io.importdb.builder.DtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.ReviewableDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.SerieReviewableDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.TagDtoFromRecordBuilder;
import com.ulicae.cinelog.io.importdb.builder.WishlistDtoFromRecordBuilder;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.CinelogSchedulers;
import com.ulicae.cinelog.room.dto.ItemDto;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.services.AsyncDataService;
import com.ulicae.cinelog.room.services.ReviewAsyncService;
import com.ulicae.cinelog.room.services.TagAsyncService;
import com.ulicae.cinelog.room.services.WishlistAsyncService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    Map<String, EntityImporter<? extends ItemDto>> entityImporterMap = new HashMap<>();

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

        Context context = requireContext();
        KinoApplication app = (KinoApplication) requireActivity().getApplication();
        AppDatabase db = app.getDb();
        

        entityImporterMap.putAll(new HashMap<String, EntityImporter<? extends ItemDto>>() {{
            put("tag",
                    new EntityImporter<>(
                            binding.importInDbContent.importDbTagsButton,
                            binding.importInDbContent.importTagsStatusWaiting,
                            binding.importInDbContent.importTagsErrorMessage,
                            new TagAsyncService(db),
                            new TagDtoFromRecordBuilder(context)
                    )
            );
            put("movie",
                    new EntityImporter<>(
                            binding.importInDbContent.importDbMoviesButton,
                            binding.importInDbContent.importMoviesStatusWaiting,
                            binding.importInDbContent.importMoviesErrorMessage,
                            new ReviewAsyncService(app, ItemEntityType.MOVIE),
                            new ReviewableDtoFromRecordBuilder(context)
                    )
            );
            put("serie",
                    new EntityImporter<>(
                            binding.importInDbContent.importDbSeriesButton,
                            binding.importInDbContent.importSeriesStatusWaiting,
                            binding.importInDbContent.importSeriesErrorMessage,     
                            new ReviewAsyncService(app, ItemEntityType.SERIE),
                            new SerieReviewableDtoFromRecordBuilder(context)
                    )
            );
            put("wishlistMovie",
                    new EntityImporter<>(
                            binding.importInDbContent.importDbWishlistMoviesButton,
                            binding.importInDbContent.importWishlistMoviesStatusWaiting,
                            binding.importInDbContent.importWishlistMoviesErrorMessage,
                            new WishlistAsyncService(app.getDb(), ItemEntityType.MOVIE),
                            new WishlistDtoFromRecordBuilder(context)
                    )
            );
            put("wishlistSerie",
                    new EntityImporter<>(
                            binding.importInDbContent.importDbWishlistSeriesButton,
                            binding.importInDbContent.importWishlistSeriesStatusWaiting,
                            binding.importInDbContent.importWishlistSeriesErrorMessage,
                            new WishlistAsyncService(app.getDb(), ItemEntityType.SERIE),
                            new WishlistDtoFromRecordBuilder(context)
                    )
            );
        }});

        initListeners();
    }

    public void initListeners() {
        for (EntityImporter entityImporter : entityImporterMap.values()) {
            entityImporter.importButton.setOnClickListener(v -> entityImporter.getLauncher().launch(Arrays.asList("text/*").toArray(new String[]{})));
        }

        binding.importInDbContent.importDbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importData();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
    }


    private void importData() {
        Toast.makeText(getContext(), getString(R.string.import_starting_toast), Toast.LENGTH_SHORT).show();
        
        for(EntityImporter<? extends ItemDto> importer : entityImporterMap.values()){
            asyncImportForType(
                    importer
            );
        }

        Toast.makeText(getContext(), getString(R.string.import_ending_toast), Toast.LENGTH_SHORT).show();

    }

    public void asyncImportForType(EntityImporter<?extends ItemDto> entityImporter) {
        Context context = requireContext();
        KinoApplication app = (KinoApplication) requireActivity().getApplication();

        try {
            if (entityImporter.getFile() == null) {
                return;
            }

            disposables.add(
                    new AsyncCsvImporter<>(
                            new FileReaderGetter(app),
                            new DtoImportCreator(context, entityImporter.getDtoFromRecordBuilder()),
                            entityImporter.getAsyncDataService(),
                            context
                    ).importCsvFile(entityImporter.getFile())
                            .subscribeOn(cinelogSchedulers.io())
                            .observeOn(cinelogSchedulers.androidMainThread())
                            .subscribe(
                                    (created) -> {
                                        entityImporter.getWaitingZone().setText(R.string.import_status_success);
                                    },
                                    error -> {
                                        showImportError(app, entityImporter.getWaitingZone(), entityImporter.getErrorZone(), (Throwable) error);
                                    }
                            )
            );

        } catch (ImportException e) {
            showImportError(app, entityImporter.getWaitingZone(), entityImporter.getErrorZone(), e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showImportError(KinoApplication app, TextView waitingUIZone, TextView errorUIZone, Throwable error) {
        Toast.makeText(app.getBaseContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        waitingUIZone.setText(R.string.import_status_failed);
        errorUIZone.setText(error.getMessage());
    }

    public class EntityImporter<T extends ItemDto> {

        private final ActivityResultLauncher launcher;
        private DocumentFile file;

        private final Button importButton;
        private TextView waitingZone;
        private final TextView errorZone;

        private final AsyncDataService<T> asyncDataService;
        private final DtoFromRecordBuilder<T> dtoFromRecordBuilder;

        private final ActivityResultCallback<Uri> openDocumentActivityResultCallback = result -> {
            try {
                this.file = DocumentFile.fromSingleUri(requireActivity(), result);

                waitingZone.setText(this.file.getName());
            } catch (NullPointerException e) {
                Toast.makeText(getContext(), getString(R.string.import_choose_file_stop), Toast.LENGTH_LONG).show();
            }
        };

        public EntityImporter(Button importButton, TextView waitingZone, TextView errorZone,
                              AsyncDataService<T> asyncDataService,
                              DtoFromRecordBuilder<T> dtoFromRecordBuilder
        ) {
            this.importButton = importButton;
            this.waitingZone = waitingZone;
            this.errorZone = errorZone;
            this.asyncDataService = asyncDataService;
            this.dtoFromRecordBuilder = dtoFromRecordBuilder;
            this.launcher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), openDocumentActivityResultCallback);
        }

        public EntityImporter(Button importDbSeriesButton, TextView importSeriesStatusWaiting,
                              TextView importSeriesErrorMessage,
                              ReviewAsyncService reviewAsyncService,
                              SerieReviewableDtoFromRecordBuilder serieReviewableDtoFromRecordBuilder) {
            this.importButton = importDbSeriesButton;
            this.waitingZone = importSeriesStatusWaiting;
            this.errorZone = importSeriesErrorMessage;
            this.asyncDataService = (AsyncDataService<T>) reviewAsyncService;
            this.dtoFromRecordBuilder = (DtoFromRecordBuilder<T>) serieReviewableDtoFromRecordBuilder;
            this.launcher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), openDocumentActivityResultCallback);
        }

        public ActivityResultLauncher getLauncher() {
            return this.launcher;
        }

        public TextView getWaitingZone() {
            return waitingZone;
        }

        public TextView getErrorZone() {
            return errorZone;
        }

        public DocumentFile getFile() {
            return file;
        }

        public DtoFromRecordBuilder<? extends ItemDto> getDtoFromRecordBuilder() {
            return dtoFromRecordBuilder;
        }

        public AsyncDataService<? extends ItemDto> getAsyncDataService() {
            return asyncDataService;
        }
    }
}
