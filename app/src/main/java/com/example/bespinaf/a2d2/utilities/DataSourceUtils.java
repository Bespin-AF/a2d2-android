package com.example.bespinaf.a2d2.utilities;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.bespinaf.a2d2.InitialSyncCallback;
import com.example.bespinaf.a2d2.models.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataSourceUtils {
    private static String PHONE_NUMBER_FORMAT = "(%1$s) %2$s-%3$s";
    private static String[] requestIds;
    private static HashMap<String, Request> requests = new HashMap<>();
    public static String a2d2PhoneNumber;
    public static Location a2d2BaseLocation;

    private static boolean isSyncingRequests = false;

    private static InitialSyncCallback requestCallback = null;

    private static String REQUESTS_TABLE_KEY = "requests";
    private static String RESOURCES_TABLE_KEY = "Resources";
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseRoot = database.getReference();
    private static DatabaseReference requestsReference = databaseRoot.child(REQUESTS_TABLE_KEY);
    private static DatabaseReference resourcesReference = databaseRoot.child(RESOURCES_TABLE_KEY);
    private static ValueEventListener requestListener;
    interface onSuccess{ void then(DataSnapshot snapshot); }
    interface onFailure{ void then(DatabaseError error); }

    private static final String DISPLAY_DATE_FORMAT = "MMM dd, HHmm";
    private static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss +0000";  //+SSSS refers to time zone offset; UTC is 0000
    private static SimpleDateFormat displayDateFormatter;
    private static SimpleDateFormat databaseDateFormatter;

    public static void initializeDateFormatters(){
        displayDateFormatter =  new SimpleDateFormat(DISPLAY_DATE_FORMAT);
        databaseDateFormatter = new SimpleDateFormat(DATABASE_DATE_FORMAT);

        displayDateFormatter.setTimeZone(TimeZone.getDefault());
        databaseDateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }


    private static String convertDateFormat(String inputDate, SimpleDateFormat from, SimpleDateFormat to){
        String output = "";

        try {
            Date date = from.parse(inputDate);
            output = to.format(date);
        } catch (ParseException error) {
            Log.e("ParseError", error.getMessage());
        }

        return output;
    }


    public static String dateToDisplayFormat(String databaseDate){
        return convertDateFormat(databaseDate, databaseDateFormatter, displayDateFormatter);
    }


    public static String dateToDatabaseFormat(String displayDate){
        return convertDateFormat(displayDate, displayDateFormatter, databaseDateFormatter);
    }


    public static String getCurrentDateString(){
        Date currentDate = Calendar.getInstance().getTime();
        return databaseDateFormatter.format(currentDate);
    }


    public static String formatPhoneNumber(String phoneNumber){
        String areaCode = phoneNumber.substring(0,3),
                base = phoneNumber.substring(3,6),
                extension = phoneNumber.substring(6);
        return String.format(PHONE_NUMBER_FORMAT, areaCode, base, extension);
    }


    /* Requests Utilities */

    public static void startRequestSync(){
        startRequestSync(null);
    }

    /*
     * If there is an ongoing sync, short circuit because presumably data has already been loaded
     * Subsequent requests for data shouldn't be grabbed from syncs; these should be grabbed from static DataSourceUtils data
     * Initial Sync -> Initial Data Callback(data) -> Subsequent data updated with sync
     * runInitialRequestsCallback is called twice
     * */
    public static void startRequestSync(InitialSyncCallback callback){
        if(isSyncingRequests) {
            //TODO: Validate that this call is required.
            runInitialRequestsCallback();
            return;
        }

        isSyncingRequests = true;
        requestCallback = callback;

        requestListener = startSync(requestsReference,
                (data) -> {
                    updateRequests(data);
                    runInitialRequestsCallback();
                },
                (error) ->  Log.e("RequestFetch", error.getMessage())
        );
    }


    public static void stopRequestSync(){
        if(isSyncingRequests){ isSyncingRequests = false; }
        else { return; }

        stopSync(requestsReference, requestListener);
    }


    private static void runInitialRequestsCallback(){
        if(requestCallback != null){
            requestCallback.callback();
            requestCallback = null;
        }
    }


    public static HashMap<String, Request> getCurrentRequests(){
        return requests;
    }


    public static HashMap<String, Request> getRequestsWithStatus(String targetStatus){
        HashMap<String, Request> filteredRequests = new HashMap<>();

        for(Map.Entry<String, Request> entry : requests.entrySet()){
            if(entry.getValue().getStatus().equals(targetStatus)){
                filteredRequests.put(entry.getKey(), entry.getValue());
            }
        }

        return filteredRequests;
    }


    //Resets data in order to repopulate request list. Simplest and reasonably efficient solution
    private static void updateRequests(DataSnapshot data){
        requests.clear();
        for(DataSnapshot requestSnapshot : data.getChildren()){
            Request request = requestSnapshot.getValue(Request.class);
            requests.put(requestSnapshot.getKey(), request);
        }
    }

    //Database CRUD

    public static void addRequest(Request data){
        addData(requestsReference, data);
    }

    public static void updateRequest(String id, Request data){
        updateData(requestsReference, id, data);
    }


    public static void loadA2D2_PhoneNumber(InitialSyncCallback callback){
        queryData(resourcesReference.child("a2d2phonenumber"),
            (data) -> {
                a2d2PhoneNumber = data.getValue(String.class);
                if(callback != null) { callback.callback(); }
            },
            (error) -> {}
        );
    }


    public static void loadA2D2_BaseLocation(InitialSyncCallback callback){
        queryData(resourcesReference.child("maxwell_afb_location"),
                (data) -> {
                    updateA2D2_BaseLocation(data.getValue(String.class));
                    if(callback != null) { callback.callback(); }
                },
                (error) -> {}
        );
    }


    private static void updateA2D2_BaseLocation(String location){
        a2d2BaseLocation = parseDatabaseLocation(location);
    }


    private static Location parseDatabaseLocation(String location){
        //Matches ±Latitude,±Longitude ; does not take into account potential spaces
        Pattern locationPattern = Pattern.compile("(-?\\d+\\.\\d*),(-?\\d+\\.\\d*)");
        Matcher coordinateMatcher = locationPattern.matcher(location);

        double latitude = 0.0d;
        double longitude = 0.0d;

        try {
            if(coordinateMatcher.find()){
                latitude = Double.parseDouble(coordinateMatcher.group(1));
                longitude = Double.parseDouble(coordinateMatcher.group(2));
            }
        } catch (NullPointerException nullError) {
            Log.e("NullLocationError", "Database contained null location for A2D2 base");
        } catch (NumberFormatException formatError) {
            Log.e("NumberFormatError", "Database location was improperly formatted: " + location);
        }

        Location baseLocation = new Location("");
        baseLocation.setLatitude(latitude);
        baseLocation.setLongitude(longitude);

        return baseLocation;
    }


    /* Generic Firebase CRUD utilities */

    public static void addData(DatabaseReference table, Object data){
        DatabaseReference databaseReference = table.push();
        updateData(table, databaseReference.getKey(), data);
    };


    public static void updateData(DatabaseReference table, String key, Object data){
        table.child(key).setValue(data);
    }


    private static ValueEventListener startSync(DatabaseReference table, onSuccess success, onFailure failure){
        ValueEventListener listener = createListener(success, failure);
        table.addValueEventListener(listener);
        return listener;
    }


    private static void stopSync(DatabaseReference table, ValueEventListener... listeners){
        for(ValueEventListener listener : listeners){
            table.removeEventListener(listener);
        }
    }


    private static void queryData(DatabaseReference table, onSuccess success, onFailure failure){
        table.addListenerForSingleValueEvent(createListener(success, failure));
    }


    private static ValueEventListener createListener(onSuccess success, onFailure failure){
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                success.then(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                failure.then(databaseError);
            }
        };
    }
}