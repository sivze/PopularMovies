package me.sivze.popularmovies.adapter;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.sivze.popularmovies.R;
import me.sivze.popularmovies.fragment.MoviesFragment;
import me.sivze.popularmovies.model.MovieModel;
import me.sivze.popularmovies.util.CommonUtil;
import me.sivze.popularmovies.util.Constants;
import me.sivze.popularmovies.util.PreferenceUtil;
import me.sivze.popularmovies.provider.FavoriteMovieContentProvider;

/**
 * Created by Siva on 4/6/2016.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieHolder> {

    private Calendar mCalendar;
    private int mDefaultColor;
    private MoviesFragment.Callback mCallback;
    private boolean mFavoriteView;
    private List<MovieModel> mMovieList;

    public MoviesAdapter(ArrayList<MovieModel> moviesList,
                         int defaultColor,
                         boolean favoriteView,
                         MoviesFragment.Callback callback) {
        this.mMovieList = moviesList;
        this.mCalendar = Calendar.getInstance();
        this.mDefaultColor = defaultColor;
        this.mCallback = callback;
        this.mFavoriteView = favoriteView;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_grid_item_template, parent, false);
        return new MovieHolder(v);
    }

    @Override
    public void onBindViewHolder(final MovieHolder holder, final int position) {
        final MovieModel movieData = mMovieList.get(position);

        String sortType = PreferenceUtil.getPrefs(holder.mSortTypeValueTextView.getContext(),
                Constants.MODE_VIEW, Constants.SORT_BY_POPULARITY_DESC);

        holder.mGridItemContainer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bitmap posterBitmap = ((BitmapDrawable) holder.mMovieImageView.getDrawable()).getBitmap();
                mCallback.onItemSelected(movieData, posterBitmap, holder.mMovieImageView, position);
            }
        });

        holder.mGridItemContainer.setContentDescription(holder.mGridItemContainer
                .getContext()
                .getString(R.string.a11y_movie_title, movieData.originalTitle));

        if (movieData.getFormattedDate() != null) {
            mCalendar.setTime(movieData.getFormattedDate());
            holder.mReleaseDateTextView.setText(String.valueOf(mCalendar.get(Calendar.YEAR)));
            holder.mReleaseDateTextView.setContentDescription(holder.mReleaseDateTextView.getContext().getString(R.string.a11y_movie_year, String.valueOf(mCalendar.get(Calendar.YEAR))));
        }

        if (Constants.SORT_BY_POPULARITY_DESC.equals(sortType)) {
            setIconForType(holder, sortType, movieData);
            holder.mSortTypeValueTextView.setText(String.valueOf(Math.round(movieData.popularity)));
        } else {
            setIconForType(holder, sortType, movieData);
            holder.mSortTypeValueTextView.setText(String.valueOf(Math.round(movieData.voteAverage)));
        }

        String imageUrl = Constants.IMAGE_MOVIE_URL + Constants.IMAGE_SIZE_W185 + movieData.posterPath;
        final RelativeLayout container = holder.mMovieTitleContainer;

        Picasso.with(holder.mMovieImageView.getContext()).load(imageUrl).placeholder(R.drawable.ic_movie_placeholder).
                into(holder.mMovieImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap posterBitmap = ((BitmapDrawable) holder.mMovieImageView.getDrawable()).getBitmap();
                        Palette.from(posterBitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                container.setBackgroundColor(ColorUtils.setAlphaComponent(palette.getMutedColor(mDefaultColor), 190)); //Opacity
                            }
                        });
                    }
                    @Override
                    public void onError() {
                    }
                });
    }

    private void setIconForType(MovieHolder holder, String sortType, MovieModel movieData) {
        if (Constants.SORT_BY_POPULARITY_DESC.equals(sortType)) {

            boolean addedInFavorite = mFavoriteView ? mFavoriteView :
                    FavoriteMovieContentProvider.getMovieData(holder.mSortTypeIconImageView.getContext(),
                            movieData.id) != null;

            holder.mSortTypeIconImageView.setImageResource(addedInFavorite ? R.drawable.ic_favorite :
                    R.drawable.ic_favorite_outline);
        } else {
            holder.mSortTypeIconImageView.setImageResource(CommonUtil.getRateIcon(movieData.voteAverage, false));
        }
    }

    @Override
    public int getItemCount() {
        return mMovieList == null ? 0 : mMovieList.size();
    }

    public void addMovies(List<MovieModel> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        mMovieList = data;
        notifyDataSetChanged();
    }

    public static final class MovieHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.grid_item_sort_type_text_view)
        TextView mSortTypeValueTextView;

        @Bind(R.id.grid_item_release_date_text_view)
        TextView mReleaseDateTextView;

        @Bind(R.id.grid_item_poster_image_view)
        ImageView mMovieImageView;

        @Bind(R.id.grid_item_sort_type_image_view)
        ImageView mSortTypeIconImageView;

        @Bind(R.id.grid_item_title_container)
        RelativeLayout mMovieTitleContainer;

        @Bind(R.id.grid_item_container)
        FrameLayout mGridItemContainer;

        public MovieHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
