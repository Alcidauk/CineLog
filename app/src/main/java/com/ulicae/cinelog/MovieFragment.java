package com.ulicae.cinelog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ulicae.cinelog.dto.KinoDto;
import com.ulicae.cinelog.dto.KinoService;
import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieFragment extends Fragment {

    FloatingActionButton fab;

    @BindView(R.id.kino_list)
    ListView kino_list;

    KinoListAdapter kino_adapter;

    List<KinoDto> kinos;

    private KinoService kinoService;

    private static final int RESULT_ADD_KINO = 2;
    static final int RESULT_VIEW_KINO = 4;

    private int LIST_VIEW_STATE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        kinoService = new KinoService(((KinoApplication) getActivity().getApplication()).getDaoSession());

        createListView(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this, view);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddKino.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        createListView(LIST_VIEW_STATE);
        System.out.println("onStart");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                kinos.set(pos, (KinoDto) Parcels.unwrap(data.getParcelableExtra("kino")));
                kino_adapter.notifyDataSetChanged();
                System.out.println("Result Ok");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("Result Cancelled");
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
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

    private void createListView(int order) {
        if (kino_list != null) {
            //date added
            switch (order) {
                case 1:
                    // TODO reimplement reverse
                    kinos = kinoService.getAllKinos();
                    break;
                case 2:
                    kinos = kinoService.getAllKinos();
                    break;
                case 3:
                    kinos = kinoService.getKinosByRating(false);
                    break;
                case 4:
                    kinos = kinoService.getKinosByRating(true);
                    break;
                case 5:
                    //kinos = get_year_desc.list();
                    break;
                case 6:
                    //kinos = get_year_asc.list();
                    break;
                default:
                    kinos = kinoService.getAllKinos();
                    break;
            }
            LIST_VIEW_STATE = order;

            kino_adapter = new KinoListAdapter(getContext(), kinos);
            kino_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(final AdapterView<?> view, View parent, final int position, long rowId) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(R.string.delete_kino_dialog)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Delete the kino
                                    KinoDto kinoDto = kinos.get(position);
                                    kinos.remove(position);
                                    kinoService.deleteKino(kinoDto);

                                    kino_adapter.notifyDataSetChanged();
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

class KinoListAdapter extends ArrayAdapter<KinoDto> {

    private List<KinoDto> kinos;

    public KinoListAdapter(@NonNull Context context, @NonNull List<KinoDto> objects) {
        super(context, R.layout.main_result_item, objects);
        this.kinos = objects;
    }

    public long getItemId(int position) {
        return 0;
    }

    // createOrUpdate a new RelativeView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.main_result_item, parent, false);
        }

        TextView kinoTitleTextView = (TextView) convertView.findViewById(R.id.main_result_kino_title);
        TextView kinoYearTextView = (TextView) convertView.findViewById(R.id.main_result_kino_year);
        ImageView kinoPosterImageView = (ImageView) convertView.findViewById(R.id.main_result_kino_poster);
        RatingBar kinoRatingRatingBar = (RatingBar) convertView.findViewById(R.id.main_result_kino_rating_bar_small);

        KinoDto movie = getItem(position);

        if (movie != null) {
            kinoTitleTextView.setText(movie.getTitle());

            if (movie.getYear() != 0) {
                kinoYearTextView.setText(String.valueOf(movie.getYear()));
            } else {
                kinoYearTextView.setText("");
            }

            kinoPosterImageView.setLayoutParams(new RelativeLayout.LayoutParams(120, 150));
            if (movie.getPosterPath() != null) {
                Glide.with(getContext())
                        .load("https://image.tmdb.org/t/p/w185" + movie.getPosterPath())
                        .centerCrop()
                        .crossFade()
                        .into(kinoPosterImageView);
            } else {
                Glide.with(getContext())
                        .load(R.drawable.noimage)
                        .centerCrop()
                        .crossFade()
                        .into(kinoPosterImageView);
            }

            kinoRatingRatingBar.setStepSize(0.5f);

            int maxRating;
            if (movie.getMaxRating() == null) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                String defaultMaxRateValue = prefs.getString("default_max_rate_value", "5");
                maxRating = Integer.parseInt(defaultMaxRateValue);
            } else {
                maxRating = movie.getMaxRating();
            }
            kinoRatingRatingBar.setNumStars(maxRating);

            if (movie.getRating() != null) {
                kinoRatingRatingBar.setRating(movie.getRating());
            } else {
                kinoRatingRatingBar.setRating(0);
            }
        }

        return convertView;
    }

}
