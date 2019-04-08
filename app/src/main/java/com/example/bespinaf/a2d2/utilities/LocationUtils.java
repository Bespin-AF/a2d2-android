package com.example.bespinaf.a2d2.utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

public class LocationUtils {
    private static float MAX_DISTANCE_IN_MILES = 25f;
    private static float MI_TO_KM_RATIO = 1.609344f;
    private static float KM_TO_MI_RATIO = 0.62137f;
    private static Location lastKnownLocation;

    public interface onLocationChanged {
        void then(Location location);
    }

    public static Location getLastKnownGPSLocation(Context context) {
        Location lastGPSLocation = getLastKnownLocation(context, LocationManager.GPS_PROVIDER);
        if(lastKnownLocation == null){
            lastKnownLocation = lastGPSLocation;
            return lastGPSLocation;
        } else if (lastGPSLocation == null) {
            return lastKnownLocation; //While it may not be GPS necessarily, something is better than nothing
        }

        return getMostRecentLocation(lastKnownLocation, lastGPSLocation); //Newer location is better than older
    }

    private static Location getMostRecentLocation(Location first, Location second){
        return (first.getTime() > second.getTime()) ? first : second;
    }

    public static Location getLastKnownNetworkLocation(Context context){
        return getLastKnownLocation(context, LocationManager.NETWORK_PROVIDER);
    }

    public static void getCurrentNetworkLocationAsync(Context context, onLocationChanged locationHandler){
        getCurrentLocationAsync(context, LocationManager.NETWORK_PROVIDER, locationHandler);
    }

    public static void getCurrentGPSLocationAsync(Context context, onLocationChanged locationHandler){
        getCurrentLocationAsync(context, LocationManager.GPS_PROVIDER, locationHandler);
    }

    public static boolean isGPSEnabled(Context context){
        return isProviderEnabled(context, LocationManager.GPS_PROVIDER);
    }

    public static  boolean isNetworkEnabled(Context context){
        return isProviderEnabled(context, LocationManager.NETWORK_PROVIDER);
    }

    private static Location getLastKnownLocation(Context context, String provider){
        if(context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return null;
        } else if (!isProviderEnabled(context, provider)){
            return null;
        }

        return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).getLastKnownLocation(provider);
    }

    private static void getCurrentLocationAsync(Context context, String provider, onLocationChanged locationHandler){
        if(!( context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED )
                || !(context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            return;
        } else if (!isProviderEnabled(context, provider)){
            return;
        }

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestSingleUpdate(provider, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lastKnownLocation = location;
                if(locationHandler != null) {
                    locationHandler.then(location);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override
            public void onProviderEnabled(String provider) { }
            @Override
            public void onProviderDisabled(String provider) { }
        }, null);
    }


    private static boolean isProviderEnabled(Context context, String provider){
        return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE) ).isProviderEnabled(provider);
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
