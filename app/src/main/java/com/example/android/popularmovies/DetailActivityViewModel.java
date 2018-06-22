package com.example.android.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmovies.database.AppDatabase;
import com.example.android.popularmovies.database.MovieEntry;
import com.example.android.popularmovies.database.MovieRepository;


public class DetailActivityViewModel extends ViewModel {

    // COMPLETED (6) Add a task member variable for the TaskEntry object wrapped in a LiveData
    private LiveData<MovieEntry> movie;
    private MovieRepository mRepository;

    // COMPLETED (8) Create a constructor where you call loadTaskById of the taskDao to initialize the tasks variable
    // Note: The constructor should receive the database and the taskId
    public DetailActivityViewModel(AppDatabase database, int taskId) {
        movie = database.movieDao().loadTaskById(taskId);
    }

    // COMPLETED (7) Create a getter for the task variable
    public LiveData<MovieEntry> getTask() {
        return movie;
    }

    public String getFavouriteTitle(Integer id) {
        String title = mRepository.getTitle(id);
    return title;}
}
