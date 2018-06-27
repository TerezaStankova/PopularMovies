package com.example.android.popularmovies.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Review;


public class ReviewFragment extends Fragment {

    // Final Strings to store state information about the reviews
    public static final String REVIEWS_ARRAY = "reviews";

    // Tag for logging
    private static final String TAG = "ReviewFragment";

    // Variables to store reviews that this fragment displays
    private Review[] mReviews;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public ReviewFragment() {
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Load the saved state (the array of trailers) if there is one
        if (savedInstanceState != null) {
            mReviews = (Review[]) savedInstanceState.getParcelableArray(REVIEWS_ARRAY);
        }


        // Inflate the fragment_reviews fragment layout
        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);

        // Get a reference to the Linear Layout in the fragment layout
        final LinearLayout trailerInfoLayout = (LinearLayout) rootView.findViewById(R.id.reviews_linear_layout);

        if (mReviews != null) {

            for (final Review review : mReviews) {
                if (review != null) {

                    View mMovieReviewItem = LayoutInflater.from(getActivity()).inflate(
                            R.layout.review_list_item, null);

                    TextView mMovieReviewAuthor = (TextView) mMovieReviewItem.findViewById(R.id.review_author);
                    mMovieReviewAuthor.setText(review.getReviewAuthor());

                    TextView mMovieReviewContent = (TextView) mMovieReviewItem.findViewById(R.id.review_content);
                    mMovieReviewContent.setText(review.getReviewContent());

                    Log.v(TAG, "Trailer Name: " + review.getReviewAuthor());


                    if (trailerInfoLayout != null) {
                        trailerInfoLayout.addView(mMovieReviewItem);
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

    public void setReviews(Review[] reviews) {
        mReviews = new Review[reviews.length];
        mReviews = reviews;
    }

    /**
     * Save the current state of this fragment
     */

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArray(REVIEWS_ARRAY, mReviews);
    }
}

