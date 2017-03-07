package com.shilo.omer.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if (intent != null && intent.hasExtra("MOVIE_DESC_OBJECT")){
            MovieDescription movieObject = intent.getParcelableExtra("MOVIE_DESC_OBJECT");

            ImageView poster = (ImageView)rootView.findViewById(R.id.imageview_movie_poster);
            TextView movieTitle = (TextView)rootView.findViewById(R.id.textview_movie_title);
            TextView movieDescription = (TextView)rootView.findViewById(R.id.textview_movie_description);
            TextView movieReleaseDate = (TextView)rootView.findViewById(R.id.textview_movie_release_date);
            TextView movieRating = (TextView)rootView.findViewById(R.id.textview_movie_user_rating);


            Uri builder = Uri.parse(getString(R.string.the_moviedb_image_link)).buildUpon()
                    .appendPath("w185").appendEncodedPath(movieObject.getPosterPath()).build();

            Glide.with(getActivity()).load(builder.toString()).into(poster);

            movieTitle.setText(movieObject.getTitle());
            movieTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);

            movieDescription.setText(movieObject.getDescription());
            movieReleaseDate.setText(movieObject.getReleaseDate());
            movieRating.setText(movieObject.getUserRating()+"/10");
        }


        return rootView;
    }
}
