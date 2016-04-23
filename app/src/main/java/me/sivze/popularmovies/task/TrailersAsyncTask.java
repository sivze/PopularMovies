package me.sivze.popularmovies.task;

import android.util.Log;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;

import me.sivze.popularmovies.model.TrailerModel;
import me.sivze.popularmovies.model.TrailersResponseModel;
import me.sivze.popularmovies.util.KeyKeeper;
import me.sivze.popularmovies.util.ServiceUtil;
import retrofit.Call;
import retrofit.Response;

public class TrailersAsyncTask extends CommonAsyncTask<TrailerModel> {

    private static final String TAG = "TrailersAsyncTask";

    private long mMovieId;

    public TrailersAsyncTask(long mMovieId, ProgressBar mProgressBar, FetchDataListener mListener) {
        super(mProgressBar, mListener);
        this.mMovieId = mMovieId;
    }

    @Override
    protected ArrayList<TrailerModel> doInBackground(Void... params) {

        Call<TrailersResponseModel> createdCall = ServiceUtil.getService().getTrailersResults(mMovieId,
                KeyKeeper.movieDBAPIkey);
        try {
            Response<TrailersResponseModel> result = createdCall.execute();
            return result.body().results;
        } catch (IOException e) {
            Log.e(TAG, "IOException occurred in doInBackground()");
        }
        return null;
    }

}
