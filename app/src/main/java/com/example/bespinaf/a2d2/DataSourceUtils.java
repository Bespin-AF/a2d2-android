package com.example.bespinaf.a2d2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.bespinaf.a2d2.utilities.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataSourceUtils {

    private static ArrayList<Request> currentRequests = new ArrayList<Request>();
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseRef = database.getReference().child("requests");
    private static Context toastContext;

    public static void startRequestSync(){
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateRequests(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(toastContext, "THERE WAS AN ERROR CONNECTING",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void stopRequestSync(){

    }

    //Update currentRequests with data from the snapshot
    private static void updateRequests(DataSnapshot snapshot){
        currentRequests.clear();
        for (DataSnapshot ds : snapshot.getChildren()) {
            Request currentRequest = ds.getValue(Request.class);

            currentRequests.add(currentRequest);
        }
    }

    public static ArrayList<Request> getCurrentRequests(){
        return currentRequests;
    }

    public static void sendData(Request rideRequest){
        databaseRef.push().setValue(rideRequest);
    }

    public static void setToastContext(Context newContext){
        toastContext = newContext;
    }
}
