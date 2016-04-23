package me.sivze.popularmovies.activity;

import android.app.ActivityOptions;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;

import com.facebook.stetho.Stetho;

import butterknife.ButterKnife;
import me.sivze.popularmovies.R;
import me.sivze.popularmovies.fragment.MovieDetailsFragment;
import me.sivze.popularmovies.fragment.MoviesFragment;
import me.sivze.popularmovies.model.MovieModel;
import me.sivze.popularmovies.util.Constants;

public class MoviesActivity extends BaseActivity implements MoviesFragment.Callback {

    private static final String TAG = "MoviesActivity";
    private boolean mTwoPane = true;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);
        setToolbar(false, true);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                replaceMovieDetailFragment(MovieDetailsFragment.newInstance(new Bundle()));
                initStetho();
            } else {
                selectedPosition = savedInstanceState.getInt(Constants.POSITION_KEY);
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
    }

    private void replaceMovieDetailFragment(MovieDetailsFragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.movie_detail_container, fragment, MovieDetailsFragment.TAG).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.POSITION_KEY, selectedPosition);
    }

    /**
     * This overrided method used as listener when user select a movie in the grid.
     * @param movieData selected movie
     * @param posterBitmap shared movie image
     * @param view shared view for transition
     * @param position selected position in the grid
     */
    @Override
    public void onItemSelected(MovieModel movieData, final Bitmap posterBitmap, View view, int position) {
        Log.d(TAG, "onItemSelected() called");
        selectedPosition = position;
        if (mTwoPane) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle args = new Bundle();
            args.putParcelable(Constants.MOVIE_DETAIL_KEY, movieData);
            args.putParcelable(Constants.POSTER_IMAGE_KEY, posterBitmap);
            MovieDetailsFragment fragment = MovieDetailsFragment.newInstance(args);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fragment.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.trans_move));
                fragment.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.no_transition));
                fragmentTransaction.addSharedElement(view, Constants.POSTER_IMAGE_VIEW_KEY);
            }
            fragmentTransaction.replace(R.id.movie_detail_container, fragment, MovieDetailsFragment.TAG).commit();
        } else {
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                options = ActivityOptions.
                        makeSceneTransitionAnimation(this, view, Constants.POSTER_IMAGE_VIEW_KEY);
            }
            Intent openDetailIntent = new Intent(this, MovieDetailsActivity.class);
            openDetailIntent.putExtra(Constants.MOVIE_DETAIL_KEY, movieData);
            openDetailIntent.putExtra(Constants.POSTER_IMAGE_KEY, posterBitmap);
            if (options != null) {
                startActivity(openDetailIntent, options.toBundle());
            } else {
                startActivity(openDetailIntent);
            }
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    private void initStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
