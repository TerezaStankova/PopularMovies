package com.example.android.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie ORDER BY title")
    LiveData<List<MovieEntry>> loadAllMovies();

    @Insert
    void insertMovie(MovieEntry movieEntry);

    @Query("SELECT title FROM movie WHERE id = :id")
    String titleById(int id);

    @Query("DELETE FROM movie WHERE id = :id")
    void deleteById(int id);
}