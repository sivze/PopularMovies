package me.sivze.popularmovies.task;

import android.util.Log;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;

import me.sivze.popularmovies.model.ReviewModel;
import me.sivze.popularmovies.model.ReviewsResponseModel;
import me.sivze.popularmovies.util.KeyKeeper;
import me.sivze.popularmovies.util.ServiceUtil;
import retrofit.Call;
import retrofit.Response;

public class ReviewsAsyncTask extends CommonAsyncTask<ReviewModel> {

    private static final String TAG = "ReviewsAsyncTask";
    private final long mMovieId;

    public ReviewsAsyncTask(long movieId, ProgressBar mProgressBar, FetchDataListener mListener) {
        super(mProgressBar, mListener);
        this.mMovieId = movieId;
    }

    @Override
    protected ArrayList<ReviewModel> doInBackground(Void... params) {
        Call<ReviewsResponseModel> createdCall = ServiceUtil.getService().getReviewsResults(mMovieId,
                KeyKeeper.movieDBAPIkey);
        try {
            Response<ReviewsResponseModel> result = createdCall.execute();
            return result.body().results;
        } catch (IOException e) {
            Log.e(TAG, "IOException occurred in doInBackground()");
        }
        return null;
    }
}
