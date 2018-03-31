package com.alcidauk.cinelog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alcidauk.cinelog.dto.KinoDto;
import com.alcidauk.cinelog.tmdb.NetworkTaskManager;
import com.alcidauk.cinelog.tmdb.TmdbServiceWrapper;
import com.github.zagum.switchicon.SwitchIconView;
import com.uwetrottmann.tmdb2.entities.Movie;

import org.parceler.Parcels;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class AddKino extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.kino_search)
    EditText kino_search;
    @BindView(R.id.kino_results)
    ListView kino_results_list;
    @BindView(R.id.kino_search_progress_bar)
    ProgressBar kino_search_progress_bar;

    private TmdbServiceWrapper tmdbServiceWrapper;
    private NetworkTaskManager networkTaskManager;

    private Handler handler;

    private final static int TRIGGER_SERACH = 1;

    // Where did 1000 come from? It's arbitrary, since I can't find average android typing speed.
    private final static long SEARCH_TRIGGER_DELAY_IN_MS = 1000;

    static int RESULT_ADD_REVIEW = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kino);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tmdbServiceWrapper = new TmdbServiceWrapper();
        networkTaskManager = new NetworkTaskManager(this);

        handler = new AddKinoHandler(new WeakReference<>(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_ADD_REVIEW) {
            if (resultCode == Activity.RESULT_OK) {
                System.out.println("Result Ok");
                startSearchTask();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("Result Cancelled");
            }
        }
    }

    private void startSearchTask() {
        if (isNetworkAvailable()) {
            networkTaskManager.createAndExecute(
                    tmdbServiceWrapper.search(kino_search.getText().toString())
            );
        } else {
            Toast t = Toast.makeText(getApplicationContext(),
                    "Error no network available.",
                    Toast.LENGTH_LONG);
            t.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @SuppressWarnings("unused")
    @OnTextChanged(R.id.kino_search)
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count > 0) {
            kino_search_progress_bar.setVisibility(View.VISIBLE);
            handler.removeMessages(TRIGGER_SERACH);
            handler.sendEmptyMessageDelayed(TRIGGER_SERACH, SEARCH_TRIGGER_DELAY_IN_MS);
        } else if (count == 0) {
            clearListView();
        }
    }

    private void clearListView() {
        if (kino_results_list != null) {
            kino_results_list.setAdapter(null);
        }
    }

    @OnClick(R.id.kino_search_add_from_scratch)
    public void onClick(View view) {
        // TODO make it work with KinoDto
        // new KinoCreator(new LocalKinoRepository(((KinoApplication) getApplication()).getDaoSession())).createOrUpdate(kino_search.getText().toString());
    }

    public void populateListView(final List<Movie> movies) {
        if (kino_results_list != null) {
            kino_results_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> view, View parent, final int position, long rowId) {
                    Intent intent = new Intent(view.getContext(), ViewKino.class);

                    KinoDto kino = new KinoBuilderFromMovie().build(movies.get(position));
                    intent.putExtra("kino", Parcels.wrap(kino));

                    startActivity(intent);
                }
            });

            kino_results_list.setAdapter(new KinoResultsAdapter(this, movies));
            kino_search_progress_bar.setVisibility(View.GONE);
        }
    }

    static class ViewHolder {
        @BindView(R.id.kino_title)
        TextView title;
        @BindView(R.id.kino_year)
        TextView year;
        @BindView(R.id.kino_poster)
        ImageView poster;

        @BindView(R.id.toggle_watched)
        LinearLayout toggle_watched;
        @BindView(R.id.toggle_review)
        LinearLayout toggle_review;

        @BindView(R.id.switch_icon_review)
        SwitchIconView switch_icon_review;
        @BindView(R.id.switch_icon_watched)
        SwitchIconView switch_icon_watched;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class AddKinoHandler extends Handler {
        private WeakReference<AddKino> addKino;

        AddKinoHandler(WeakReference<AddKino> addKino) {
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


