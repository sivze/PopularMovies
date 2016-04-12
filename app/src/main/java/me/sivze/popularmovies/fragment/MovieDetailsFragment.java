package me.sivze.popularmovies.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import me.sivze.popularmovies.R;
import me.sivze.popularmovies.model.MovieModel;
import me.sivze.popularmovies.util.ServiceUtil;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {

    Context mContext;
    TextView mMovieTitle;
    TextView mMovieReleaseDate;
    TextView mMovieDescription;
    TextView mMovieRating;
    ImageView mMoviePoster;
    RatingBar mMovieRatingBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        mContext = rootView.getContext();
        mMovieTitle = (TextView) rootView.findViewById(R.id.movie_title_text_view);
        mMovieReleaseDate = (TextView) rootView.findViewById(R.id.movie_release_date_text_view);
        mMovieDescription = (TextView) rootView.findViewById(R.id.movie_description_text_view);
        mMovieRating = (TextView) rootView.findViewById((R.id.movie_rating_text_view));
        mMoviePoster = (ImageView) rootView.findViewById(R.id.movie_poster_image_view);
        mMovieRatingBar = (RatingBar) rootView.findViewById(R.id.movie_rating_bar);

        return rootView;
    }

    public void setMovieDetails(@NonNull MovieModel movie){

        mMovieTitle.setText(movie.getTitle());
        mMovieReleaseDate.setText(movie.getReleaseDate());
        mMovieDescription.setText(movie.getOverview());
        mMovieRating.setText(movie.getVoteAverage().toString().substring(0,3)+"/10");

        Glide.with(mContext)
                .load(ServiceUtil.buildPosterUrl(movie.getBackdropPath()))
                .into(mMoviePoster);

        mMovieRatingBar.setRating(movie.getVoteAverage());
    }

}
