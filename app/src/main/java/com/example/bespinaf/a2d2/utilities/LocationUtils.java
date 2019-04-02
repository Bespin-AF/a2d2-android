package com.example.bespinaf.a2d2.utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationUtils {
    private static float MAX_DISTANCE_IN_MILES = 25f;
    private static float MI_TO_KM_RATIO = 1.609344f;
    private static float KM_TO_MI_RATIO = 0.62137f;


    public static Location getCurrentLocation(Context context) {
        if(context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return null;
        }

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }


    public static boolean isInRange(Location from, Location to){
        return isInRange(from, to, MAX_DISTANCE_IN_MILES);
    }


    public static boolean isInRange(Location from, Location to, float range){
        return getDistanceInMiles(from, to) <= range;
    }


    private static float getDistanceInMiles(Location from, Location to){
        float distance = from.distanceTo(to);
        return convert_KM_TO_MI(distance / 1000); //Division converts meters to KM
    }


    private static float convert_KM_TO_MI(float distance){
        return distance * KM_TO_MI_RATIO;
    }


    private static float convert_MI_TO_KM(float distance){
        return distance * MI_TO_KM_RATIO;
    }
}
