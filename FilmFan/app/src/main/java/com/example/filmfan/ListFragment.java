package com.example.filmfan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static com.example.filmfan.DetailsActivity.MOVIE_ID_TOKEN;

public class ListFragment extends Fragment {

    final static private String TAG = "HOME_FRAGMENT";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);


        ListView listView = root.findViewById(R.id.list);
        ProgressBar progressBar = root.findViewById(R.id.loadingList);
        TextView nothingToShow = root.findViewById(R.id.nothingToShow);

        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.user_configurations), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.tmdb_api_key), "API KEY");
        editor.apply();

        String api_key= sharedPref.getString(getString(R.string.tmdb_api_key), "");

        Log.i(TAG, "onCreateView: " + api_key);
        Log.i(TAG, "onCreateView: " + getArguments().getInt("myArg"));

        Movie m =  Movie.getInstance(getActivity()); // new Movie( getActivity() );
        m.setViews (listView, progressBar, nothingToShow);

        Intent intent = getActivity().getIntent();
        int movieId = intent.getIntExtra(MOVIE_ID_TOKEN, 0);

        switch (getArguments().getInt("myArg"))
        {
            case 0: m.retrieveNowPlaying    (); break;
            case 1: m.retrieveFavorites     (); break;
            case 2: m.retrieveVotedMovies   (); break;
            case 3: m.retrieveSimilarMovies   (movieId); break;
            case 4: m.retrieveRecommendedMovies   (movieId); break;
            case 5: m.retrieveCast   (movieId); break;
        }

        return root;
    }
}