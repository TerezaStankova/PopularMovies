package com.example.android.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.MovieEntry;
import com.example.android.popularmovies.database.MovieRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private MovieRepository mRepository;
    private LiveData<List<MovieEntry>> movies;


    public MainViewModel(Application application) {
        super(application);
        mRepository = new MovieRepository(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        movies = mRepository.getAllWords();
    }

    public LiveData<List<MovieEntry>> getTasks() {
        return movies;
    }
}

