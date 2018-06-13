package com.example.android.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.android.popularmovies.model.Movie;

public class JSONUtils {

    //Parse data from Json in detail
    public static Movie parseMovieJson(String json) {

        try {
            JSONObject jsonObjectMovies = new JSONObject(json);
            JSONObject jsonObjectMovie = jsonObjectMovies.getJSONObject("results");

            String title = jsonObjectMovie.getString("title");
            String originalTitle = jsonObjectMovie.getString("original_title");
            double voteAverage = (float) jsonObjectMovie.get("vote_average");

            String releaseDate = jsonObjectMovie.getString("release_date");
            String plot = jsonObjectMovie.getString("overview");
            String poster_path = jsonObjectMovie.getString("poster_path");
            String poster =  "http://image.tmdb.org/t/p/w185" + poster_path;

            return new Movie(title, originalTitle, releaseDate, voteAverage, poster, plot);
        }

        //In case data not parsed return null
        catch(JSONException ex) {
            return null;
        }
    }

    //Parse data from Json for posters
    public static Movie[] getSimpleWeatherStringsFromJson(Context context, String movieJsonStr)
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

    /**
     * Parse the JSON and convert it into ContentValues that can be inserted into our database.
     *
     * @param context         An application context, such as a service or activity context.
     * @param forecastJsonStr The JSON to parse into ContentValues.
     *
     * @return An array of ContentValues parsed from the JSON.
     */
    public static ContentValues[] getFullWeatherDataFromJson(Context context, String forecastJsonStr) {
        /** This will be implemented in a future lesson **/
        return null;
    }
}