package com.example.android.popularmovies;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.MovieDao;
import com.example.android.popularmovies.database.MovieEntry;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.utilities.JSONUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;
import  com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.TrailerAdapter.TrailerAdapterOnClickHandler;
import com.example.android.popularmovies.ReviewAdapter.ReviewAdapterOnClickHandler;

import java.net.URL;
import java.util.Date;
import java.util.List;

import static com.example.android.popularmovies.R.color.colorAccentDark;
import static com.example.android.popularmovies.R.color.colorPrimaryLight;

public class DetailActivity extends AppCompatActivity implements TrailerAdapterOnClickHandler, ReviewAdapterOnClickHandler {

    private static final String TAG = DetailActivity.class.getSimpleName();

    boolean isFavourite;

    // Fields for views
    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private TextView mErrorMessageDisplay;
    Button mButton;
    private String favouriteTitle;

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

        // COMPLETED (9) Remove the logging and the call to loadTaskById, this is done in the ViewModel now
        // COMPLETED (10) Declare a AddTaskViewModelFactory using mDb and mTaskId
        DetailActivityViewModelFactory factory = new DetailActivityViewModelFactory(mDb, id);
        // COMPLETED (11) Declare a AddTaskViewModel variable and initialize it by calling ViewModelProviders.of
        // for that use the factory created above AddTaskViewModel
        final DetailActivityViewModel viewModel
                = ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);


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
            }
        });

       /*
        try{
        favouriteTitle= viewModel.getFavouriteTitle(id);

        }
        catch (Exception e){
            favouriteTitle = null;
            Log.d("favourite Title","tryFail" +  favouriteTitle);
                    }*/



        Log.d("Is FAVOURITE?????","IS IT??" + isFavourite);

        populateUI(movie);
        Picasso.with(this)
                .load(movie.getPoster())
                .into(posterIv);

        setTitle(title);

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

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    public final int getIdFromDetail(){
        return id;
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

        if (isFavourite == true){
            mButton.setText("MY FAVOURITE MOVIE");}
            else {mButton.setText("NEW FAVOURITE?");
        }
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
        String urlAsString = key;
        openWebPage(urlAsString);
    }

    @Override
    public void onClick(Review singleReview) {
        Context context = this;
        Review review;
        review = singleReview;
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


    public void onSaveButtonClicked() {

        if (isFavourite == false) {
            final MovieEntry movieEntry = new MovieEntry(id, title, originalTitle, releaseDate, voteAverage, poster, plot);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                        // insert new task
                        mDb.movieDao().insertTask(movieEntry);
                    Log.d("set task", "set task TITLEENTERED?" + mDb.movieDao().titleById(id));
                }
            });
            mButton.setText("MY FAVOURITE MOVIE");
            isFavourite = true;
        }
        else {
            // Delete from favourites
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().deleteById(id);
                    Log.d("delete task","set task TITLEENTERED?" + mDb.movieDao().titleById(id));
                }
            });

            mButton.setText("NEW FAVOURITE?");
            isFavourite = false;
        }

    }
}
