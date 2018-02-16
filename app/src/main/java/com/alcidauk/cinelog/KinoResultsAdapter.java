package com.alcidauk.cinelog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.alcidauk.cinelog.dao.DaoSession;
import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.dao.LocalKinoDao;
import com.bumptech.glide.Glide;
import com.github.zagum.switchicon.SwitchIconView;
import com.uwetrottmann.tmdb2.entities.Movie;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.Query;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.alcidauk.cinelog.AddKino.RESULT_ADD_REVIEW;

/**
 * Created by alcidauk on 15/02/18.
 */
public class KinoResultsAdapter extends BaseAdapter {

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
        AddKino.ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.search_result_item, null);
            holder = new AddKino.ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (AddKino.ViewHolder) convertView.getTag();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
