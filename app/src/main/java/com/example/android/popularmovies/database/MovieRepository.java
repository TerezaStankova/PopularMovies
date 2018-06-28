package com.example.android.popularmovies.database;


import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class MovieRepository {

    private MovieDao mWordDao;
    private LiveData<List<MovieEntry>> mAllWords;
    private String title;

    public MovieRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        mWordDao = db.movieDao();
        mAllWords = mWordDao.loadAllMovies();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<MovieEntry>> getAllMovies() {
        return mAllWords;
    }
}

