package com.ulicae.cinelog.android.activities.add;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.services.reviews.DataService;
import com.ulicae.cinelog.databinding.ContentAddReviewBinding;
import com.ulicae.cinelog.databinding.ToolbarBinding;
import com.ulicae.cinelog.network.TmdbServiceWrapper;
import com.ulicae.cinelog.network.task.NetworkTaskManager;
import com.ulicae.cinelog.utils.ThemeWrapper;
import com.uwetrottmann.tmdb2.entities.BaseRatingObject;

import java.lang.ref.WeakReference;
import java.util.List;


/**
 * CineLog Copyright 2022 Pierre Rognon
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
public abstract class AddReviewActivity<T extends BaseRatingObject> extends AppCompatActivity {

    protected TmdbServiceWrapper tmdbServiceWrapper;
    protected NetworkTaskManager networkTaskManager;

    protected DataService dataService;

    private Handler handler;

    private final static int TRIGGER_SERACH = 1;

    // Where did 1000 come from? It's arbitrary, since I can't find average android typing speed.
    private final static long SEARCH_TRIGGER_DELAY_IN_MS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        inflateBinding();

        getContentAddReviewBinding().kinoSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onSearchChange(charSequence, i, i1, i2);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        getContentAddReviewBinding().kinoSearchAddFromScratch.setOnClickListener(this::onFromScratchClick);


        setSupportActionBar(getToolbar().toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tmdbServiceWrapper = new TmdbServiceWrapper(this);

        handler = new AddReviewHandler(new WeakReference<AddReviewActivity>(this));
    }

    protected abstract void inflateBinding();

    protected abstract ToolbarBinding getToolbar();

    protected abstract ContentAddReviewBinding getContentAddReviewBinding();

    protected abstract void onFromScratchClick(View view);

    private void startSearchTask() {
        if (isNetworkAvailable()) {
            executeTask(getContentAddReviewBinding().kinoSearch.getText().toString());
        } else {
            Toast t = Toast.makeText(getApplicationContext(),
                    getString(R.string.addkino_error_no_network),
                    Toast.LENGTH_LONG);
            t.show();
        }
    }

    protected abstract void executeTask(String textToSearch);

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @SuppressWarnings("unused")
    public void onSearchChange(CharSequence s, int start, int before, int count) {
        if (count > 0) {
            getContentAddReviewBinding().kinoSearchProgressBar.setVisibility(View.VISIBLE);
            handler.removeMessages(TRIGGER_SERACH);
            handler.sendEmptyMessageDelayed(TRIGGER_SERACH, SEARCH_TRIGGER_DELAY_IN_MS);
        } else if (count == 0) {
            clearListView();
        }
    }

    public void clearListView() {
        if (getContentAddReviewBinding().kinoResults.getAdapter() != null) {
            getContentAddReviewBinding().kinoResults.setAdapter(null);
        }
        getContentAddReviewBinding().kinoSearchProgressBar.setVisibility(View.GONE);
    }

    public abstract void populateListView(final List<T> movies);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static class AddReviewHandler extends Handler {
        private WeakReference<AddReviewActivity> addKino;

        AddReviewHandler(WeakReference<AddReviewActivity> addKino) {
            this.addKino = addKino;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TRIGGER_SERACH) {
                addKino.get().startSearchTask();
            }
        }
    }
}
