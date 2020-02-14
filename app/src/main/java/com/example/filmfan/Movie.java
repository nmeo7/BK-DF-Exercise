package com.example.filmfan;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movie {

    private static Movie thisMovie;

    private final String BASE_URL = "https://api.themoviedb.org/3/";
    private final String API_KEY = "bd2803c693505ef0ab0bdf221ce6e525";
    private String SESSION_ID = "028964ad2ed5cd74f506b80e3368d46761c60515";
    private String RESOURCE_LAN = "&language=fr-FR";

    private int id;
    private String title;
    private String releaseDate;
    private String votesAverage;
    private String picture;

    private String overview;
    private String genre;
    private String rating;

    private ListView listView;
    private ProgressBar progressBar;
    private TextView nothingToShow;

    private TextView textView;

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


    public Movie(int id, String title, String releaseDate, String votesAverage, String picture, String overview, String genre, String rating) {
        this.id = id;
        this.title=title;
        this.releaseDate = releaseDate;
        this.votesAverage = votesAverage;
        this.picture = picture;
        this.overview = overview;
        this.genre = genre;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
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

    public void setViews (TextView textView)
    {
        this.textView = textView;
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
                        String title = movie.getString("original_title");
                        if (!title.equals(movie.getString("title")))
                            title = title + " (" + movie.getString("title") + ")";

                        dataModels.add(
                                new Movie(
                                        movie.getInt("id"),
                                        title,
                                        "Release date: " + movie.getString("release_date"),
                                        movie.getString("vote_average"),
                                        "https://image.tmdb.org/t/p/w500" + movie.getString("poster_path"),
                                        "", "", "" ) );
                        Log.i(TAG, "onResponse: " + response.getJSONArray("results").get(i).toString());
                    }

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
                    String title = response.getString("original_title");
                    if (!title.equals(response.getString("title")))
                        title = title + " (" + response.getString("title") + ")";

                    String releaseDate = response.getString("release_date").split("-")[0];
                    String genres = "";

                    Log.i(TAG, "onResponse: " + title);

                    JSONArray resultsArray = response.getJSONArray("genres");
                    int size = resultsArray.length();

                    for (int i = 0; i < size; i++)
                    {
                        if (i != 0) genres += ", ";
                        genres += response.getJSONArray("genres").getJSONObject(i).getString("name");
                    }
                    Log.i(TAG, "onResponse: " + genres);

                    textView.setText(
                            title + "\n"  +
                                    response.getString("tagline") + "\n" +
                                    response.getString("vote_average") + "\n" +
                                    genres + "\n" +
                                    releaseDate +
                                    "\n\n" + response.getString("overview") );
                    Log.i(TAG, "onResponse: " + textView.getText());

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