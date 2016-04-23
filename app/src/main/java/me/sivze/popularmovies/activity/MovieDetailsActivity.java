package me.sivze.popularmovies.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import butterknife.ButterKnife;
import me.sivze.popularmovies.R;
import me.sivze.popularmovies.fragment.MovieDetailsFragment;

/**
 * It sets the MovieDetailFragment to display detail data for the selected movie.
 */
public class MovieDetailsActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle(getIntent().getExtras());
            fragmentTransaction.add(R.id.movie_detail_container, MovieDetailsFragment.newInstance(bundle)).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
