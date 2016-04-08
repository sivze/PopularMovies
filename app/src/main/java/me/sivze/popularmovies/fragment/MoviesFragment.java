package me.sivze.popularmovies.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.sivze.popularmovies.R;
import me.sivze.popularmovies.adapter.MoviesAdapter;
import me.sivze.popularmovies.model.MovieModel;
import me.sivze.popularmovies.service.MoviesService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {

    private RecyclerView mMoviesGridView;
    private MoviesAdapter mMovieAdapter;

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        RecyclerView mMoviesRecyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mMovieAdapter = new MoviesAdapter(getContext(), new ArrayList<MovieModel>());

        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MoviesService movieServicesTask = new MoviesService(getActivity(), mMovieAdapter);
        movieServicesTask.execute("popular");
    }
}
