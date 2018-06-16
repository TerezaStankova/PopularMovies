package com.example.android.popularmovies.utilities;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.android.popularmovies.model.Movie;

public class JSONUtils {

    //Parse data from Json for posters
    public static Movie[] getMovieDataFromJson(Context context, String movieJsonStr)
            throws JSONException {

        /* Movie information. Each movie's info is an element of the "results" array */
        final String OWM_RESULTS = "results";
        final String OWN_TITLE = "title";
        final String OWM_ORIGINAL_TITLE = "original_title";
        final String OWM_VOTE_AVERAGE = "vote_average";
        final String OWM_RELEASE_DATE = "release_date";
        final String OWM_PLOT = "overview";
        final String OWM_POSTER = "poster_path";

        /* Movie array to hold each movie's info */
        Movie[] parsedMovieData = null;

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);

        parsedMovieData = new Movie[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {
            String title;
            String originalTitle;
            double voteAverage;

            String releaseDate;
            String plot;
            String poster_path;
            String poster;

            /* Get the JSON object representing the movie */
            JSONObject movieInfo = movieArray.getJSONObject(i);

           title = movieInfo.getString(OWN_TITLE);
           originalTitle = movieInfo.getString(OWM_ORIGINAL_TITLE);
           voteAverage = movieInfo.getDouble(OWM_VOTE_AVERAGE);

            releaseDate = movieInfo.getString(OWM_RELEASE_DATE);
            plot = movieInfo.getString(OWM_PLOT);
            poster_path = movieInfo.getString(OWM_POSTER);
            poster =  "http://image.tmdb.org/t/p/w185" + poster_path;

            parsedMovieData[i] = new Movie(title, originalTitle, releaseDate, voteAverage, poster, plot);
        }

        return parsedMovieData;
    }
}