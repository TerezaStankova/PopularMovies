package com.example.android.popularmovies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Trailer;


public class TrailerFragment extends Fragment {

    // Final Strings to store state information about the trailers
    public static final String TRAILERS = "trailers";

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
            mTrailers = (Trailer[]) savedInstanceState.getParcelableArray(TRAILERS);
        }


        // Inflate the trailer_list_item fragment layout
        View rootView = inflater.inflate(R.layout.fragment_trailers, container, false);

        // Get a reference to the ImageView in the fragment layout
        final LinearLayout trailerInfoLayout = (LinearLayout) rootView.findViewById(R.id.trailers_linear_layout);

        if (mTrailers != null) {

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
                Log.v(TAG, "This fragment has a null list of image id's");
        }

        // Return the rootView
        return rootView;

        }

        public void setTrailers(Trailer[] trailers) {
            mTrailers = new Trailer[trailers.length];
            mTrailers = trailers;
        }

        private void openWebPage(String url) {
            Uri webpage = Uri.parse(url);

            // This action allows the user to view the webpage URL.
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

            // Verify that this Intent can be launched and then call startActivity
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }

        /*Save the current state of this fragment*/
        @Override
        public void onSaveInstanceState(Bundle currentState) {
            currentState.putParcelableArray(TRAILERS, mTrailers);
        }
}

