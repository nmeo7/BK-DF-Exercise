package com.example.filmfan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static com.example.filmfan.DetailsActivity.MOVIE_ID_TOKEN;

public class DetailsFragment extends Fragment implements RatingBar.OnRatingBarChangeListener {

    private final String TAG = "HomeFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_details, container, false);

        Movie m = Movie.getInstance (getActivity()); // new Movie( getActivity() );
        m.setViews (
                (TextView) root.findViewById(R.id.details_title),
                (TextView) root.findViewById(R.id.details_title_original),
                (RatingBar) root.findViewById(R.id.details_ratingBar),
                (TextView) root.findViewById(R.id.details_rating),
                (TextView) root.findViewById(R.id.details_genres),
                (TextView) root.findViewById(R.id.details_release_year),
                (TextView) root.findViewById(R.id.details_details),
                (ImageView) root.findViewById(R.id.details_cover)
        );

        Intent intent = getActivity().getIntent();
        int movieId = intent.getIntExtra(MOVIE_ID_TOKEN, 0);

        m.retrieveDetails (movieId);

        RatingBar ratingBar = root.findViewById(R.id.details_ratingBar);
        ratingBar.setOnRatingBarChangeListener(this);
        ratingBar.setIsIndicator(false);

        return root;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        Log.i(TAG, "onRatingChanged: " + rating);

        Movie m = Movie.getInstance (getActivity()); //  new Movie( getActivity() );

        Intent intent = getActivity().getIntent();
        int movieId = intent.getIntExtra(MOVIE_ID_TOKEN, 0);

        m.voteForMovie( ratingBar, movieId, ratingBar.getRating() );
        ratingBar.setIsIndicator(true);
    }
}