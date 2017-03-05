package com.shilo.omer.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FragmentMain extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final String LOG_TAG = com.shilo.omer.popularmovies.FragmentMain.class.getSimpleName();
    SwipeRefreshLayout mSwipeRefreshLayout;

    public FragmentMain() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_main);
        mSwipeRefreshLayout.setOnRefreshListener(this);

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
        // // TODO: 02/03/2017 implement asyncTask to create and receive movies information from MovieDB
    }

    public class FetchNowPlayingMoviesTask extends AsyncTask<String,Void,String[]>{

        private final String LOG_TAG = com.shilo.omer.popularmovies.FragmentMain.FetchNowPlayingMoviesTask.class.getSimpleName();
        private String mMovieId;

        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieListJsonStr = null;

            if (params.length == 0){
                return new String[]{""};
            }

            mMovieId = params[0];

            try{
                Uri buildUrl = Uri.parse(getString(R.string.the_moviedb_api_link)).buildUpon()
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
            } catch (IOException e) {
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
                String[] result =

                return result;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new String[]{""};
        }
    }

}
