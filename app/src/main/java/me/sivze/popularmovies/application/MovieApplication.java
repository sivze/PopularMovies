package me.sivze.popularmovies.application;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Main application class
 */
public class MovieApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
