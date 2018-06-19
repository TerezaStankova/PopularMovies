package com.example.android.popularmovies.utilities;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Trailer;

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
        final String OWM_ID = "id";

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
            int id;

            /* Get the JSON object representing the movie */
            JSONObject movieInfo = movieArray.getJSONObject(i);

            title = movieInfo.getString(OWN_TITLE);
            originalTitle = movieInfo.getString(OWM_ORIGINAL_TITLE);
            voteAverage = movieInfo.getDouble(OWM_VOTE_AVERAGE);
            releaseDate = movieInfo.getString(OWM_RELEASE_DATE);
            plot = movieInfo.getString(OWM_PLOT);
            poster_path = movieInfo.getString(OWM_POSTER);
            poster =  "http://image.tmdb.org/t/p/w185" + poster_path;
            id = movieInfo.getInt(OWM_ID);

            parsedMovieData[i] = new Movie(title, originalTitle, releaseDate, voteAverage, poster, plot, id);
        }

        return parsedMovieData;
    }

    //Parse data from Json for trailers
    public static Trailer[] getTrailerDataFromJson(Context context, String trailerJsonStr)
            throws JSONException {

        /* Trailer information. Each trailer's info is an element of the "results" array */
        final String OWM_RESULTS = "results";
        final String OWN_NAME = "name";
        final String OWM_TYPE = "type";
        final String OWM_KEY = "key";

        /* Trailer array to hold each trailer's info */
        Trailer[] parsedTrailerData = null;

        JSONObject trailerJson = new JSONObject(trailerJsonStr);
        JSONArray trailerArray = trailerJson.getJSONArray(OWM_RESULTS);

        parsedTrailerData = new Trailer[trailerArray.length()];
        int a = 0;

        for (int i = 0; i < trailerArray.length(); i++) {
            String name;
            String key;
            String type;
            String whole_key;

            /* Get the JSON object representing the trailer */
            JSONObject trailerInfo = trailerArray.getJSONObject(i);
            type = trailerInfo.getString(OWM_TYPE);

            if (type.equals("Trailer")){
                name = trailerInfo.getString(OWN_NAME);
                key = trailerInfo.getString(OWM_KEY);
                whole_key =  "https://wwww.youtube.com/watch?v=" + key;
                parsedTrailerData[a] = new Trailer(name, whole_key);
                a++;
            }

        }

        return parsedTrailerData;
    }
}