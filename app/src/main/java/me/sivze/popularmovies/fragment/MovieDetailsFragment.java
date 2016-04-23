package me.sivze.popularmovies.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.sivze.popularmovies.R;
import me.sivze.popularmovies.activity.MovieDetailsActivity;
import me.sivze.popularmovies.model.MovieModel;
import me.sivze.popularmovies.model.ReviewModel;
import me.sivze.popularmovies.model.TrailerModel;
import me.sivze.popularmovies.task.CommonAsyncTask;
import me.sivze.popularmovies.task.ReviewsAsyncTask;
import me.sivze.popularmovies.task.TrailersAsyncTask;
import me.sivze.popularmovies.util.CommonUtil;
import me.sivze.popularmovies.util.Constants;
import me.sivze.popularmovies.provider.FavoriteMovieContentProvider;

/**
 *This class fetches and displays the movies details data.
 */
public class MovieDetailsFragment extends Fragment {

    public static final String TAG = "MovieDetailFragment";

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.movie_detail_poster_image_view)
    ImageView mPosterMovie;

    @Bind(R.id.movie_detail_backdrop_image_view)
    ImageView mBackdropMovie;

    @Bind(R.id.movie_detail_rate_image_view)
    ImageView mDetailRateImageView;

    @Bind(R.id.movie_detail_play_trailer_image_view)
    ImageView mPlayTrailerImageView;

    @Bind(R.id.movie_detail_rate_value_text_view)
    TextView mDetailRateTextView;

    @Bind(R.id.movie_detail_title_text_view)
    TextView mDetailMovieTitle;

    @Bind(R.id.movie_detail_year_text_view)
    TextView mDetailMovieYear;

    @Bind(R.id.movie_detail_synopsys_data_text_view)
    TextView mDetailMovieSynopsis;

    @Bind(R.id.empty_trailer_list)
    TextView mDetailMovieEmptyTrailers;

    @Bind(R.id.empty_review_list)
    TextView mDetailMovieEmptyReviews;

    @Nullable
    @Bind(R.id.favorite_fab)
    FloatingActionButton mFavoriteFab;

    @Bind({R.id.appbar, R.id.inc_movie_detail})
    List<View> viewContainers;

    @Bind(R.id.inc_no_selected_movie)
    View noSelectedView;

    @Bind(R.id.movie_detail_trailer_progress_bar)
    ProgressBar mTrailersProgressBar;

    @Bind(R.id.movie_detail_review_progress_bar)
    ProgressBar mReviewsProgressBar;

    @Bind(R.id.movie_detail_trailer_container)
    LinearLayout mTrailerLinearLayout;

    @Bind(R.id.detail_movie_reviews_container)
    LinearLayout mReviewLinearLayout;

    private MovieModel mMovieData;
    private Bitmap mPosterImage;
    private boolean mAddedInFavorite;
    private ArrayList<TrailerModel> mTrailers;
    private ArrayList<ReviewModel> mReviews;
    private TrailerModel mMainTrailer;
    private TrailersAsyncTask trailersAsyncTask;
    private ReviewsAsyncTask reviewsAsyncTask;

    public static MovieDetailsFragment newInstance(Bundle bundle) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public MovieDetailsFragment() {
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.FAVORITE, mAddedInFavorite);
        outState.putParcelableArrayList(Constants.TRAILERS, mTrailers);
        outState.putParcelableArrayList(Constants.REVIEWS, mReviews);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mPosterImage = getArguments().getParcelable(Constants.POSTER_IMAGE_KEY);
            mMovieData = getArguments().getParcelable(Constants.MOVIE_DETAIL_KEY);
            if (mMovieData != null) {
                mAddedInFavorite = FavoriteMovieContentProvider.getMovieData(getActivity(), mMovieData.id) != null;
            }
//            Log.d(TAG, "onCreate() called with: " + "mMovieData = [" + mMovieData + "]");
            Log.d(TAG, "onCreate() called with: " + "mAddedInFavorite = [" + mAddedInFavorite + "]");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_details, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.share_movie:
                openShareIntent(mMainTrailer);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        Log.d(TAG, "onCreateView() called");
        ButterKnife.bind(this, view);

        if (getActivity() instanceof MovieDetailsActivity) {
            MovieDetailsActivity detailActivity = (MovieDetailsActivity) getActivity();
            detailActivity.setToolbar(mToolbar, true, false);
        }
        if (savedInstanceState != null) {
            mTrailers = savedInstanceState.getParcelableArrayList(Constants.TRAILERS);
            mReviews = savedInstanceState.getParcelableArrayList(Constants.REVIEWS);
            mAddedInFavorite = savedInstanceState.getBoolean(Constants.FAVORITE);
            mMainTrailer = savedInstanceState.getParcelable(Constants.MAIN_TRAILER);
            addTrailerViews(mTrailers);
            addReviewViews(mReviews);
        } else {
            executeTasks(mMovieData);
        }
        initView(mMovieData);
        return view;
    }

    /**
     * Runs tasks in background and retrives movie trailers and reviews
     * @param mMovieData current MovieData
     */
    private void executeTasks(MovieModel mMovieData) {

        if (mMovieData == null) {
            return;
        }

        trailersAsyncTask = new TrailersAsyncTask(mMovieData.id, mTrailersProgressBar,
                new CommonAsyncTask.FetchDataListener<TrailerModel>() {
            @Override
            public void onFetchData(ArrayList<TrailerModel> resultList) {
//                Log.d(TAG, "TrailersAsyncTask.onFetchData() returned: " + resultList);
                mTrailers = resultList;
                addTrailerViews(mTrailers);
            }
        });

        reviewsAsyncTask = new ReviewsAsyncTask(mMovieData.id, mReviewsProgressBar,
                new CommonAsyncTask.FetchDataListener<ReviewModel>() {
            @Override
            public void onFetchData(ArrayList<ReviewModel> resultList) {
//                Log.d(TAG, "ReviewsAsyncTask.onFetchData() returned: " + resultList);
                mReviews = resultList;
                addReviewViews(mReviews);
            }
        });

        trailersAsyncTask.execute();
        reviewsAsyncTask.execute();

    }


    /**
     * Dynamically added trailers views in the view container
     * @param resultList list of Trailer
     */
    private void addTrailerViews(List<TrailerModel> resultList) {

        final LayoutInflater inflater = LayoutInflater.from(getActivity());

        boolean emptyList = resultList == null || resultList.isEmpty();

        if (resultList != null && !resultList.isEmpty()) {
            mMainTrailer = resultList.get(0);
            mPlayTrailerImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openYouTubeIntent(mMainTrailer.key);
                }
            });
            for (TrailerModel trailer : resultList) {
                final String key = trailer.key;
                final View trailerView = inflater.inflate(R.layout.list_item_trailer, mTrailerLinearLayout, false);
                ImageView trailerImage = ButterKnife.findById(trailerView, R.id.trailer_poster_image_view);
                ImageView playImage = ButterKnife.findById(trailerView, R.id.play_trailer_image_view);
                playImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openYouTubeIntent(key);
                    }
                });

                Picasso.with(getActivity())
                        .load(String.format(Constants.YOU_TUBE_IMG_URL, trailer.key))
                        .placeholder(R.drawable.ic_movie_placeholder)
                        .error(R.drawable.ic_movie_placeholder)
                        .into(trailerImage);
                mTrailerLinearLayout.addView(trailerView);
            }
        }
        mDetailMovieEmptyTrailers.setVisibility(emptyList ? View.VISIBLE : View.GONE);

    }

    /**
     * Dynamically added reviews views in the view container
     * @param resultList list of Review
     */
    private void addReviewViews(List<ReviewModel> resultList) {

        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        boolean emptyList = resultList == null || resultList.isEmpty();

        if (!emptyList) {
            for (ReviewModel review : resultList) {
                final View reviewView = inflater.inflate(R.layout.list_item_review, mReviewLinearLayout, false);
                TextView reviewAuthor = ButterKnife.findById(reviewView, R.id.list_item_review_author_text_view);
                TextView reviewContent = ButterKnife.findById(reviewView, R.id.list_item_review_content_text_view);
                reviewAuthor.setText(review.author);
                reviewContent.setText(review.content);
                mReviewLinearLayout.addView(reviewView);
            }
        }
        mDetailMovieEmptyReviews.setVisibility(emptyList ? View.VISIBLE : View.GONE);
    }


    private void initView(MovieModel movieData) {
        if (movieData == null) {
            toggleNonSelectedView(true);
            return;
        }

        toggleNonSelectedView(false);
        switchFabIcon();

        String imageUrl = Constants.IMAGE_MOVIE_URL + Constants.IMAGE_SIZE_W500 + mMovieData.backdropPath;

        Picasso.with(getActivity())
                .load(imageUrl)
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.drawable.ic_movie_placeholder)
                .into(mBackdropMovie);

        mPosterMovie.setImageBitmap(mPosterImage);
        mDetailRateImageView.setImageResource(CommonUtil.getRateIcon(movieData.voteAverage, true));
        mDetailRateImageView.setContentDescription(getString(R.string.a11y_movie_title, movieData.originalTitle));

        mDetailMovieTitle.setText(movieData.originalTitle);
        mDetailMovieTitle.setContentDescription(getString(R.string.a11y_movie_title, movieData.originalTitle));

        mDetailRateTextView.setText(String.format("%d/10", Math.round(movieData.voteAverage)));
        mDetailRateTextView.setContentDescription(getString(R.string.a11y_movie_rate, String.format("%d/10",
                Math.round(movieData.voteAverage))));

        mDetailMovieSynopsis.setText(movieData.overview);
        mDetailMovieSynopsis.setContentDescription(getString(R.string.a11y_movie_overview, movieData.overview));

        if (movieData.getFormattedDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(movieData.getFormattedDate().getTime());
            mDetailMovieYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
            mDetailMovieYear.setContentDescription(getString(R.string.a11y_movie_year, String.valueOf(calendar.get(Calendar.YEAR))));
        }
    }

    /**
     * Switch FAB icons
     */
    private void switchFabIcon() {
        mFavoriteFab.setImageResource(mAddedInFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_outline);
    }


    /**
     * Show/hide empty message and containers depending on movie data
     * @param noMovieData
     */
    private void toggleNonSelectedView(boolean noMovieData) {
        toggleVisibleFab(!noMovieData);
        noSelectedView.bringToFront();
        noSelectedView.setVisibility(noMovieData ? View.VISIBLE : View.GONE);
        for (View view : viewContainers) {
            view.setVisibility(noMovieData ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Show or hide FAB button dependding on movie data. It is used only for two panel mode
     * @param showFab identify if fab should be shown
     */
    private void toggleVisibleFab(boolean showFab) {
        if (mFavoriteFab != null) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) mFavoriteFab.getLayoutParams();
            p.setAnchorId(showFab ? R.id.appbar : View.NO_ID);
            mFavoriteFab.setLayoutParams(p);
            mFavoriteFab.setVisibility(showFab ? View.VISIBLE : View.GONE);
        }
    }

    @OnClick(R.id.favorite_fab)
    public void toggleFavorite() {
        //TODO Mladen add fade out/fade in animation for FAB
        int resultMsg;
        if (!mAddedInFavorite) {
            FavoriteMovieContentProvider.putMovieData(getActivity(), mMovieData);
            resultMsg = R.string.added_to_favorite;
            Log.d(TAG, "toggleFavorite() called to add favorite movie");
        } else {
            FavoriteMovieContentProvider.deleteMovieData(getActivity(), mMovieData.id);
            resultMsg = R.string.removed_from_favorite;
            Log.d(TAG, "toggleFavorite() called to delete from favorite movie list");
        }
        mAddedInFavorite = !mAddedInFavorite;
        Snackbar.make(getView(), resultMsg, Snackbar.LENGTH_SHORT).show();
        switchFabIcon();
    }

    private void openYouTubeIntent(String key) {
        Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOU_TUBE_VIDEO_URL + key));
        youTubeIntent.putExtra("force_fullscreen", true);
        startActivity(youTubeIntent);
    }


    /**
     * Share trailer
     * @param trailer
     */
    private void openShareIntent(TrailerModel trailer) {
        if (trailer != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, Constants.YOU_TUBE_VIDEO_URL + trailer.key);
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, mMovieData.originalTitle);
            startActivity(Intent.createChooser(intent, getActivity().getString(R.string.share)));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (trailersAsyncTask != null) {
            trailersAsyncTask.cancel(true);
            trailersAsyncTask = null;
        }

        if (reviewsAsyncTask != null) {
            reviewsAsyncTask.cancel(true);
            reviewsAsyncTask = null;
        }
    }
}
