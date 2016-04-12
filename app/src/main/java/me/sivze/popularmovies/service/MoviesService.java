package me.sivze.popularmovies.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import me.sivze.popularmovies.adapter.MoviesAdapter;
import me.sivze.popularmovies.model.MovieModel;
import me.sivze.popularmovies.model.MovieResponseModel;
import me.sivze.popularmovies.util.ServiceUtil;

/**
 * Created by Siva on 4/6/2016.
 */
public class MoviesService extends AsyncTask<String, Void, List<MovieModel>> {

    private Context mContext;
    private MoviesAdapter mMoviesAdapter;

    public MoviesService(Context context, MoviesAdapter moviesAdapter){
        mContext = context;
        mMoviesAdapter = moviesAdapter;
    }

    @Override
    protected List<MovieModel> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesResponse;

        String sortType = params[0];

        try{

            URL url = new URL(ServiceUtil.buildMoviesServiceUrl(sortType));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                moviesResponse = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                moviesResponse = null;
            }

            moviesResponse = buffer.toString();

        } catch (IOException e) {
            Log.e(MoviesService.class.getSimpleName(), "Error: ", e);
            moviesResponse = null;
        }

        MovieResponseModel moviesJson = new Gson().fromJson(moviesResponse, MovieResponseModel.class);

        if (moviesJson!=null && moviesJson.getResults().size()>0)
            return moviesJson.getResults();

        return null;
    }

    protected void onPostExecute(List<MovieModel> result) {
        if (result != null) {
            mMoviesAdapter.updateDataSet(result);
        }
    }
}
