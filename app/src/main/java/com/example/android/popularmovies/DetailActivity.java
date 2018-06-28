package com.example.android.popularmovies;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.widget.Button;

import android.widget.ImageView;


import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.MovieEntry;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.ui.ReviewFragment;
import com.example.android.popularmovies.ui.TrailerFragment;
import com.example.android.popularmovies.utilities.JSONUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;
import  com.example.android.popularmovies.model.Movie;


import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    boolean isFavourite;
    private String favouriteTitle;

    // Fields for views
    Button mButton;

    //Fields for movie's info
    private int id;
    private String title;
    private String originalTitle;
    private String releaseDate;
    private String voteAverage;
    private String poster;
    private String plot;

    // Member variable for the Database
    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Movie movie = (Movie) getIntent().getParcelableExtra("parcel_data");

        if (movie == null) {
            // Movie data unavailable
            closeOnError();
            return;
        }
        mDb = AppDatabase.getInstance(getApplicationContext());

        //Set views
        ImageView posterIv = findViewById(R.id.image_iv);
        mButton = findViewById(R.id.saveButton);

        //Set movie´s info
        id = movie.getId();
        originalTitle = movie.getOriginalTitle();
        releaseDate = movie.getReleaseDate();
        voteAverage = movie.getVoteAverage();
        poster = movie.getPoster();
        plot = movie.getPlot();
        title = movie.getTitle();


        if (savedInstanceState != null)
        {
            // Load variables here and overwrite the default values
            isFavourite = savedInstanceState.getBoolean("isFavourite", true);
            Log.d("SaveInstance FAVOURITE","IS IT??" + isFavourite);
            setButton(isFavourite);
        }
        else{
            loadMovieDetailData();
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                favouriteTitle = mDb.movieDao().titleById(id);
                Log.d("favourite Title", "tryFSucc" + favouriteTitle);
                if (favouriteTitle == null) {
                    isFavourite = false;
                } else {
                    isFavourite = true;
                }
               setButton(isFavourite);

                Log.d("Is FAVOURITE?????","IS IT??" + isFavourite);
            }
        });}

        populateUI(movie);
        Picasso.with(this)
                .load(movie.getPoster())
                .into(posterIv);

        setTitle(title);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });

    }


    public void setButton(boolean isFavourite){
        if (isFavourite == true){
            mButton.setText("MY FAVOURITE MOVIE");}
        else if (isFavourite == false) {mButton.setText("NEW FAVOURITE?");
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }


    private void loadMovieDetailData() {
        if (isConnected() == true) {
            new FetchDetailTrailerTask2().execute();
            new FetchDetailReviewTask().execute();
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

    public class FetchDetailTrailerTask2 extends AsyncTask<String, Void, Trailer[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Trailer[] doInBackground(String... params) {

            URL trailerRequestUrl = NetworkUtils.buildVideoUrl(id);

            try {
                String jsonTrailerResponse = NetworkUtils.getResponseFromHttpUrl(trailerRequestUrl);

                return JSONUtils.getTrailerDataFromJson(DetailActivity.this, jsonTrailerResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Trailer[] trailerData) {

            if (trailerData != null) {
                // Create a new head  TrailerFragment
                TrailerFragment trailerFragment = new TrailerFragment();

                // Set the trailers for the head fragment and set the position to the second image in the list
                trailerFragment.setTrailers(trailerData);

                // Add the fragment to its container using a FragmentManager and a Transaction
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.trailer_container, trailerFragment)
                        .commit();
            }
        }
    }


    public class FetchDetailReviewTask extends AsyncTask<String, Void, Review[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Review[] doInBackground(String... params) {

            URL trailerRequestUrl = NetworkUtils.buildReviewUrl(id);

            try {
                String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(trailerRequestUrl);

                return JSONUtils.getReviewDataFromJson(DetailActivity.this, jsonReviewResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Review[] reviewData) {
            if (reviewData != null) {
                // Create a new head  ReviewFragment
                ReviewFragment reviewFragment = new ReviewFragment();

                // Set the reviews for the head fragment
                reviewFragment.setReviews(reviewData);

                // Add the fragment to its container using a FragmentManager and a Transaction
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.review_container, reviewFragment)
                        .commit();
            }
        }
    }


    //Define what happens when "favourite" button is clicked
    public void onSaveButtonClicked() {

        if (!isFavourite) {
            final MovieEntry movieEntry = new MovieEntry(id, title, originalTitle, releaseDate, voteAverage, poster, plot);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                        // insert new task
                        mDb.movieDao().insertMovie(movieEntry);
                    Log.d("set task", "new favourite movie" + mDb.movieDao().titleById(id));
                }
            });
            mButton.setText(R.string.my_favourite);
            isFavourite = true;
        }
        else {
            // Delete from favourites
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().deleteById(id);
                    Log.d("delete task","deleted movie: " + mDb.movieDao().titleById(id));
                }
            });

            mButton.setText(R.string.new_favourite);
            isFavourite = false;
        }

    }

    //Save info about "is favourite?"
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("isFavourite", isFavourite);
    }
}
