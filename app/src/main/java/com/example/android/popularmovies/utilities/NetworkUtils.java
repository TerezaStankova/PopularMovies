package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String POPULAR_MOVIE_URL = "https://api.themoviedb.org/3/movie/top_rated";
    private static final String API_KEY = "api_key";
    private static final String api_key = "";

    private static final String MOVIE_BASE_URL = POPULAR_MOVIE_URL;


    public static URL buildUrl() {
        Uri weatherQueryUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY, api_key)
                .build();

        try {
            URL movieQueryURL = new URL(weatherQueryUri.toString());
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
