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
import android.widget.RelativeLayout;

import com.alcidauk.cinelog.dao.DaoSession;
import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.dao.LocalKinoDao;
import com.alcidauk.cinelog.dto.KinoDto;
import com.alcidauk.cinelog.dto.KinoService;
import com.bumptech.glide.Glide;
import com.github.zagum.switchicon.SwitchIconView;
import com.uwetrottmann.tmdb2.entities.Movie;

import org.greenrobot.greendao.query.DeleteQuery;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.alcidauk.cinelog.AddKino.RESULT_EDIT_REVIEW;

/**
 * Created by alcidauk on 15/02/18.
 */
public class KinoResultsAdapter extends BaseAdapter {

    private Context mContext;
    private List<Movie> mData;
    private int[] mWatchedData;
    private int[] mReveiewedData;
    private SimpleDateFormat sdf;

    private DaoSession daoSession;
    private DeleteQuery<LocalKino> delete_by_id_query;

    private KinoService kinoService;

    public KinoResultsAdapter(Context c, List<Movie> v) {
        mContext = c;
        if (v != null) {
            mData = v;
            mWatchedData = new int[mData.size()];
            mReveiewedData = new int[mData.size()];

            sdf = new SimpleDateFormat("yyyy");

            daoSession = ((KinoApplication) mContext.getApplicationContext()).getDaoSession();

            delete_by_id_query = daoSession.getLocalKinoDao()
                    .queryBuilder()
                    .where(LocalKinoDao.Properties.Tmdb_id.eq(1)).buildDelete();

            kinoService = new KinoService(daoSession);
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

    // createOrUpdate a new RelativeView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO clean that

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
        holder.add_review_button.setEnabled(false);

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
            holder.poster.setLayoutParams(new RelativeLayout.LayoutParams(120, 150));
            Glide.with(mContext)
                    .load("https://image.tmdb.org/t/p/w185" + movie.poster_path)
                    .centerCrop()
                    .crossFade()
                    .into(holder.poster);
        } else {
            if (holder.poster != null)
                holder.poster.setImageResource(0);
        }

        final KinoDto kino = new KinoDto(
                null,
                movie.id.longValue(),
                movie.title,
                null,
                null,
                null,
                null,
                movie.poster_path,
                movie.overview,
                year_i,
                year
        );

        final Integer m_id = movie.id;
        holder.add_review_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditReview.class);

                KinoDto kinoByTmdbMovieId = kinoService.getKinoByTmdbMovieId(m_id);

                if (kinoByTmdbMovieId == null) {
                    intent.putExtra("kino", Parcels.wrap(kino));
                } else {
                    intent.putExtra("kino", Parcels.wrap(kinoByTmdbMovieId));
                }

                ((Activity) mContext).startActivityForResult(intent, RESULT_EDIT_REVIEW);
            }
        });


        int get_res = mWatchedData[position];
        if (get_res != 0) {
            if (get_res == 1) {
                get_res = mReveiewedData[position];
                if (get_res == 1) {
                    holder.add_review_button.setEnabled(true);
                }
            }
        } else {
            KinoDto kinoByTmdbMovieId = kinoService.getKinoByTmdbMovieId(movie.id);
            if (kinoByTmdbMovieId != null) {
                System.out.println("Result found");
                mWatchedData[position] = 1;

                if (kinoByTmdbMovieId.getReview() != null || kinoByTmdbMovieId.getRating() != 0.0f) {
                    mReveiewedData[position] = 1;
                    holder.add_review_button.setEnabled(true);
                } else {
                    mReveiewedData[position] = -1;
                }
            } else {
                mWatchedData[position] = -1;
            }
        }

        holder.add_review_button.setFocusable(false);

        return convertView;
    }
}
