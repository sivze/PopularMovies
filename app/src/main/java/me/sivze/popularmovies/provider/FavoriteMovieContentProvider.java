package me.sivze.popularmovies.provider;

import android.content.Context;
import android.net.Uri;

import java.util.List;

import me.sivze.popularmovies.BuildConfig;
import me.sivze.popularmovies.model.MovieModel;
import nl.littlerobots.cupboard.tools.provider.CupboardContentProvider;
import nl.littlerobots.cupboard.tools.provider.UriHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;


public class FavoriteMovieContentProvider extends CupboardContentProvider {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";
    public static final String DB_NAME = "movies.db";

    static {
        cupboard().register(MovieModel.class);
    }

    public FavoriteMovieContentProvider() {
        super(AUTHORITY, DB_NAME, 1);
    }

    public static MovieModel getMovieData(Context context, long id) {
        UriHelper uriHelper = UriHelper.with(FavoriteMovieContentProvider.AUTHORITY);
        Uri moviesUri = uriHelper.getUri(MovieModel.class);
        return cupboard().withContext(context).query(moviesUri, MovieModel.class).withSelection("id = ?", "" + id).get();
    }

    public static void deleteMovieData(Context context, long id) {
        UriHelper uriHelper = UriHelper.with(FavoriteMovieContentProvider.AUTHORITY);
        Uri moviesUri = uriHelper.getUri(MovieModel.class);
        cupboard().withContext(context).delete(moviesUri, "id = ?", id + "");
    }

    public static void putMovieData(Context context, MovieModel mMovieData) {
        UriHelper uriHelper = UriHelper.with(FavoriteMovieContentProvider.AUTHORITY);
        Uri movieUri = uriHelper.getUri(MovieModel.class);
        cupboard().withContext(context).put(movieUri, mMovieData);
    }

    public static List<MovieModel> getFavorites(Context context){
        UriHelper uriHelper = UriHelper.with(FavoriteMovieContentProvider.AUTHORITY);
        Uri movieUri = uriHelper.getUri(MovieModel.class);
       return cupboard().withContext(context).query(movieUri, MovieModel.class).list();
    }
}
