package com.example.android.popularmovies.database;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class MovieRepository {

    private MovieDao mWordDao;
    private LiveData<List<MovieEntry>> mAllWords;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public MovieRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        mWordDao = db.movieDao();
        mAllWords = mWordDao.loadAllTasks();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<MovieEntry>> getAllWords() {
        return mAllWords;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert (MovieEntry word) {
        new insertAsyncTask(mWordDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<MovieEntry, Void, Void> {

        private MovieDao mAsyncTaskDao;

        insertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MovieEntry... params) {
            mAsyncTaskDao.insertTask(params[0]);
            return null;
        }
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public String getTitle (Integer id) {
        new getTitleAsyncTask(mWordDao).execute(id);
        return null;
    }

    private static class getTitleAsyncTask extends AsyncTask<Integer, Void, String> {

        private MovieDao mAsyncTaskDao;

        getTitleAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected String doInBackground(final Integer... params) {
            String title = mAsyncTaskDao.titleById(params[0]);
            return title;
        }
    }
}

