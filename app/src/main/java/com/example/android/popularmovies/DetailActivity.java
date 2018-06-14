package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.popularmovies.utilities.JSONUtils;
import com.squareup.picasso.Picasso;
import  com.example.android.popularmovies.model.Movie;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView posterIv = findViewById(R.id.image_iv);

        Movie movie = (Movie) getIntent().getParcelableExtra("parcel_data");
        System.out.println("HALLLLLLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" + movie.getTitle());
        if (movie == null) {
            // Movie data unavailable
            closeOnError();
            return;
        }

        populateUI(movie);
        Picasso.with(this)
                .load(movie.getPoster())
                .into(posterIv);

        setTitle(movie.getTitle());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Movie movie) {
        TextView titleView = (TextView) findViewById(R.id.title_tv);
        titleView.setText(movie.getTitle());

        TextView originalTitleView = (TextView) findViewById(R.id.original_title_tv);
        titleView.setText(movie.getOriginalTitle());

        TextView releaseDateView = (TextView) findViewById(R.id.release_date_tv);
        releaseDateView.setText(movie.getReleaseDate());

        TextView voteView = (TextView) findViewById(R.id.vote_tv);
        voteView.setText(Double.toString(movie.getVoteAverage()));

        TextView plotView = (TextView) findViewById(R.id.plot_tv);
        plotView.setText(movie.getPlot());
    }
}
