package me.sivze.popularmovies.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.sivze.popularmovies.R;
import me.sivze.popularmovies.activity.MoviesActivity;
import me.sivze.popularmovies.adapter.MoviesAdapter;
import me.sivze.popularmovies.loader.FavoriteResultsLoader;
import me.sivze.popularmovies.loader.ResultsLoader;
import me.sivze.popularmovies.model.MovieModel;
import me.sivze.popularmovies.util.Constants;
import me.sivze.popularmovies.util.DeviceUtil;
import me.sivze.popularmovies.util.PreferenceUtil;
import me.sivze.popularmovies.ui.SpacesItemDecoration;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener,
        LoaderManager.LoaderCallbacks<List<MovieModel>>{

    public static final String LOG_TAG = MoviesFragment.class.getSimpleName();

    public static Fragment newInstance() {
        return new MoviesFragment();
    }

    public interface Callback {
        void onItemSelected(MovieModel movieModel, Bitmap posterBitmap, View view, int position);
    }

    @Bind(R.id.main_movie_grid_recycle_view)
    RecyclerView mMoviesGridView;

    @Bind(R.id.main_movie_sw_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.error_no_connection_frame)
    LinearLayout mNoConnectionFrame;

    @Bind(R.id.error_no_movies)
    View noMoviesView;

    private ArrayList<MovieModel> mMoviesList;
    private MoviesAdapter mMovieAdapter;
    private MoviesActivity mMoviesActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMoviesList = new ArrayList<>();
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMoviesActivity = (MoviesActivity) context;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String sortType;
        boolean result;

        switch (item.getItemId()) {
            case R.id.show_favorites:
                sortType = Constants.SHOW_FAVORITES;
                result = true;
                break;
            case R.id.sort_by_popularity_desc:
                sortType = Constants.SORT_BY_POPULARITY_DESC;
                result = true;
                break;
            case R.id.sort_by_rates_desc:
                sortType = Constants.SORT_BY_RATING_DESC;
                result = true;
                break;
            default:
                sortType = Constants.SORT_BY_POPULARITY_DESC;
                result = super.onOptionsItemSelected(item);
                break;
        }

        item.setChecked(true);
        PreferenceUtil.savePrefs(getActivity(), Constants.MODE_VIEW, sortType);
        restartLoader();
        mSwipeRefreshLayout.setRefreshing(true);
        return result;
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, view);

        int gridColumns = getResources().getInteger(R.integer.grid_columns);
        int progressViewOffsetStart = getResources().getInteger(R.integer.progress_view_offset_start);
        int progressViewOffsetEnd = getResources().getInteger(R.integer.progress_view_offset_end);

        //set progress bar color
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        //set progress bar draggable length
        mSwipeRefreshLayout.setProgressViewOffset(true, progressViewOffsetStart, progressViewOffsetEnd);

        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), gridColumns);

        mMoviesGridView.setLayoutManager(mLayoutManager);
        mMoviesGridView.setHasFixedSize(true);

        //for spacing between movies "1dp"
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        mMoviesGridView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        int colorPrimaryLight = ContextCompat.getColor(getActivity(), (R.color.colorPrimaryTransparent));
        mMovieAdapter = new MoviesAdapter(mMoviesList, colorPrimaryLight, false, (Callback) getActivity());

        mMoviesGridView.setAdapter(mMovieAdapter);
        //set listener for onRefreshListener, otherwise it will be loading indeterminately
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public Loader<List<MovieModel>> onCreateLoader(int id, Bundle args) {
        return PreferenceUtil.getPrefs(getActivity(), Constants.MODE_VIEW, Constants.SORT_BY_POPULARITY_DESC)
                .equals(Constants.SHOW_FAVORITES) ?
                new FavoriteResultsLoader(getActivity()) :
                new ResultsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<MovieModel>> loader, List<MovieModel> data) {
        mSwipeRefreshLayout.setRefreshing(false);
        mMoviesList = (ArrayList<MovieModel>) data;
        mMovieAdapter.addMovies(data);
        if (data == null || data.isEmpty()) {
            if (!DeviceUtil.isOnline(getActivity())) {
                mNoConnectionFrame.setVisibility(View.VISIBLE);
            } else {
                toggleShowEmptyMovie(false);
            }
        } else {
            toggleShowEmptyMovie(true);
        }

        if (mMoviesActivity != null && mMoviesActivity.getSelectedPosition() != -1) {
            mMoviesGridView.scrollToPosition(mMoviesActivity.getSelectedPosition());
        }

        Snackbar.make(getView(), data == null ? R.string.movies_not_found : R.string.movies_data_loaded,
                Snackbar.LENGTH_LONG).show();
    }

    private void toggleShowEmptyMovie(boolean showMovieGrid) {
        noMoviesView.setVisibility(showMovieGrid ? View.GONE : View.VISIBLE);
        mNoConnectionFrame.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<MovieModel>> loader) {
        mSwipeRefreshLayout.setRefreshing(false);
        mMovieAdapter.addMovies(null);
    }

    @Override
    public void onRefresh() {
        restartLoader();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

