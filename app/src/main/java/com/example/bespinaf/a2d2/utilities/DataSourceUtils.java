package com.example.bespinaf.a2d2.utilities;

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
import java.util.TimeZone;

import static java.util.Map.Entry;

public class DataSourceUtils {
    private static String[] requestIds;
    private static HashMap<String, Request> currentRequests = new HashMap<>();
    private static String a2d2number;

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference messagesReference = database.getReference().child("requests");
    private static DatabaseReference resourceReference = database.getReference("Resources");
    private static boolean isSyncingRequests = false;
    private static InitialSyncCallback initialSyncCallback = null;
    private static final String DISPLAY_DATE_FORMAT = "MMM dd, HHmm";
    //Millseconds are hardcoded in order to prevent iOS from breaking when they read in the time
    private static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss +0000";
    public static SimpleDateFormat displayDateFormatter;
    public static SimpleDateFormat databaseDateFormatter;

    private static ValueEventListener dataSyncEvent = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            updateRequests(dataSnapshot);
            runInitialCallback();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            //Show an error
        }
    };


    //Creates standard date formatters for use throughout application
    public static void initDateFormatters(){
        displayDateFormatter =  new SimpleDateFormat(DISPLAY_DATE_FORMAT);
        databaseDateFormatter = new SimpleDateFormat(DATABASE_DATE_FORMAT);

        displayDateFormatter.setTimeZone(TimeZone.getDefault());
        databaseDateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }


    public static String convertDateFormat(String inputDate, SimpleDateFormat from, SimpleDateFormat to) {
        String output = "";

        try {
            Date outputDate = from.parse(inputDate);
            output = to.format(outputDate);
        } catch (ParseException e){
            Log.e("Date Parse", "Failed to convert dates");
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
        Date currentDateTime = Calendar.getInstance().getTime();
        return databaseDateFormatter.format(currentDateTime);
    }


    public static void startRequestSync(){
        startRequestSync(null);
    }


    public static void startRequestSync(InitialSyncCallback callback){
        if (isSyncingRequests) {
            //Triggers callback if initial data already exists
            runInitialCallback();
            return;
        }
        else { isSyncingRequests = true; }

        initialSyncCallback = callback;
        messagesReference.addValueEventListener(dataSyncEvent);
    }


    public static void stopRequestSync(){
        if(isSyncingRequests) { isSyncingRequests = false; }
        else { return; }

        messagesReference.removeEventListener(dataSyncEvent);
    }


    private static void runInitialCallback(){
        if(initialSyncCallback != null){
            initialSyncCallback.callback();
            initialSyncCallback = null;
        }
    }

    //Update currentRequests with data from the snapshot
    private static void updateRequests(DataSnapshot snapshot){
        currentRequests.clear();
        for (DataSnapshot ds : snapshot.getChildren()) {
            Request currentRequest = ds.getValue(Request.class);

            currentRequests.put(ds.getKey(), currentRequest);
        }
    }

    public static HashMap<String, Request> getRequestsWithStatus(String targetStatus){
        HashMap<String, Request> requests = new HashMap<>();

        for(Entry<String, Request> entry : currentRequests.entrySet()){
            if(entry.getValue().getStatus().equals(targetStatus)){
                requests.put(entry.getKey(), entry.getValue());

            }
        }

        return requests;
    }


    public static HashMap<String, Request> getCurrentRequests(){
        return currentRequests;
    }

    public static String getA2D2Number(){ return a2d2number;}

    public static void getResource(String property, InitialSyncCallback callback){
        resourceReference.child(property).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                a2d2number = dataSnapshot.getValue(String.class);
                if(callback != null){ callback.callback(); }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", databaseError.getMessage());
            }
        });
    }

    //Link about updating/receiving from firebase, and handling errors: https://stackoverflow.com/questions/49979998/firebase-exception-handling-around-setvalue
    public static void sendData(Object data){
        DatabaseReference dataReference = messagesReference.push();
        updateData(dataReference.getKey(), data);
    }


    public static void updateData(String key, Object data){
        messagesReference.child(key).setValue(data);
    }
}
