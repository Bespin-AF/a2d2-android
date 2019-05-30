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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationUtils {
    private static float MAX_DISTANCE_IN_MILES = 25f;
    private static float MI_TO_KM_RATIO = 1.609344f;
    private static float KM_TO_MI_RATIO = 0.62137f;
    private static Location lastKnownLocation;
    public static HashMap<String, Location> a2d2Locations = new HashMap<>();

    public interface onLocationChanged {
        void then(Location location);
    }

    public static void updateA2D2Locations(HashMap<String, Object> locations){
        a2d2Locations.clear();

        for(Map.Entry<String, Object> location : locations.entrySet()){
            if(location.getValue() == null){
                continue;
            }

            String[] latitude_longitude = location.getValue().toString().split(",");
            Location coordinates = new Location(""){{
                setLatitude(Double.valueOf(latitude_longitude[0]));
                setLongitude(Double.valueOf(latitude_longitude[1]));
            }};

            a2d2Locations.put(location.getKey(), coordinates);
        }
    }

    //TODO: Add firebase function functionality
    /**
    * Uses the stored Firebase function in order to find the base closest to the given location
    * @param location The location to be queried against; must be in the form of "{latitude},{longitude}"
     * @return task<result> returns the name of the closest base, or "FAILURE" if an error occurs
    */
    public static Task<String> getClosestA2D2Location(String location) {
        //Reference: https://stackoverflow.com/questions/42872743/calling-a-cloud-function-from-android-through-firebase
        //Source code: https://console.cloud.google.com/functions/details/us-central1/closest?project=a2d2-22ec0&authuser=1&folder&organizationId&supportedpurview=project&tab=source&duration=PT1H
        return FirebaseFunctions.getInstance().getHttpsCallable("closest").call(location).continueWith(task -> (String) task.getResult().getData());
    }

    public void test(){
        getClosestA2D2Location("30.23423,-86.3423").addOnCompleteListener((task)->{
            if(!task.isSuccessful()){
                //The IDE has issues interpreting firebase code
                FirebaseFunctionsException fbException = (FirebaseFunctionsException) task.getException();
                FirebaseFunctionsException.Code fbExceptionCode = fbException.getCode();
                Object details = fbException.getDetails();
            }
        });
    }

    private static Location getMostRecentLocation(Location first, Location second){
        return (first.getTime() > second.getTime()) ? first : second;
    }


    public static Location getLastKnownGPSLocation(Context context) {
        Location lastGPSLocation = getLastKnownLocation(context, LocationManager.GPS_PROVIDER);

        if(lastKnownLocation == null){
            lastKnownLocation = lastGPSLocation;
            return lastGPSLocation;
        } else if (lastGPSLocation == null) {
            return lastKnownLocation; //While it may not be GPS necessarily, something is better than nothing
        }

        lastKnownLocation = getMostRecentLocation(lastKnownLocation, lastGPSLocation); //Newer location is better than older
        return lastKnownLocation;
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


    private static float convert_KM_TO_MI(float distance){
        return distance * KM_TO_MI_RATIO;
    }


    private static float convert_MI_TO_KM(float distance){
        return distance * MI_TO_KM_RATIO;
    }


    public static boolean isInRange(Location from, Location to){
        return isInRange(from, to, MAX_DISTANCE_IN_MILES);
    }


    private static float getDistanceInMiles(Location from, Location to){
        float distance = from.distanceTo(to);
        return convert_KM_TO_MI(distance / 1000); //Division converts meters to KM
    }


    public static boolean isInRange(Location from, Location to, float range){
        return getDistanceInMiles(from, to) <= range;
    }


    private static boolean isProviderEnabled(Context context, String provider){
        return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE) ).isProviderEnabled(provider);
    }
}
