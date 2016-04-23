package me.sivze.popularmovies.loader;

import android.content.Context;

import java.util.List;

import me.sivze.popularmovies.model.MovieModel;
import me.sivze.popularmovies.provider.FavoriteMovieContentProvider;

/**
 * Task retrieves data from content provider
 */
public class FavoriteResultsLoader extends CommonTaskLoader {
    private static final String TAG = "FavoriteResultsLoader";

    public FavoriteResultsLoader(Context context) {
        super(context);
    }

    @Override
    public List<MovieModel> loadInBackground() {
        return FavoriteMovieContentProvider.getFavorites(getContext());
    }
}