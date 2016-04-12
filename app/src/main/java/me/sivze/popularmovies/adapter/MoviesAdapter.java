package me.sivze.popularmovies.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.sivze.popularmovies.R;
import me.sivze.popularmovies.listener.OnItemClickListener;
import me.sivze.popularmovies.model.MovieModel;
import me.sivze.popularmovies.util.ServiceUtil;

/**
 * Created by Siva on 4/6/2016.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<MovieModel> mMovieList;
    private OnItemClickListener mItemClickListener;

    public MoviesAdapter(Context context, @NonNull List<MovieModel> movies) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mMovieList = new ArrayList<MovieModel>();
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.movie_poster, parent, false);
        return new MovieHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        holder.bindMovieItem(mMovieList.get(position), mItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }

    public void updateDataSet(List<MovieModel> newMovies) {
        mMovieList.clear();
        mMovieList.addAll(newMovies);
        this.notifyDataSetChanged();
    }

    public static final class MovieHolder extends RecyclerView.ViewHolder {
        ImageView mPoster;

        public MovieHolder(View view) {
            super(view);
            mPoster = (ImageView) view.findViewById(R.id.movie_poster_image_view);
        }

        public void bindMovieItem(@NonNull final MovieModel movie, @NonNull final OnItemClickListener listener) {
            Glide.with(itemView.getContext())
                    .load(ServiceUtil.buildPosterUrl(movie.getPosterPath()))
                    .into(mPoster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movie);
                }
            });
        }
    }
}
