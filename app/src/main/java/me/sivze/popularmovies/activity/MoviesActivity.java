package me.sivze.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import me.sivze.popularmovies.R;
import me.sivze.popularmovies.fragment.MoviesFragment;
import me.sivze.popularmovies.model.MovieModel;
import me.sivze.popularmovies.util.ServiceUtil;

public class MoviesActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_MODEL = "model.MovieModel";
    MoviesFragment mMovieFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        mMovieFragment = (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movies);

        if (id == R.id.action_sort_by_popularity) {
            onSortChanged(ServiceUtil.SORT_TYPE_POPULAR);
        } else if (id == R.id.action_sort_by_rating) {
            onSortChanged(ServiceUtil.SORT_TYPE_TOP_RATED);
        }
        return super.onOptionsItemSelected(item);
    }

    /*
        onMovieClicked is called from MovieFragment.java as a result of onItemClick event
     */
    public void onMovieClicked(MovieModel movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE_MODEL, movie);
        startActivity(intent);
    }

    private void onSortChanged(@NonNull String sortType){
        mMovieFragment.updateMovies(sortType);
        mMovieFragment.scrollToTop();
    }
}
