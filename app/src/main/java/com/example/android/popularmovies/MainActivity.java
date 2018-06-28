package com.example.android.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.MovieEntry;
import com.example.android.popularmovies.utilities.JSONUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.MovieAdapter.MovieAdapterOnClickHandler;
import com.example.android.popularmovies.model.Movie;

import android.widget.AdapterView.OnItemSelectedListener;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler, OnItemSelectedListener {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private int optionSelected = 0;
    private boolean newMenu;
    private Spinner spinner;
    private Movie[] movies;

    private AppDatabase mDb;

    // Final String to store state information about the movies
    public static final String MOVIES = "movies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        final int columns = getResources().getInteger(R.integer.gallery_columns);

        GridLayoutManager layoutManager = new GridLayoutManager(this, columns, GridLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        /*
         * The mMovieAdapter is responsible for linking data with the Views that
         * will end up displaying the data.
         */
        mMovieAdapter = new MovieAdapter(this);
        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mMovieAdapter);
        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null)
        {
            // Load variables here and overrite the default values
            optionSelected = savedInstanceState.getInt("mySpinner", 0);
            // Load the saved state (the array of trailers) if there is one
            movies = (Movie[]) savedInstanceState.getParcelableArray(MOVIES);
            Log.d("SavedInstanceNotNull", optionSelected + "a");
        }

        /* Load the data. */
        if(optionSelected != 2){
            Log.d("Optionselected", optionSelected + "a");
            if (movies != null){
                showMovieDataView();
                mMovieAdapter.setMovieData(null);
                mMovieAdapter.setMovieData(movies);
                Log.d("MoviesNotNull", optionSelected + "a");
            }
            else {
                loadMovieData();
                Log.d("MoviesNull", optionSelected + "a");
            }
        }

        else {setupViewModel();
            Log.d("setupViewModel", optionSelected + "a");
        }
    }

    /**
     * This method will tell some background method to get the data in the background.
     */
    private void loadMovieData() {
            if (isConnected() == true) {
                showMovieDataView();
                new FetchMovieTask().execute();
            } else {
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

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param singleMovie The movie for the poster that was clicked
     */
    @Override
    public void onClick(Movie singleMovie) {
        Context context = this;
        Movie movie;
        movie = singleMovie;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("parcel_data", movie);
        startActivity(intentToStartDetailActivity);
    }

    /**
     * This method will make the View for the movie data visible and
     * hide the error message
     */
    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movie
     * View.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {

           URL movieRequestUrl = NetworkUtils.buildUrl();

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                return JSONUtils.getMovieDataFromJson(MainActivity.this, jsonMovieResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                movies = new Movie[movieData.length];
                movies = movieData;
                showMovieDataView();
                mMovieAdapter.setMovieData(movieData);
            } else {
                showErrorMessage();
            }
        }
    }


    private void setupViewModel() {
        showMovieDataView();
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                Log.d("message", "Updating list of tasks from LiveData in ViewModel");
                mMovieAdapter.setFavouriteMovies(movieEntries);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        newMenu = true;
        Log.d("CreatingMenu", optionSelected + "newMenu" + newMenu);

        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.order, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        MenuItem mSpinnerItem;
        mSpinnerItem = menu.findItem( R.id.spinner );

        View view = mSpinnerItem.getActionView();
        if (view instanceof Spinner)
        {
            spinner = (Spinner) view;
            spinner.setOnItemSelectedListener(this);

            ArrayAdapter aa = ArrayAdapter.createFromResource( this,
                    R.array.spinner_data,
                    android.R.layout.simple_spinner_dropdown_item );
            spinner.setAdapter(aa);
        }

        spinner.setSelection(optionSelected);

        Log.d("SelectionSet", optionSelected + "a");

       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
           mMovieAdapter.setMovieData(null);
           loadMovieData();
           return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (newMenu == false) {
            Log.d("onItemSelectedTriggered", position + "a");
            switch (position) {
                case 0:
                    // Order by popularity when the popularity menu item is clicked
                    NetworkUtils.changeBaseUrl("popular");
                    mMovieAdapter.setMovieData(null);
                    loadMovieData();
                    break;
                case 1:
                    // Order by top rated when the top rated menu item is clicked
                    NetworkUtils.changeBaseUrl("rating");
                    mMovieAdapter.setMovieData(null);
                    loadMovieData();
                    break;

                case 2:
                    //List favourite movies
                    mMovieAdapter.setMovieData(null);
                    setupViewModel();
                    break;
            }
        } else {
            newMenu = false;
            if (movies == null){
                Log.d("NEWmoviesNull", position + "newMenu" + newMenu);
                switch (position) {
                    case 0:
                        // Order by popularity when the popularity menu item is clicked
                        NetworkUtils.changeBaseUrl("popular");
                        mMovieAdapter.setMovieData(null);
                        loadMovieData();
                        break;
                    case 1:
                        // Order by top rated when the top rated menu item is clicked
                        NetworkUtils.changeBaseUrl("rating");
                        mMovieAdapter.setMovieData(null);
                        loadMovieData();
                        break;

                    case 2:
                        //List favourite movies
                        mMovieAdapter.setMovieData(null);
                        setupViewModel();
                        break;
                }
            } else {
                Log.d("NEWmoviesNotNull", position + "newMenu" + newMenu);
                switch (position) {
                    case 0:
                        // Order by popularity when the popularity menu item is clicked
                        NetworkUtils.changeBaseUrl("popular");
                        mMovieAdapter.setMovieData(null);
                        mMovieAdapter.setMovieData(movies);
                        break;
                    case 1:
                        // Order by top rated when the top rated menu item is clicked
                        NetworkUtils.changeBaseUrl("rating");
                        mMovieAdapter.setMovieData(null);
                        mMovieAdapter.setMovieData(movies);
                        break;

                    case 2:
                        //List favourite movies
                        mMovieAdapter.setMovieData(null);
                        setupViewModel();
                        break;
                }
            }
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("mySpinner", spinner.getSelectedItemPosition());
        savedInstanceState.putParcelableArray(MOVIES, movies);

    }
}