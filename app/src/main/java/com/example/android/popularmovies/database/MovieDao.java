package com.example.android.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie ORDER BY title")
    LiveData<List<MovieEntry>> loadAllTasks();

    @Insert
    void insertTask(MovieEntry movieEntry);

    /*@Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(MovieEntry movieEntry);*/

    @Delete
    void deleteTask(MovieEntry movieEntry);

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<MovieEntry> loadTaskById(int id);

    @Query("SELECT title FROM movie WHERE id = :id")
    String titleById(int id);


    @Query("DELETE FROM movie WHERE id = :id")
    void deleteById(int id);

    @Query("DELETE FROM movie")
    void deleteAll();
}