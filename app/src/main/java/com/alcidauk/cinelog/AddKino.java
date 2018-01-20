package com.alcidauk.cinelog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.zagum.switchicon.SwitchIconView;
import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.Movie;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;
import com.uwetrottmann.tmdb2.services.SearchService;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.Query;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import com.alcidauk.cinelog.dao.DaoSession;
import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.dao.LocalKinoDao;
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

    private static int RESULT_ADD_REVIEW = 6;

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
            //@Query("query") String query,
            //@Query("page") Integer page,
            //@Query("language") String language,
            //@Query("include_adult") Boolean includeAdult,
            //@Query("year") Integer year,
            //@Query("primary_release_year") Integer primaryReleaseYear,
            //@Query("search_type") String searchType
            Call<MovieResultsPage> results = searchService.movie(kino_search.getText().toString(), 1, null, null, null, null, "ngram");
            NetworkTask searchTask = new NetworkTask();
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

    class NetworkTask extends AsyncTask<Call<MovieResultsPage>, Void, List<Movie>> {

        private Exception exception;

        protected List<Movie> doInBackground(Call<MovieResultsPage>... results) {
            List<Movie> movies = null;
            try {
                if (!isCancelled()) {
                    movies = results[0].execute().body().results;
                }

                //search_adapter.clear();

                //search_adapter.notifyDataSetChanged();
            } catch (java.io.IOException e) {
                return null;
            }
            return movies;
        }

        protected void onPostExecute(List<Movie> movies) {
            //System.out.println(message);
            if (!isCancelled()) {

                populateListView(movies);
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void populateListView(final List<Movie> movies) {
        if (kino_results_list != null) {
            kino_results_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> view, View parent, final int position, long rowId) {
                    Intent intent = new Intent(view.getContext(), ViewKino.class);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                    String year = "";
                    int year_i = 0;
                    if (movies.get(position).release_date != null) {
                        year = sdf.format(movies.get(position).release_date);
                        year_i = Integer.parseInt(year);
                    }

                    System.out.println(year_i);
                    LocalKino kino = new LocalKino(movies.get(position).title, year, movies.get(position).poster_path, movies.get(position).overview, year_i, movies.get(position).id);
                    intent.putExtra("kino", Parcels.wrap(kino));
                    startActivity(intent);
                }
            });
            kino_results_list.setAdapter(new KinoResultsAdapter(this, movies));
            kino_search_progress_bar.setVisibility(View.GONE);
        }
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

    class KinoResultsAdapter extends BaseAdapter {

        private Context mContext;
        private List<Movie> mData;
        private int[] mWatchedData;
        private int[] mReveiewedData;
        private SimpleDateFormat sdf;
        PopupWindow popup_window;
        View popup_view;
        DaoSession daoSession;
        LocalKinoDao localKinoDao;
        Query<LocalKino> movie_id_query;
        DeleteQuery<LocalKino> delete_by_id_query;

        public KinoResultsAdapter(Context c, List<Movie> v) {
            mContext = c;
            if (v != null) {
                mData = v;
                mWatchedData = new int[mData.size()];
                mReveiewedData = new int[mData.size()];

                sdf = new SimpleDateFormat("yyyy");

                daoSession = ((KinoApplication) mContext.getApplicationContext()).getDaoSession();
                localKinoDao = daoSession.getLocalKinoDao();
                movie_id_query = localKinoDao.queryBuilder().where(LocalKinoDao.Properties.Movie_id.eq(1)).limit(1).build();
                delete_by_id_query = localKinoDao.queryBuilder().where(LocalKinoDao.Properties.Movie_id.eq(1)).buildDelete();
                //movie_review_query = localKinoDao.queryBuilder().where(LocalKinoDao.Properties.Movie_id.eq(1), localKinoDao.queryBuilder().or(LocalKinoDao.Properties.Rating.isNotNull(), LocalKinoDao.Properties.Review.isNotNull())).limit(1).build();
                //movie_review_query = localKinoDao.queryBuilder().where(localKinoDao.queryBuilder().or(LocalKinoDao.Properties.Rating.isNotNull(), LocalKinoDao.Properties.Review.isNotNull())).limit(1).build();
            } else {
                mData = new ArrayList<>();
            }
        }

        public int getCount() {
            return mData.size();
        }

        public Object getItem(int position) {
            return mData.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new RelativeView for each item referenced by the Adapter
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.search_result_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Movie movie = mData.get(position);

            // default the icons to disabled
            holder.switch_icon_watched.setIconEnabled(false);
            holder.switch_icon_review.setIconEnabled(false);

            if (movie.title != null)
                holder.title.setText(movie.title);

            String year = "";
            int year_i = 0;
            if (movie.release_date != null) {
                year = sdf.format(movie.release_date);
                holder.year.setText(year);
                year_i = Integer.parseInt(year);
            }


            if (movie.poster_path != null) {
                //poster.setLayoutParams(new ListView.LayoutParams(120,150));
                holder.poster.setLayoutParams(new RelativeLayout.LayoutParams(120, 150));
                Glide.with(mContext)
                        .load("https://image.tmdb.org/t/p/w185" + movie.poster_path)
                        .centerCrop()
                        //.placeholder(R.drawable.loading_spinner)
                        .crossFade()
                        .into(holder.poster);
            } else {
                if (holder.poster != null)
                    holder.poster.setImageResource(0);
            }

            final LocalKino kino = new LocalKino(movie.title, year, movie.poster_path, movie.overview, year_i, movie.id);
            final Integer m_id = movie.id;
            holder.toggle_review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), AddReview.class);

                    movie_id_query.setParameter(0, m_id);
                    List<LocalKino> movies = movie_id_query.list();

                    if (movies.isEmpty()) {
                        intent.putExtra("kino", Parcels.wrap(kino));
                    } else {
                        intent.putExtra("kino", Parcels.wrap(movies.get(0)));
                    }

                    //mContext.startActivity(intent);
                    ((Activity) mContext).startActivityForResult(intent, RESULT_ADD_REVIEW);
                    //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });


            int get_res = mWatchedData[position];
            if (get_res != 0) {
                if (get_res == 1) {
                    holder.switch_icon_watched.setIconEnabled(true);

                    get_res = mReveiewedData[position];
                    if (get_res == 1) {
                        holder.switch_icon_review.setIconEnabled(true);
                    }
                }
            } else {
                movie_id_query.setParameter(0, movie.id);
                List<LocalKino> movies = movie_id_query.list();
                if (!movies.isEmpty()) {
                    System.out.println("Result found");
                    mWatchedData[position] = 1;
                    holder.switch_icon_watched.setIconEnabled(true);

                    if (movies.get(0).getReview() != null || movies.get(0).getRating() != 0.0f) {
                        mReveiewedData[position] = 1;
                        holder.switch_icon_review.setIconEnabled(true);
                    } else {
                        mReveiewedData[position] = -1;
                    }

                } else {
                    mWatchedData[position] = -1;
                }
            }

            final SwitchIconView tmp_review = holder.switch_icon_review;
            final Integer movie_id = movie.id;
            holder.toggle_watched.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final SwitchIconView v = (SwitchIconView) ((ViewGroup) view).getChildAt(0);
                    if (v.isIconEnabled()) {
                        System.out.println("is_watched");
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddKino.this);
                        builder.setMessage(R.string.delete_kino_dialog)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Delete the kino
                                        delete_by_id_query.setParameter(0, movie_id);
                                        delete_by_id_query.executeDeleteWithoutDetachingEntities();
                                        daoSession.clear();
                                        v.setIconEnabled(false);
                                        tmp_review.setIconEnabled(false);
                                        mWatchedData[position] = -1;
                                        mReveiewedData[position] = -1;
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                });
                        builder.show();
                    } else {
                        try {
                            localKinoDao.insert(kino);
                            localKinoDao.detachAll();
                            v.setIconEnabled(true);
                            mWatchedData[position] = 1;
                        } catch (SQLiteConstraintException e) {
                            System.out.println("exception caught: " + e.getMessage());
                        }

                    }
                }
            });


            holder.toggle_watched.setFocusable(false);
            holder.toggle_review.setFocusable(false);


            return convertView;
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

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


