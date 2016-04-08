package me.sivze.popularmovies.util;

import android.net.Uri;

/**
 * Created by Siva on 4/6/2016.
 */
public class ServiceUtil {

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";

    public static String buildPosterUrl(String imagePath) {
        return POSTER_BASE_URL + "/w342"+ imagePath;
    }

    public static String buildMoviesServiceUrl(String sortType){
        final String API_KEY_PARAM = "api_key";

        Uri moviesServiceUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(sortType)
                .appendQueryParameter(API_KEY_PARAM, "04514068c761482f8ca2912905651833")
                .build();



        return moviesServiceUri.toString();
    }
}
