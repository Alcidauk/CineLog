package com.ulicae.cinelog.room.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ulicae.cinelog.android.v2.activities.MainActivity;

public abstract class AddableFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = ((MainActivity) requireActivity()).getFab();
        fab.setOnClickListener(v -> onFabClick());
        fab.setImageResource(getFabImage());
        fab.show();
    }

    protected abstract int getFabImage();

    protected abstract void onFabClick();

}
