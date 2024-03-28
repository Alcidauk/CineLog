package com.ulicae.cinelog.android.v2.fragments;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.TmdbItemDto;

public abstract class ShareableFragment<T extends TmdbItemDto> extends Fragment {

    private String linkBaseUrl;

    protected T item;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_review, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            shareMovie();
            return true;
        }
        return true;
    }

    private void shareMovie() {
        if (this.item.getTmdbKinoId() == null) {
            shareText(this.item.getTitle());
        } else {
            shareText(linkBaseUrl + this.item.getTmdbKinoId());
        }
    }
    private void shareText(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    protected void setLinkBaseUrl(String url) {
        this.linkBaseUrl = url;
    }

    protected void addOptionMenu() {
        setHasOptionsMenu(true);
    }
}
