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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alcidauk.cinelog.addkino.KinoCreator;
import com.alcidauk.cinelog.db.LocalKinoRepository;
import com.github.zagum.switchicon.SwitchIconView;
import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;
import com.uwetrottmann.tmdb2.services.SearchService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit2.Call;

public class AddKino extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.kino_search)
    EditText kino_search;
    @BindView(R.id.kino_results)
    ListView kino_results_list;
    @BindView(R.id.kino_search_progress_bar)
    ProgressBar kino_search_progress_bar;

    Tmdb tmdb;
    SearchService searchService;

    private ArrayList<NetworkTask> taskList;

    private String API_KEY = "d6d6579b3a02efda2efde4585120d45e";

    static int RESULT_ADD_REVIEW = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kino);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        kino_search.addTextChangedListener(textWatcher);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tmdb = new Tmdb(API_KEY);
        searchService = tmdb.searchService();

        taskList = new ArrayList<>();
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
            Call<MovieResultsPage> results = searchService.movie(kino_search.getText().toString(), 1, null, null, null, null, "ngram");
            NetworkTask searchTask = new NetworkTask(this);
            searchTask.execute(results);
            taskList.add(searchTask);
        } else {
            Toast t = Toast.makeText(getApplicationContext(),
                    "Error no network available.",
                    Toast.LENGTH_LONG);
            t.show();
        }
    }

    private final int TRIGGER_SERACH = 1;
    // Where did 1000 come from? It's arbitrary, since I can't find average android typing speed.
    private final long SEARCH_TRIGGER_DELAY_IN_MS = 1000;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TRIGGER_SERACH) {
                startSearchTask();
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            System.out.println("onTextChanged");
            System.out.println(start + " " + before + " " + count);
            for (NetworkTask task : taskList) {
                task.cancel(true);
            }
            if (count > 0) {
                kino_search_progress_bar.setVisibility(View.VISIBLE);
                handler.removeMessages(TRIGGER_SERACH);
                handler.sendEmptyMessageDelayed(TRIGGER_SERACH, SEARCH_TRIGGER_DELAY_IN_MS);
            } else if (count == 0) {
                for (NetworkTask task : taskList) {
                    task.cancel(true);
                }
                clearListView();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            System.out.println("beforeTextChanged");
            System.out.println(start + " " + count + " " + after);
        }

        @Override
        public void afterTextChanged(Editable s) {
            System.out.println("afterTextChanged");
        }
    };

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void clearListView() {
        if (kino_results_list != null) {
            kino_results_list.setAdapter(null);
        }
    }

    @OnItemClick(R.id.kino_results)
    public void onItemClick(AdapterView<?> parent, int position) {
        System.out.println("click detected");
    }

    @OnClick(R.id.kino_search_add_from_scratch)
    public void onClick(View view) {
        new KinoCreator(new LocalKinoRepository(((KinoApplication) getApplication()).getDaoSession())).create(kino_search.getText().toString());
        System.out.println("coucou toi !");
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

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


