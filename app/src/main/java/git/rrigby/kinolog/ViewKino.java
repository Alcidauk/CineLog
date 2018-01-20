package git.rrigby.kinolog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import git.rrigby.kinolog.dao.LocalKino;

public class ViewKino extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;

    @BindView(R.id.view_poster) ImageView poster;
    @BindView(R.id.view_title) TextView title;
    @BindView(R.id.view_year) TextView year;
    @BindView(R.id.view_overview) TextView overview;
    @BindView(R.id.view_rating) RatingBar rating;
    @BindView(R.id.view_review) TextView review;

    LocalKino kino;
    int position;
    boolean editted = false;

    private static final int RESULT_ADD_REVIEW = 3;

    @OnClick(R.id.fab)
    public void onClick(View view) {
        Intent intent = new Intent(this, AddReview.class);
        intent.putExtra("kino", Parcels.wrap(kino));
        startActivityForResult(intent, RESULT_ADD_REVIEW);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_kino);
        ButterKnife.bind(this);

        kino = (LocalKino) Parcels.unwrap(getIntent().getParcelableExtra("kino"));
        position = getIntent().getIntExtra("kino_position",-1);
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
    protected void onStart() {
        super.onStart();
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w185"+kino.getPoster_path())
                .centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .crossFade()
                .into(poster);
        title.setText(kino.getTitle());
        year.setText(kino.getRelease_date());

        overview.setText(kino.getOverview());

        rating.setRating(kino.getRating());
        review.setText(kino.getReview());

        toolbar.setTitle(kino.getTitle());
        System.out.println("onStart()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_ADD_REVIEW) {
            if(resultCode == Activity.RESULT_OK) {
                //addNewLocation(data);
                kino = (LocalKino) Parcels.unwrap(data.getParcelableExtra("kino"));
                editted = true;
                System.out.println("Result Ok");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("Result Cancelled");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if(editted) {
                    Intent returnIntent = getIntent();
                    returnIntent.putExtra("kino", Parcels.wrap(kino));
                    returnIntent.putExtra("kino_position", position);
                    setResult(Activity.RESULT_OK, returnIntent);
                }

                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }

}
