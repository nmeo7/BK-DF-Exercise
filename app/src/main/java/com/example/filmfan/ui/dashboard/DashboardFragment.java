package com.example.filmfan.ui.dashboard;

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

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private DashboardViewModel dashboardViewModel;
    private final String TAG = "DashboardFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        RatingBar ratingBar = root.findViewById(R.id.ratingBar);
        ratingBar.setOnClickListener(this);

        Log.i(TAG, "onCreateView: ");
        
        return root;
    }

    @Override
    public void onClick(View v) {
        RatingBar ratingBar = (RatingBar) v;

        Log.i(TAG, "onClick: " + ratingBar.getRating() );

        Movie m = new Movie( getActivity() );

        Intent intent = getActivity().getIntent();
        int movieId = intent.getIntExtra(MOVIE_ID_TOKEN, 0);

        m.voteForMovie( v, movieId, ratingBar.getRating() );
    }
}