package me.sivze.popularmovies.loader;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import me.sivze.popularmovies.model.MovieModel;
import me.sivze.popularmovies.model.MovieResponseModel;
import me.sivze.popularmovies.util.Constants;
import me.sivze.popularmovies.util.KeyKeeper;
import me.sivze.popularmovies.util.PreferenceUtil;
import me.sivze.popularmovies.util.ServiceUtil;
import retrofit.Call;
import retrofit.Response;

/**
 * Task retrieves data from the web server
 */
public class ResultsLoader extends CommonTaskLoader {
    private static final String TAG = "ResultsLoader";

    public ResultsLoader(Context context) {
        super(context);
    }

    @Override
    public List<MovieModel> loadInBackground() {
        Call<MovieResponseModel> createdCall = ServiceUtil.getService().getMovieResults(KeyKeeper.movieDBAPIkey,
                PreferenceUtil.getPrefs(getContext(),
                        Constants.MODE_VIEW,
                        Constants.SORT_BY_POPULARITY_DESC));
        try {
            Response<MovieResponseModel> result = createdCall.execute();
            return result.body().getResults();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "IOException occurred in loadInBackground()");
        }
        return null;
    }
}