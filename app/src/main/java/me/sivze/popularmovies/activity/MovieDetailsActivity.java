package me.sivze.popularmovies.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.sivze.popularmovies.R;
import me.sivze.popularmovies.fragment.MovieDetailsFragment;
import me.sivze.popularmovies.model.MovieModel;

public class MovieDetailsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        MovieModel movie = (MovieModel) getIntent().getSerializableExtra(MoviesActivity.EXTRA_MOVIE_MODEL);
        MovieDetailsFragment mMovieFragment =
                (MovieDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movie_details);

        //passing the movie object to the MovieDetailsFragment
        mMovieFragment.setMovieDetails(movie);
    }
}
