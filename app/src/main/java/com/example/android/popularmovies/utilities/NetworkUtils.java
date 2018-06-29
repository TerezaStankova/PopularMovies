package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


import com.example.android.popularmovies.BuildConfig;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String TOP_RATED_MOVIE_URL = "https://api.themoviedb.org/3/movie/top_rated";
    private static final String POPULAR_MOVIE_URL = "https://api.themoviedb.org/3/movie/popular";
    private static final String API_KEY = "api_key";
    private static final String api_key = BuildConfig.API_KEY;

    private static String MOVIE_BASE_URL = POPULAR_MOVIE_URL;

    private static final String BASIC_MOVIE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String VIDEO_MOVIE_URL_END = "/videos";
    private static final String REVIEW_MOVIE_URL_END = "/reviews";

    public static void changeBaseUrl(String choice){
        if (choice.equals("popular")) {MOVIE_BASE_URL = POPULAR_MOVIE_URL;}
        else MOVIE_BASE_URL = TOP_RATED_MOVIE_URL;
    }

    public static URL buildUrl() {
        Uri movieQueryUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY, api_key)
                .build();

        try {
            URL movieQueryURL = new URL(movieQueryUri.toString());
            Log.v(TAG, "URL: " + movieQueryURL);
            return movieQueryURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL buildVideoUrl(int id) {
        Uri videoQueryUri = Uri.parse(BASIC_MOVIE_URL + id + VIDEO_MOVIE_URL_END).buildUpon()
                .appendQueryParameter(API_KEY, api_key)
                .build();

        try {
            URL movieQueryURL = new URL(videoQueryUri.toString());
            Log.v(TAG, "URL: " + movieQueryURL);
            return movieQueryURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL buildReviewUrl(int id) {
        Uri reviewQueryUri = Uri.parse(BASIC_MOVIE_URL + id + REVIEW_MOVIE_URL_END).buildUpon()
                .appendQueryParameter(API_KEY, api_key)
                .build();

        try {
            URL movieQueryURL = new URL(reviewQueryUri.toString());
            Log.v(TAG, "URL: " + movieQueryURL);
            return movieQueryURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            /*You should checkout Volley library
            *(https://developer.android.com/training/volley/) for making network requests.
            * *They really make networking so much easy.*/

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}
