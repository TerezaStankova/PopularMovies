package com.example.android.popularmovies;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.MovieEntry;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.ui.TrailerFragment;
import com.example.android.popularmovies.utilities.JSONUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;
import  com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.TrailerAdapter.TrailerAdapterOnClickHandler;
import com.example.android.popularmovies.ReviewAdapter.ReviewAdapterOnClickHandler;

import java.net.URL;

public class DetailActivity extends AppCompatActivity implements TrailerAdapterOnClickHandler, ReviewAdapterOnClickHandler {

    boolean isFavourite;
    private String favouriteTitle;

    // Fields for views
    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private TextView mErrorMessageDisplay;
    private TextView mReviewLabel;
    private TextView mTrailerLabel;
    private LinearLayoutManager layoutManagerReviews;
    Button mButton;


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

        if(savedInstanceState == null) {

            new FetchDetailTrailerTask2().execute();

        }

        ImageView posterIv = findViewById(R.id.image_iv);

        mDb = AppDatabase.getInstance(getApplicationContext());


        mButton = findViewById(R.id.saveButton);


        Movie movie = (Movie) getIntent().getParcelableExtra("parcel_data");

        if (movie == null) {
            // Movie data unavailable
            closeOnError();
            return;
        }

           id = movie.getId();
           originalTitle = movie.getOriginalTitle();
           releaseDate = movie.getReleaseDate();
           voteAverage = movie.getVoteAverage();
           poster = movie.getPoster();
           plot = movie.getPlot();
           title = movie.getTitle();

        // call to loadTaskById, this is done in the ViewModel
        DetailActivityViewModelFactory factory = new DetailActivityViewModelFactory(mDb, id);
        final DetailActivityViewModel viewModel
                = ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);

        if (savedInstanceState != null)
        {
            // Load variables here and overwrite the default values
            isFavourite = savedInstanceState.getBoolean("isFavourite", true);
            Log.d("SaveInstance FAVOURITE","IS IT??" + isFavourite);
            setButton(isFavourite);
        }
        else{
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

        /* This TextView is used to display errors and will be hidden if there are no errors
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display_detail);
        mTrailerLabel = (TextView) findViewById(R.id.trailers_label);
        mReviewLabel = (TextView) findViewById(R.id.reviews_label);
        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trailers);


       LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
       layoutManager.setAutoMeasureEnabled(true);
        mTrailerRecyclerView.setLayoutManager(layoutManager);
        mTrailerRecyclerView.setHasFixedSize(false);
        /*
         * The ForecastAdapter is responsible for linking our data with the Views that
         * will end up displaying our data.

        mTrailerAdapter = new TrailerAdapter(this);
        /* Setting the adapter attaches it to the RecyclerView in our layout.
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        loadTrailerData();


        mReviewRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_reviews);
        /* This TextView is used to display errors and will be hidden if there are no errors

        layoutManagerReviews = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManagerReviews.setAutoMeasureEnabled(true);

        mReviewRecyclerView.setLayoutManager(layoutManagerReviews);
        mReviewRecyclerView.setHasFixedSize(false);
        /*
         * The ForecastAdapter is responsible for linking our data with the Views that
         * will end up displaying our data.
         */
        mReviewAdapter = new ReviewAdapter(this);
        /* Setting the adapter attaches it to the RecyclerView in our layout.
        mReviewRecyclerView.setAdapter(mReviewAdapter);
        loadReviewData();*/

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

    /*
    private void loadTrailerData() {
        if (isConnected() == true) {
            showTrailerDataView();
            new FetchDetailTrailerTask().execute();
        }
        else {
            showTrailerErrorMessage();
        }
    }

    private void loadReviewData() {
        if (isConnected() == true) {
            showReviewDataView();
            new FetchDetailReviewTask().execute();
            //new FetchDetailTrailerTask2().execute();
        }
        else {
            showReviewErrorMessage();
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

    private void showTrailerDataView() {
        /* First, make sure the error is invisible
        //mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie data is visible
        mTrailerRecyclerView.setVisibility(View.VISIBLE);
        mTrailerLabel.setVisibility(View.VISIBLE);
    }

    private void showReviewDataView() {
        /* First, make sure the error is invisible
        //mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie data is visible
        mReviewRecyclerView.setVisibility(View.VISIBLE);
        mReviewLabel.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the trailer
     * View.
     */

    private void showReviewErrorMessage() {
        /* First, hide the currently visible data */
        mReviewRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mReviewLabel.setVisibility(View.INVISIBLE);
    }

    private void showTrailerErrorMessage() {
        /* First, hide the currently visible data */
        mReviewRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mTrailerLabel.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(Trailer singleTrailer) {
        Trailer trailer;
        trailer = singleTrailer;
        String key = trailer.getTrailerKey();
        openWebPage(key);
    }

    @Override
    public void onClick(Review singleReview) {
    }

    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);

       // This action allows the user to view our webpage URL.
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        // Verify that this Intent can be launched and then call startActivity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /*

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
               showTrailerErrorMessage();
            }
        }
    }*/



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

        // Retrieve list index values that were sent through an intent; use them to display the desired Android-Me body part image
            // Use setListindex(int index) to set the list index for all BodyPartFragments

            // Create a new head  TrailerFragment
            TrailerFragment trailerFragment = new TrailerFragment();

            // Set the list of image id's for the head fragment and set the position to the second image in the list
            trailerFragment.setTrailers(trailerData);

            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.trailer_container, trailerFragment)
                    .commit();


            /*
            if (trailerData != null) {
                for (final Trailer trailer : trailerData) {
                    if (trailer != null) {

                    View mMovieTrailerItem = LayoutInflater.from(DetailActivity.this).inflate(
                            R.layout.trailer_list_item, null);

                    mMovieTrailerItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openWebPage(trailer.getTrailerKey());
                        }
                    });


                    TextView mMovieTrailerTitle = (TextView) mMovieTrailerItem.findViewById(R.id.trailer_name);
                    mMovieTrailerTitle.setText(trailer.getTrailerName());

                    LinearLayout mMovieTrailerC = (LinearLayout) mMovieTrailerItem.findViewById(R.id.container_trailer);
                        if (mMovieTrailerC != null) {
                    mMovieTrailerC.addView(mMovieTrailerItem);}}
                }

            } else {
                showTrailerErrorMessage();
            }*/
        }



    }

    /*
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
                showReviewErrorMessage();
            }
        }
    }*/


    public void onSaveButtonClicked() {

        if (!isFavourite) {
            final MovieEntry movieEntry = new MovieEntry(id, title, originalTitle, releaseDate, voteAverage, poster, plot);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                        // insert new task
                        mDb.movieDao().insertTask(movieEntry);
                    Log.d("set task", "set task TITLEENTERED?" + mDb.movieDao().titleById(id));
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
                    Log.d("delete task","delete task TITLEENTERED?" + mDb.movieDao().titleById(id));
                }
            });

            mButton.setText(R.string.new_favourite);
            isFavourite = false;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("isFavourite", isFavourite);
    }
}
