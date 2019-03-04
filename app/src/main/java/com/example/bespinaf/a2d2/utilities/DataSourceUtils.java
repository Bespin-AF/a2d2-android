package com.example.bespinaf.a2d2.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.bespinaf.a2d2.InitialSyncCallback;
import com.example.bespinaf.a2d2.models.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DataSourceUtils {

    private static ArrayList<Request> currentRequests = new ArrayList<Request>();
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseRef = database.getReference().child("requests");
    private static Context toastContext;
    private static boolean isSyncingRequests = false;
    private static InitialSyncCallback initialSyncCallback = null;
    private static final String DISPLAY_DATE_FORMAT = "MMM dd, HHmm";
    private static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss +SSSS";
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
            Toast.makeText(toastContext, "THERE WAS AN ERROR CONNECTING",
                    Toast.LENGTH_SHORT).show();
        }
    };


    //Creates standard date formatters for use throughout application
    public static void initDateFormatters(){
        displayDateFormatter =  new SimpleDateFormat(DISPLAY_DATE_FORMAT);
        databaseDateFormatter = new SimpleDateFormat(DATABASE_DATE_FORMAT);
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
        databaseRef.addValueEventListener(dataSyncEvent);
    }


    public static void stopRequestSync(){
        if(isSyncingRequests) { isSyncingRequests = false; }
        else { return; }

        databaseRef.removeEventListener(dataSyncEvent);
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

            currentRequests.add(currentRequest);
        }
    }


    public static ArrayList<Request> getRequestsWithStatus(String targetStatus){
        ArrayList<Request> targetRequests = new ArrayList<>();
        for (Request request : currentRequests) {
            if (request.getStatus().equals(targetStatus)) {
                targetRequests.add(request);
            }
        }
        return targetRequests;
    }


    public static boolean updateRequest(String id, Request request){
        databaseRef.child(request.getId()).setValue(request);

        return true;
    }


    public static ArrayList<Request> getCurrentRequests(){
        return currentRequests;
    }


    public static void sendData(Object data){
        DatabaseReference dataReference = databaseRef.push();
        updateData(dataReference.getKey(), data);
    }


    public static void updateData(String key, Object data){
        databaseRef.child(key).setValue(data);
    }


    public static void setToastContext(Context newContext){
        toastContext = newContext;
    }
}
