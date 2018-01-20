package com.alcidauk.cinelog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.greendao.query.Query;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.alcidauk.cinelog.dao.DaoSession;
import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.dao.LocalKinoDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddReview extends AppCompatActivity {

    @BindView(R.id.kino_rating_bar)
    RatingBar rating_bar;
    @BindView(R.id.kino_review_text)
    EditText review_text;
    @BindView(R.id.kino_review_date)
    TextView review_date;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    LocalKino kino;
    DaoSession daoSession;
    LocalKinoDao localKinoDao;
    Query<LocalKino> movie_id_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        ButterKnife.bind(this);


        daoSession = ((KinoApplication) getApplicationContext()).getDaoSession();
        localKinoDao = daoSession.getLocalKinoDao();

        kino = (LocalKino) Parcels.unwrap(getIntent().getParcelableExtra("kino"));

        //movie_id_query = localKinoDao.queryBuilder().where(LocalKinoDao.Properties.Movie_id.eq(1)).limit(1).build();
        //movie_id_query.setParameter(0, kino.movie_id);
        //List<LocalKino> movies = movie_id_query.list();


        rating_bar.setRating(kino.getRating());

        if (kino.getReview() != null) {
            review_text.setText(kino.getReview());
        }

        if(kino.getReview_date() != null){
            String review_date_as_string = null;
            if(kino.getReview_date() != null){
                review_date_as_string =
                        new SimpleDateFormat("dd/MM/yyyy").format(kino.getReview_date());
            }
            review_date.setText(review_date_as_string);
        }


        toolbar.setTitle("Add Review: " + kino.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                System.out.println("Saved");
                if (rating_bar.getRating() == 0 && (review_text.getText().toString().equals("") || review_text.getText().toString().isEmpty())) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Error no review to save.",
                            Toast.LENGTH_LONG);
                    t.show();
                } else {
                    kino.setRating(rating_bar.getRating());
                    kino.setReview(review_text.getText().toString());
                    localKinoDao.save(kino);
                    localKinoDao.detachAll();

                    Intent returnIntent = getIntent();
                    returnIntent.putExtra("kino", Parcels.wrap(kino));
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }

    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            final Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);

            LocalKino kino = ((AddReview) getActivity()).kino;
            kino.setReview_date(c.getTime());
            String review_date_as_string = null;
            if(kino.getReview_date() != null){
                review_date_as_string =
                        new SimpleDateFormat("dd/MM/yyyy").format(kino.getReview_date());
            }
            ((AddReview)getActivity()).review_date.setText(review_date_as_string);
            // Do something with the date chosen by the user
        }
    }
}