package com.alcidauk.cinelog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcidauk.cinelog.dao.DaoSession;
import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.dao.LocalKinoDao;
import com.alcidauk.cinelog.exportdb.ExportDb;
import com.alcidauk.cinelog.importdb.ImportInDb;
import com.alcidauk.cinelog.settings.SettingsActivity;
import com.bumptech.glide.Glide;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.Query;
import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

//@ TODO ordering of results, infinite scroll

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.kino_list)
    ListView kino_list;

    @OnClick(R.id.fab)
    public void onClick(View view) {
        Intent intent = new Intent(this, AddKino.class);
        startActivity(intent);
    }

    //Tmdb tmdb;
    //SearchService searchService;

    DaoSession daoSession;
    LocalKinoDao localKinoDao;
    Query<LocalKino> get_reverse;
    DeleteQuery<LocalKino> delete_by_id_query;
    KinoListAdapter kino_adapter;
    List<LocalKino> kinos;


    Query<LocalKino> get_year_asc;
    Query<LocalKino> get_year_desc;

    Query<LocalKino> get_rating_asc;
    Query<LocalKino> get_rating_desc;

    private static final int RESULT_ADD_KINO = 2;
    private static final int RESULT_VIEW_KINO = 4;

    private String API_KEY = "d6d6579b3a02efda2efde4585120d45e";

    private int LIST_VIEW_STATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        //tmdb = new Tmdb(API_KEY);
        //searchService = tmdb.searchService();


        daoSession = ((KinoApplication) getApplication()).getDaoSession();
        localKinoDao = daoSession.getLocalKinoDao();

        get_reverse = localKinoDao.queryBuilder().orderDesc(LocalKinoDao.Properties.Id).build();

        // TODO remove this sort
        //get_year_asc = localKinoDao.queryBuilder().orderAsc(LocalKinoDao.Properties.Year).build();
        //get_year_desc = localKinoDao.queryBuilder().orderDesc(LocalKinoDao.Properties.Year).build();

        get_rating_asc = localKinoDao.queryBuilder().orderAsc(LocalKinoDao.Properties.Rating).build();
        get_rating_desc = localKinoDao.queryBuilder().orderDesc(LocalKinoDao.Properties.Rating).build();


        delete_by_id_query = localKinoDao.queryBuilder().where(LocalKinoDao.Properties.Id.eq(1)).buildDelete();
        createListView(1);
    }

    protected void onStart() {
        super.onStart();
        //updateListView();
        createListView(LIST_VIEW_STATE);
        System.out.println("onStart");
        //updateListView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_ADD_KINO) {
            if (resultCode == Activity.RESULT_OK) {
                System.out.println("Result Ok");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("Result Cancelled");
            }
        }
        if (requestCode == RESULT_VIEW_KINO) {
            if (resultCode == Activity.RESULT_OK) {
                int pos = data.getIntExtra("kino_position", -1);
                kinos.set(pos, (LocalKino) Parcels.unwrap(data.getParcelableExtra("kino")));
                kino_adapter.notifyDataSetChanged();
                System.out.println("Result Ok");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("Result Cancelled");
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_export:
                startActivity(new Intent(this, ExportDb.class));
                return true;
            case R.id.action_import:
                startActivity(new Intent(this, ImportInDb.class));
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.order_by_date_added_newest_first:
                createListView(1);
                return true;
            case R.id.order_by_date_added_oldest_first:
                createListView(2);
                return true;


            case R.id.order_by_rating_highest_first:
                createListView(3);
                return true;
            case R.id.order_by_rating_lowest_first:
                createListView(4);
                return true;
            case R.id.order_by_year_newest_first:
                createListView(5);
                return true;
            case R.id.order_by_year_oldest_first:
                createListView(6);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnItemClick(R.id.kino_list)
    public void onItemClick(AdapterView<?> parent, int position) {
        System.out.println("click detected");
    }

    private void updateListView() {
        System.out.println("updateListView");
        //kinos = get_reverse.list();
        //kino_adapter.notifyDataSetChanged();
    }

    private void createListView(int order) {
        System.out.println("createListView");
        if (kino_list != null) {

            //date added
            switch (order) {
                case 1:
                    kinos = get_reverse.list(); //localKinoDao.loadAll();
                    break;
                case 2:
                    kinos = localKinoDao.loadAll();
                    break;
                case 3:
                    kinos = get_rating_desc.list();
                    break;
                case 4:
                    kinos = get_rating_asc.list();
                    break;
                case 5:
                    kinos = get_year_desc.list();
                    break;
                case 6:
                    kinos = get_year_asc.list();
                    break;
                default:
                    kinos = localKinoDao.loadAll();
                    break;
            }
            LIST_VIEW_STATE = order;


            kino_adapter = new KinoListAdapter(this, kinos);
            kino_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(final AdapterView<?> view, View parent, final int position, long rowId) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(R.string.delete_kino_dialog)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Delete the kino

                                    delete_by_id_query.setParameter(0, kinos.get(position).getId());
                                    kinos.remove(position);
                                    kino_adapter.notifyDataSetChanged();
                                    delete_by_id_query.executeDeleteWithoutDetachingEntities();
                                    daoSession.clear();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    builder.create().show();
                    return true;
                }
            });
            kino_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> view, View parent, final int position, long rowId) {
                    Intent intent = new Intent(view.getContext(), ViewKino.class);
                    intent.putExtra("kino", Parcels.wrap(kinos.get(position)));
                    intent.putExtra("kino_position", position);
                    startActivityForResult(intent, RESULT_VIEW_KINO);
                }
            });


            kino_list.setAdapter(kino_adapter);
        }
    }
}

class KinoListAdapter extends BaseAdapter {

    private Context mContext;
    private List<LocalKino> mData;

    public KinoListAdapter(Context c, List<LocalKino> v) {
        mContext = c;
        mData = v;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.main_result_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LocalKino movie = mData.get(position);

        holder.title.setText(movie.getTitle());
        //TODO remove it holder.year.setText(movie.getRelease_date());

        if (movie.getKino() != null && movie.getKino().getPoster_path() != null) {
            //poster.setLayoutParams(new ListView.LayoutParams(120,150));
            holder.poster.setLayoutParams(new RelativeLayout.LayoutParams(120, 150));
            Glide.with(mContext)
                    .load("https://image.tmdb.org/t/p/w185" + movie.getKino().getPoster_path())
                    .centerCrop()
                    //.placeholder(R.drawable.loading_spinner)
                    .crossFade()
                    .into(holder.poster);
        } else {
            holder.poster.setLayoutParams(new RelativeLayout.LayoutParams(120, 150));
            Glide.with(mContext)
                    // TODO set a default image
                    .load("https://www.texasforthem.org/wp-content/uploads/2016/03/Feral-Cat-1.jpg")
                    .centerCrop()
                    //.placeholder(R.drawable.loading_spinner)
                    .crossFade()
                    .into(holder.poster);
        }

        holder.rating_bar.setStepSize(0.5f);

        int maxRating;
        if(movie.getMaxRating() == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            String defaultMaxRateValue = prefs.getString("default_max_rate_value", "5");
            maxRating = Integer.parseInt(defaultMaxRateValue);
        } else {
            maxRating = movie.getMaxRating();
        }
        holder.rating_bar.setNumStars(maxRating);

        if(movie.getRating() != null) {
            holder.rating_bar.setRating(movie.getRating());
        }

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.kino_title)
        TextView title;
        @BindView(R.id.kino_year)
        TextView year;
        @BindView(R.id.kino_poster)
        ImageView poster;
        @BindView(R.id.kino_rating_bar_small)
        RatingBar rating_bar;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}