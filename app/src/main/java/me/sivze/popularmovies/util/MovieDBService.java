package me.sivze.popularmovies.util;

import me.sivze.popularmovies.model.MovieResponseModel;
import me.sivze.popularmovies.model.ReviewsResponseModel;
import me.sivze.popularmovies.model.TrailersResponseModel;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface MovieDBService {

    @GET("/3/discover/movie")
    Call<MovieResponseModel> getMovieResults(@Query("api_key") String apiKey, @Query("sort_by") String sortBy);

    @GET("/3/movie/{id}/videos")
    Call<TrailersResponseModel> getTrailersResults(@Path("id") long movieId, @Query("api_key") String apiKey);

    @GET("/3/movie/{id}/reviews")
    Call<ReviewsResponseModel> getReviewsResults(@Path("id") long movieId, @Query("api_key") String apiKey);

}
