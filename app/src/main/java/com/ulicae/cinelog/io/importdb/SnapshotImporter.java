package com.ulicae.cinelog.io.importdb;

import android.app.Application;
import android.widget.Toast;

import com.ulicae.cinelog.R;

public class SnapshotImporter {

    private Application application;

    public SnapshotImporter(Application application) {
        this.application = application;
    }

    public void importFiles(String importFilename, CsvImporter csvImporter) {
        showToast(R.string.import_starting_toast, Toast.LENGTH_LONG);
        try {
            csvImporter.importCsvFile(importFilename);
        } catch (ImportException e) {
            showToast(R.string.import_error_toast, Toast.LENGTH_SHORT, importFilename);
            showToast(e.getMessage(), Toast.LENGTH_LONG);
        }
        showToast(R.string.import_ending_toast, Toast.LENGTH_LONG);
    }

    private void showToast(int messageId, int lengthLong, String... args) {
        showToast(application.getString(messageId, (Object[]) args), lengthLong);
    }

    private void showToast(String message, int lengthLong) {
        Toast.makeText(application.getApplicationContext(), message, lengthLong).show();
    }
}
