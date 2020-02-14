package com.example.filmfan;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Movie implements Comparable {

    private static Movie thisMovie;

    private final String BASE_URL = "https://api.themoviedb.org/3/";
    private final String API_KEY = "bd2803c693505ef0ab0bdf221ce6e525";
    private String SESSION_ID = "028964ad2ed5cd74f506b80e3368d46761c60515";
    private String RESOURCE_LAN = "&language=fr-FR";

    private int id;
    private String title;
    private String title2;
    private String releaseDate;
    private String votesAverage;
    private String picture;

    private String overview;
    private String genre;
    private String rating;

    private ListView listView;
    private ProgressBar progressBar;
    private TextView nothingToShow;

    private TextView details_title;
    private TextView details_originalTitle;
    private RatingBar details_rating;
    private TextView details_rating_text;
    private TextView details_genres;
    private TextView details_release_year;
    private TextView details_details;
    private ImageView details_cover;

    Context context;

    final String TAG = "MOVIE_MODEL";

    private String makeUrl (String uri, String additionalParameters)
    {
        return BASE_URL + uri + "?api_key=" + API_KEY + "&session_id=" + SESSION_ID + RESOURCE_LAN + additionalParameters;
    }

    public static Movie getInstance (Context context)
    {
        if (thisMovie == null)
        {
            thisMovie = new Movie(context);
        }

        return thisMovie;
    }

    public Movie (Context context)
    {
        this.context = context;
    }


    public Movie(int id, String title, String title2, String releaseDate, String votesAverage, String picture, String overview, String genre, String rating) {
        this.id = id;
        this.title=title;
        this.title2=title2;
        this.releaseDate = releaseDate;
        this.votesAverage = votesAverage;
        this.picture = picture;
        this.overview = overview;
        this.genre = genre;
        this.rating = rating;
    }

    @Override
    public int compareTo(Object o) {
        return title.compareToIgnoreCase( ((Movie) o).getTitle() );
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle2() {
        return title2;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVotesAverage() {
        return votesAverage;
    }

    public String getPicture() {
        return picture;
    }

    public String getOverview() {
        return overview;
    }

    public String getGenre() {
        return genre;
    }

    public String getRating() {
        return rating;
    }

    public void setViews (ListView listView, ProgressBar progressBar, TextView nothingToShow)
    {
        this.listView = listView;
        this.progressBar = progressBar;
        this.nothingToShow = nothingToShow;
    }

    public void setViews (TextView details_title, TextView details_originalTitle, RatingBar details_rating, TextView details_rating_text, TextView details_genres, TextView details_release_year, TextView details_details, ImageView details_cover)
    {
        this.details_title          = details_title;
        this.details_originalTitle  = details_originalTitle;
        this.details_rating         = details_rating;
        this.details_rating_text    = details_rating_text;
        this.details_genres         = details_genres;
        this.details_release_year   = details_release_year;
        this.details_details        = details_details;
        this.details_cover          = details_cover;
    }

    private JsonObjectRequest retrieveMoviesList (String url)
    {
        listView.setVisibility      ( View.GONE );
        nothingToShow.setVisibility (View.GONE);
        progressBar.setVisibility   ( View.VISIBLE );

        return new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                ArrayList<Movie> dataModels= new ArrayList<>();

                try
                {
                    JSONArray results = response.getJSONArray("results");
                    int size = results.length();

                    for (int i = 0; i < size; i++)
                    {
                        JSONObject movie = response.getJSONArray("results").getJSONObject(i);
                        String title2 = movie.getString("original_title");
                        String title = movie.getString("title");

                        if (title.equals(title2)) title2 = "";

                        dataModels.add(
                                new Movie(
                                        movie.getInt("id"),
                                        title,
                                        title2,
                                        "Release date: " + movie.getString("release_date"),
                                        movie.getString("vote_average"),
                                        "https://image.tmdb.org/t/p/w500" + movie.getString("poster_path"),
                                        "", "", "" ) );
                        Log.i(TAG, "onResponse: " + response.getJSONArray("results").get(i).toString());
                    }

                    Collections.sort(dataModels);

                    MovieAdapter adapter = new MovieAdapter(dataModels, context);
                    listView.setAdapter(adapter);

                    listView.setVisibility      ( View.VISIBLE );
                    progressBar.setVisibility   ( View.GONE );
                    nothingToShow.setVisibility (View.GONE);

                } catch (Exception e) {
                    progressBar.setVisibility   ( View.GONE );
                    nothingToShow.setVisibility ( View.VISIBLE);
                    listView.setVisibility      ( View.GONE );
                }

                if (dataModels.size() == 0)
                {
                    progressBar.setVisibility   ( View.GONE );
                    nothingToShow.setVisibility ( View.VISIBLE);
                    listView.setVisibility      ( View.GONE );
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onResponse: " + error.toString());
                // TODO: Handle error

            }
        });
    }


    private JsonObjectRequest retrieveCast (String url)
    {
        listView.setVisibility      ( View.GONE );
        nothingToShow.setVisibility (View.GONE);
        progressBar.setVisibility   ( View.VISIBLE );

        return new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                ArrayList<Actor> dataModels= new ArrayList<>();

                try
                {
                    JSONArray results = response.getJSONArray("cast");
                    int size = results.length();

                    for (int i = 0; i < size; i++)
                    {
                        JSONObject movie = response.getJSONArray("cast").getJSONObject(i);

                        dataModels.add(
                                new Actor(
                                        movie.getString("character"),
                                        movie.getString("name"),
                                        movie.getString("profile_path") ) );
                    }

                    ActorAdapter adapter = new ActorAdapter(dataModels, context);
                    listView.setAdapter(adapter);

                    listView.setVisibility      ( View.VISIBLE );
                    progressBar.setVisibility   ( View.GONE );
                    nothingToShow.setVisibility (View.GONE);

                } catch (Exception e) {
                    progressBar.setVisibility   ( View.GONE );
                    nothingToShow.setVisibility ( View.VISIBLE);
                    listView.setVisibility      ( View.GONE );
                }

                if (dataModels.size() == 0)
                {
                    progressBar.setVisibility   ( View.GONE );
                    nothingToShow.setVisibility ( View.VISIBLE);
                    listView.setVisibility      ( View.GONE );
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onResponse: " + error.toString());
                // TODO: Handle error

            }
        });
    }

    private JsonObjectRequest addToFavorites (final String url, JSONObject obj, final View img)
    {
        return new JsonObjectRequest (Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try
                {

                    Snackbar.make(img, "Movie added to favorites successfully.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } catch (Exception e) {
                    Log.i(TAG, "onResponse: " + e.getMessage());
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onResponse: " + error.toString());
                // TODO: Handle error

            }
        });
    }

    private JsonObjectRequest voteForMovie (final String url, JSONObject obj, final View view)
    {
        Log.i(TAG, "voteForMovie: " + url + obj);
        return new JsonObjectRequest (Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    Snackbar.make(view, "Movie voted successfully.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.i(TAG, "voteForMovie: " + response);

                } catch (Exception e) {
                    Log.i(TAG, "onResponse: " + e.getMessage());
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onResponse: " + error.toString());
                // TODO: Handle error

            }
        });
    }

    private JsonObjectRequest retrieveMoviesDetails (final String url)
    {
        Log.i(TAG, "retrieveMoviesDetails: " + url);
        return new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    if (response.getString("original_title").equals(response.getString("title")))
                        details_originalTitle.setVisibility(View.GONE);

                    String genres = "";

                    JSONArray resultsArray = response.getJSONArray("genres");
                    int size = resultsArray.length();

                    for (int i = 0; i < size; i++)
                    {
                        if (i != 0) genres += ", ";
                        genres += response.getJSONArray("genres").getJSONObject(i).getString("name");
                    }
                    Log.i(TAG, "onResponse: " + genres);

                    String details = response.getString("tagline");

                    if (!details.equals("")) details.concat("\n");
                    details = details.concat( response.getString("overview") );

                    details_title.setText(response.getString("title"));
                    details_originalTitle.setText(response.getString("original_title"));
                    details_rating.setRating( Float.valueOf( response.getString("vote_average") ) );
                    details_rating_text.setText(response.getString("vote_average"));
                    details_genres.setText(genres);
                    details_release_year.setText(releaseDate);
                    details_details.setText( details );

                    String url = "https://image.tmdb.org/t/p/original";
                    url = url.concat(response.getString("poster_path"));

                    Picasso.get().load(url).into(details_cover);


                } catch (Exception e) {
                    Log.i(TAG, "onResponse: " + e.getMessage());
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onResponse: " + error.toString());
                // TODO: Handle error

            }
        });
    }

    public void retrieveNowPlaying ()
    {
        JsonObjectRequest jsonObjectRequest = retrieveMoviesList (makeUrl ("movie/now_playing","&page=1"));
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void retrieveCast (int id)
    {
        JsonObjectRequest jsonObjectRequest = retrieveCast (makeUrl("movie/" + id + "/credits" ,"") );
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void retrieveDetails (int id)
    {
        JsonObjectRequest jsonObjectRequest = retrieveMoviesDetails (makeUrl("movie/" + id ,"") );
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void voteForMovie (View view, int id, float vote)
    {
        JSONObject obj = new JSONObject();

        try
        {
            obj.put("value", vote);
        }
        catch (Exception e)
        {
            Snackbar.make(view, "Unable to vote for this.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = voteForMovie (makeUrl ("movie/" + id + "/rating", ""), obj, view);
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void addToFavorites (View img, int id)
    {
        JSONObject obj = new JSONObject();

        try
        {
            obj.put("media_type", "movie");
            obj.put("media_id", id);
            obj.put("favorite", true);
        }
        catch (Exception e)
        {
            Snackbar.make(img, "Unable to make this favorite", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = addToFavorites (makeUrl ("account/{account_id}/favorite", ""), obj, img);
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void retrieveSimilarMovies (int movieId)
    {
        JsonObjectRequest jsonObjectRequest = retrieveMoviesList (makeUrl ("movie/" + movieId + "/similar",""));
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void retrieveRecommendedMovies (int movieId)
    {
        JsonObjectRequest jsonObjectRequest = retrieveMoviesList (makeUrl ("movie/" + movieId + "/recommendations",""));
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void retrieveFavorites ()
    {
        JsonObjectRequest jsonObjectRequest = retrieveMoviesList (makeUrl ("account/0/favorite/movies","&sort_by=created_at.desc"));
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void retrieveVotedMovies ()
    {
        JsonObjectRequest jsonObjectRequest = retrieveMoviesList (makeUrl ("account/1/rated/movies","&sort_by=created_at.desc"));
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void requestToken ()
    {

    }

    private void requestSession ()
    {

    }
}