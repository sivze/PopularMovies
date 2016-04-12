package me.sivze.popularmovies.util;

import android.net.Uri;

/**
 * Created by Siva on 4/6/2016.
 */
public class ServiceUtil {

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String SORT_TYPE_POPULAR = "popular";
    public static final String SORT_TYPE_TOP_RATED = "top_rated";

    public static String buildPosterUrl(String imagePath) {
        return POSTER_BASE_URL + "/w342" + imagePath;
    }

    public static String buildMoviesServiceUrl(String sortType) {
        final String API_KEY_PARAM = "api_key";

        Uri moviesServiceUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(sortType)
                .appendQueryParameter(API_KEY_PARAM, KeyKeeper.movieDBAPIkey)
                .build();

        return moviesServiceUri.toString();
    }
}
