package com.shilo.omer.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class FragmentMain extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final String LOG_TAG = com.shilo.omer.popularmovies.FragmentMain.class.getSimpleName();
    SwipeRefreshLayout mSwipeRefreshLayout;
    MoviesAdapter mMoviesAdapter;
    GridView mDataGridView;
    List<MovieDescription> mMovieDescriptionList;

    public FragmentMain() {
        // Required empty public constructor
        mMovieDescriptionList = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();
        FetchMovieList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMoviesAdapter = new MoviesAdapter(
                getActivity(),
                new ArrayList<MovieDescription>());
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_main);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mDataGridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        mDataGridView.setAdapter(mMoviesAdapter);

        mDataGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieDescription selectedMovie = mMoviesAdapter.getItem(position);
                Intent openDetailMovieActivity = new Intent(getActivity(),MovieDetailActivity.class);
                openDetailMovieActivity.putExtra("MOVIE_DESC_OBJECT",selectedMovie);
                startActivity(openDetailMovieActivity);
            }
        });

                
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRefresh() {
        FetchMovieList();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public class MoviesAdapter extends ArrayAdapter<MovieDescription>{
        private class ViewHolder{
            ImageView poster;
        }

        public MoviesAdapter(Context context, ArrayList<MovieDescription> movies){
            super(context, 0, movies);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            MovieDescription movie = getItem(position);
            ViewHolder viewHolder;

            if (convertView == null){
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.movie_list_layout,parent,false);
                viewHolder.poster = (ImageView) convertView.findViewById(R.id.imageview_movie_poster);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Uri builder = Uri.parse(getString(R.string.the_moviedb_image_link)).buildUpon()
                    .appendPath("w185").appendEncodedPath(movie.getPosterPath()).build();

            //Log.d(LOG_TAG,builder.toString());
            Glide.with(getActivity()).load(builder.toString()).centerCrop().crossFade().into(viewHolder.poster);

            return convertView;
        }
    }

    public class FetchNowPlayingMoviesTask extends AsyncTask<String,Void,MovieDescription[]>{

        private final String LOG_TAG = com.shilo.omer.popularmovies.FragmentMain.FetchNowPlayingMoviesTask.class.getSimpleName();
        private String mMovieId;

        @Override
        protected MovieDescription[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieListJsonStr = null;

            if (params.length == 0){
                return new MovieDescription[]{};
            }

            mMovieId = params[0];

            try{
                Uri buildUrl = Uri.parse(getString(R.string.the_moviedb_api_link)).buildUpon()
                        .appendPath(getSortMoviesOption())
                        .appendQueryParameter("api_key",BuildConfig.THE_MOVIEDB_API_KEY).build();


                URL finishedUrl = new URL(buildUrl.toString());
                urlConnection = (HttpURLConnection) finishedUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                movieListJsonStr = buffer.toString();
            } catch (Exception e) {
                Log.e(LOG_TAG,"Error",e);
                return null;
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                //// TODO: 05/03/2017 Parse the JSON result to an object
                MovieDescription[] result = getMovieListFromJson(movieListJsonStr);

                return result;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new MovieDescription[]{};
        }

        @Override
        protected void onPostExecute(MovieDescription[] s) {
            mSwipeRefreshLayout.setRefreshing(false);
            mMoviesAdapter.clear();
            UpdateMoviesDescriptionList(s);
            mMoviesAdapter.addAll(mMovieDescriptionList);
        }
    }

    private String getSortMoviesOption(){
        SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(getActivity());
        String value = prefs.getString(getString(R.string.pref_sort_key),getString(R.string.pref_sort_default_value));
        String result = getString(R.string.the_moviedb_api_path_popular);

        if (!value.equals(prefs.getString(getString(R.string.pref_sort_default_value),getString(R.string.pref_sort_default_value)))){
            result = getString(R.string.the_moviedb_api_path_top_rated);
        }

        return result;
    }

    private void UpdateMoviesDescriptionList(MovieDescription[] inputArray){
        mMovieDescriptionList.clear();

        for (MovieDescription desc: inputArray) {
            mMovieDescriptionList.add(desc);
        }
    }

    private void FetchMovieList(){
        if(isOnline()){
            new FetchNowPlayingMoviesTask().execute("");
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private MovieDescription[] getMovieListFromJson(String movieListJsonStr) throws JSONException{
        JSONObject moviesJson = new JSONObject(movieListJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray("results");
        int amountOfMovies = moviesArray.length();

        MovieDescription[] result = new MovieDescription[amountOfMovies];

        for (int i = 0; i < amountOfMovies; i++) {
            JSONObject currentMovie = moviesArray.getJSONObject(i);

            result[i] = new MovieDescription(currentMovie.getString("poster_path"));
            result[i].setDescription(currentMovie.getString("overview"));
            result[i].setTitle(currentMovie.getString("title"));
            result[i].setReleaseDate(currentMovie.getString("release_date"));
            result[i].setUserRating(currentMovie.getString("vote_average"));
        }

        return result;
    }
}
