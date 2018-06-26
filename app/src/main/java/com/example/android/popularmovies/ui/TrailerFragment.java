package com.example.android.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.popularmovies.DetailActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.utilities.JSONUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TrailerFragment extends Fragment {

    // Final Strings to store state information about the list of images and list index
    public static final String TRAILER_LIST = "trailer_ids";

    // Tag for logging
    private static final String TAG = "TrailerFragment";

    // Variables to store a list of image resources and the index of the image that this fragment displays
    private Trailer[] mTrailers;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public TrailerFragment() {
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Load the saved state (the array of trailers) if there is one
        if (savedInstanceState != null) {
            mTrailers = (Trailer[]) savedInstanceState.getParcelableArray(TRAILER_LIST);
        }


        // Inflate the trailer_list_item fragment layout
        View rootView = inflater.inflate(R.layout.fragment_trailers, container, false);

        // Get a reference to the ImageView in the fragment layout
        final LinearLayout trailerInfoLayout = (LinearLayout) rootView.findViewById(R.id.trailers_linear_layout);
        TextView mMovieTrailerLabel = (TextView) rootView.findViewById(R.id.trailers_label);

        if (mTrailers != null) {
            mMovieTrailerLabel.setVisibility(View.VISIBLE);

            for (final Trailer trailer : mTrailers) {
                if (trailer != null) {

                    View mMovieTrailerItem = LayoutInflater.from(getActivity()).inflate(
                            R.layout.trailer_list_item, null);

                    TextView mMovieTrailerTitle = (TextView) mMovieTrailerItem.findViewById(R.id.trailer_name);
                    mMovieTrailerTitle.setText(trailer.getTrailerName());
                    Log.v(TAG, "Trailer Name: " + trailer.getTrailerName());

                    mMovieTrailerItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openWebPage(trailer.getTrailerKey());
                        }
                    });

                    if (trailerInfoLayout != null) {
                        trailerInfoLayout.addView(mMovieTrailerItem);
                    }
                }
                }
            }

        else {
            mMovieTrailerLabel.setVisibility(View.INVISIBLE);
            Log.v(TAG, "This fragment has a null list of image id's");
        }

        // Return the rootView
        return rootView;

        }









        /*
        // Inflate the trailer_list_item fragment layout
        View rootView = inflater.inflate(R.layout.trailer_list_item, container, false);

        // Get a reference to the ImageView in the fragment layout
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.body_part_image_view);

        // If a list of image ids exists, set the image resource to the correct item in that list
        // Otherwise, create a Log statement that indicates that the list was not found
        if (mImageIds != null) {
            // Set the image resource to the list item at the stored index
            imageView.setImageResource(mImageIds.get(mListIndex));

            // Set a click listener on the image view
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Increment position as long as the index remains <= the size of the image ids list
                    if (mListIndex < mImageIds.size() - 1) {
                        mListIndex++;
                    } else {
                        // The end of list has been reached, so return to beginning index
                        mListIndex = 0;
                    }
                    // Set the image resource to the new list item
                    imageView.setImageResource(mImageIds.get(mListIndex));
                }
            });

        } else {
            Log.v(TAG, "This fragment has a null list of image id's");
        }

        // Return the rootView
        return rootView;

    } */

    // Setter methods for keeping track of the list images this fragment can display and which image
    // in the list is currently being displayed



    public void setTrailers(Trailer[] trailers) {
        mTrailers = new Trailer[trailers.length];
        mTrailers = trailers;
    }

    /**
         * Save the current state of this fragment
         */

        @Override
        public void onSaveInstanceState(Bundle currentState) {
            currentState.putParcelableArray(TRAILER_LIST, mTrailers);
        }

        private void openWebPage(String url) {
            Uri webpage = Uri.parse(url);

            // This action allows the user to view our webpage URL.
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

            // Verify that this Intent can be launched and then call startActivity
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
}

