package com.example.filmfan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.filmfan.Movie;
import com.example.filmfan.MovieAdapter;
import com.example.filmfan.R;
import com.example.filmfan.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.filmfan.MovieDetailsActivity.MOVIE_ID_TOKEN;

public class MoviesListFragment extends Fragment {

    // private HomeViewModel homeViewModel;

    ArrayList<Movie> dataModels;
    ListView listView;
    private MovieAdapter adapter;

    final static private String TAG = "HOME_FRAGMENT";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // homeViewModel =
                // ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_movies_list, container, false);


        listView = root.findViewById(R.id.list);
        ProgressBar progressBar = root.findViewById(R.id.loadingList);
        TextView nothingToShow = root.findViewById(R.id.nothingToShow);

        dataModels= new ArrayList<>();

        dataModels.add( new Movie( 0, "a", "b", "c", "d", "e", "f", "g" ) );

        adapter= new MovieAdapter(dataModels, getActivity());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie dataModel= dataModels.get(position);

                // Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getType()+" API: "+dataModel.getVersion_number(), Snackbar.LENGTH_LONG)
                        // .setAction("No action", null).show();

            }
        });

        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.user_configurations), Context.MODE_PRIVATE);
        // SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

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
        // m.retrieveNowPlaying(listView);

        return root;
    }
}