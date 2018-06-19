package com.example.android.popularmovies;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.utilities.JSONUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;
import  com.example.android.popularmovies.model.Movie;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView posterIv = findViewById(R.id.image_iv);

        Movie movie = (Movie) getIntent().getParcelableExtra("parcel_data");

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
        loadTrailerData();

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void loadTrailerData() {
        if (isConnected() == true) {
          //  showMovieDataView();
            new FetchDetailMovieTask().execute();
        }
        else {
            //showErrorMessage();
        }
    }

    /**Check for internet connection before making the actual request to the API,
     * so the device can save one unneeded network call given that we know it will
     * fail to fetch the movies.
     */

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void populateUI(Movie movie) {
        TextView originalTitleView = (TextView) findViewById(R.id.original_title_tv);
        originalTitleView.setText(movie.getOriginalTitle());

        TextView releaseDateView = (TextView) findViewById(R.id.release_date_tv);
        releaseDateView.setText(movie.getReleaseDate());

        TextView voteView = (TextView) findViewById(R.id.vote_tv);
        voteView.setText(movie.getVoteAverage());

        TextView plotView = (TextView) findViewById(R.id.plot_tv);
        plotView.setText(movie.getPlot());
    }

    public class FetchDetailMovieTask extends AsyncTask<String, Void, Trailer[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Trailer[] doInBackground(String... params) {

            URL trailerRequestUrl = NetworkUtils.buildVideoUrl();

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(trailerRequestUrl);

                return JSONUtils.getMovieDataFromJson(DetailActivity.this, jsonMovieResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieData) {
            if (movieData != null) {
               // showMovieDataView();
               // mMovieAdapter.setMovieData(movieData);
            } else {
               // showErrorMessage();
            }
        }
    }

    /**
     * This method will make the View for the trailers for movie data visible and
     * hide the error message
     */
    //private void showMovieDataView() {
        /* First, make sure the error is invisible */
      //  mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie data is visible */
       // mRecyclerView.setVisibility(View.VISIBLE);
    //}

    /**
     * This method will make the error message visible and hide the trailer
     * View.
     */
   // private void showErrorMessage() {
        /* First, hide the currently visible data */
    //    mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
    //    mErrorMessageDisplay.setVisibility(View.VISIBLE);
  //  }

}
