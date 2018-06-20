package com.example.android.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.utilities.JSONUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;
import  com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.TrailerAdapter.TrailerAdapterOnClickHandler;
import com.example.android.popularmovies.ReviewAdapter.ReviewAdapterOnClickHandler;

import java.net.URL;

public class DetailActivity extends AppCompatActivity implements TrailerAdapterOnClickHandler, ReviewAdapterOnClickHandler {

    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private TextView mErrorMessageDisplay;
    private int id;

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

        id = movie.getId();
        populateUI(movie);
        Picasso.with(this)
                .load(movie.getPoster())
                .into(posterIv);

        setTitle(movie.getTitle());

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display_detail);
        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trailers);
        /* This TextView is used to display errors and will be hidden if there are no errors */

       LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mTrailerRecyclerView.setLayoutManager(layoutManager);
        //mRecyclerView.setHasFixedSize(true);
        /*
         * The ForecastAdapter is responsible for linking our data with the Views that
         * will end up displaying our data.
         */
        mTrailerAdapter = new TrailerAdapter(this);
        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        loadTrailerData();


        mReviewRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_reviews);
        /* This TextView is used to display errors and will be hidden if there are no errors */

        LinearLayoutManager layoutManagerReviews = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mReviewRecyclerView.setLayoutManager(layoutManagerReviews);
        //mRecyclerView.setHasFixedSize(true);
        /*
         * The ForecastAdapter is responsible for linking our data with the Views that
         * will end up displaying our data.
         */
        mReviewAdapter = new ReviewAdapter(this);
        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mReviewRecyclerView.setAdapter(mReviewAdapter);
        loadReviewData();
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void loadTrailerData() {
        if (isConnected() == true) {
            showTrailerDataView();
            new FetchDetailTrailerTask().execute();
        }
        else {
            showErrorMessage();
        }
    }

    private void loadReviewData() {
        if (isConnected() == true) {
            showReviewDataView();
            new FetchDetailReviewTask().execute();
        }
        else {
            showErrorMessage();
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

    /**
     * This method will make the View for the trailers for movie data visible and
     * hide the error message
     */
    private void showTrailerDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie data is visible */
        mTrailerRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showReviewDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie data is visible */
        mReviewRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the trailer
     * View.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mTrailerRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(Trailer singleTrailer) {
        Context context = this;
        Trailer trailer;
        trailer = singleTrailer;
        String key = trailer.getTrailerKey();

        // COMPLETED (5) Create a String that contains a URL ( make sure it starts with http:// or https:// )
        String urlAsString = key;

        // COMPLETED (6) Replace the Toast with a call to openWebPage, passing in the URL String from the previous step
        openWebPage(urlAsString);
    }

    @Override
    public void onClick(Review singleReview) {
        Context context = this;
        Review review;
        review = singleReview;
    }

    private void openWebPage(String url) {
        // COMPLETED (2) Use Uri.parse to parse the String into a Uri
        /*
         * We wanted to demonstrate the Uri.parse method because its usage occurs frequently. You
         * could have just as easily passed in a Uri as the parameter of this method.
         */
        Uri webpage = Uri.parse(url);

        // COMPLETED (3) Create an Intent with Intent.ACTION_VIEW and the webpage Uri as parameters
        /*
         * Here, we create the Intent with the action of ACTION_VIEW. This action allows the user
         * to view particular content. In this case, our webpage URL.
         */
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        // COMPLETED (4) Verify that this Intent can be launched and then call startActivity
        /*
         * This is a check we perform with every implicit Intent that we launch. In some cases,
         * the device where this code is running might not have an Activity to perform the action
         * with the data we've specified. Without this check, in those cases your app would crash.
         */
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public class FetchDetailTrailerTask extends AsyncTask<String, Void, Trailer[]> {

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
               showTrailerDataView();
               mTrailerAdapter.setTrailerData(trailerData);
            } else {
               showErrorMessage();
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
                showReviewDataView();
                mReviewAdapter.setReviewData(reviewData);
            } else {
                showErrorMessage();
            }
        }
    }

}
