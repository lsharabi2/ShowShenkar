package il.ac.shenkar.endofyearshenkar.utils;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by TomGoldberg on 27.6.16.
 */
public class CurrentLocationListener implements android.location.LocationListener {

    public CurrentLocationListener() {
    }

    @Override
    public void onLocationChanged(Location location) {
        location.getLatitude();
        location.getLongitude();

        String myLocation = "Latitude = " + location.getLatitude() + " Longitude = " + location.getLongitude();

        //I make a log to see the results
        Log.e("MY CURRENT LOCATION", myLocation );

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }
    @Override
    public void onProviderEnabled(String s) {

    }
    @Override
    public void onProviderDisabled(String s) {

    }
}
