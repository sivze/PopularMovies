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
import me.sivze.popularmovies.activity.MoviesActivity;
import me.sivze.popularmovies.adapter.MoviesAdapter;
import me.sivze.popularmovies.listener.OnItemClickListener;
import me.sivze.popularmovies.model.MovieModel;
import me.sivze.popularmovies.service.MoviesService;
import me.sivze.popularmovies.util.ServiceUtil;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment implements OnItemClickListener {

    private MoviesAdapter mMovieAdapter;
    private String mSortSetting;
    private RecyclerView mMoviesRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        mMoviesRecyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mMovieAdapter = new MoviesAdapter(getContext(), new ArrayList<MovieModel>());
        mMovieAdapter.setOnItemClickListener(this);

        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        updateMovies(ServiceUtil.SORT_TYPE_POPULAR);
    }

    @Override
    public void onItemClick(MovieModel movie) {
        ((MoviesActivity) getActivity()).onMovieClicked(movie);
    }

    public void scrollToTop() {
        mMoviesRecyclerView.scrollToPosition(0);
    }

    private void setSortingMode(String sortType) {
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //preferences.edit().putString(getString(R.string.pref_sortmode_key), val).apply();
        mSortSetting = sortType;
    }

    public void updateMovies(String sortType) {
        setSortingMode(sortType);
        MoviesService movieServicesTask = new MoviesService(getActivity(), mMovieAdapter);
        movieServicesTask.execute(mSortSetting);
    }
}

