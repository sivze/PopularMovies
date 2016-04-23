package me.sivze.popularmovies.util;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;

/**
 * Created by Siva on 4/21/2016.
 */
public class DeviceUtil {
    public static boolean isLandscape(Context context){
        return Configuration.ORIENTATION_LANDSCAPE == context.getResources().getConfiguration().orientation;
    }

    public static boolean isOnline(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected());
    }
}
