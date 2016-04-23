package me.sivze.popularmovies.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import me.sivze.popularmovies.R;

/**
 * Created by Siva on 4/20/2016.
 * Base class for the activities, it has common methods for all extended activities.
 */

public class BaseActivity extends AppCompatActivity{
    @Nullable
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setToolbar(boolean showHomeUp, boolean showTitle) {
        setToolbar(mToolbar, showHomeUp, showTitle);
    }

    public void setToolbar(Toolbar mToolbar, boolean showHomeUp, boolean showTitle) {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeUp);
            getSupportActionBar().setDisplayShowTitleEnabled(showTitle);
        }
    }
}
