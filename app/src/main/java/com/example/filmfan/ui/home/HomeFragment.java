package com.example.filmfan.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.filmfan.Movie;
import com.example.filmfan.R;

import static com.example.filmfan.MovieDetailsActivity.MOVIE_ID_TOKEN;

public class HomeFragment extends Fragment implements RatingBar.OnRatingBarChangeListener {

    private HomeViewModel homeViewModel;
    private final String TAG = "HomeFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        TextView textView = root.findViewById(R.id.text_home);

        Movie m = Movie.getInstance (getActivity()); // new Movie( getActivity() );
        m.setViews (textView);

        Intent intent = getActivity().getIntent();
        int movieId = intent.getIntExtra(MOVIE_ID_TOKEN, 0);

        m.retrieveDetails (movieId);

        RatingBar ratingBar = root.findViewById(R.id.ratingBar);
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