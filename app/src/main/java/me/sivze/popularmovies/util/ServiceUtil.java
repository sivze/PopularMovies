package me.sivze.popularmovies.util;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Siva on 4/6/2016.
 */
public class ServiceUtil {

    private static MovieDBService service;
    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MOVIE_URL).addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(MovieDBService.class);
    }

    public static MovieDBService getService() {
        return service;
    }
}
