package com.example.bespinaf.a2d2.models;

import android.support.annotation.NonNull;

import com.example.bespinaf.a2d2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.ResourceBundle;

public class DataSource {
    public DatabaseReference databaseRef;
    private DataReceiver receiver;
    private ValueEventListener dataSyncEvent;
    final String BASE = "maxwell_afb";

    public DataSource(DataSourceType type){
        databaseRef = FirebaseDatabase.getInstance().getReference().child("bases").child(BASE);
    	databaseRef = getTableFromType(type);
    	dataSyncEvent = createDataSyncEvent();
    }


    @Override
    protected void finalize(){
        stopSync();
    }


    public void setReciever(DataReceiver receiver){
        this.receiver = receiver;
        startSync();
    }

    public void removeReceiver(DataReceiver receiver){
        this.receiver = null;
        stopSync();
    }


    //When submitting requests, you need to pass it request.getData() rather than the request itself
    public String sendData(Object data) {
        DatabaseReference newChild = databaseRef.push();
        newChild.setValue(data);
        return newChild.getKey();
    }


    public void updateData(String key, Object data){
        databaseRef.child(key).setValue(data);
    }


    public void removeData(String key){
        databaseRef.child(key).removeValue();
    }


    private DatabaseReference getTableFromType(DataSourceType type){
        switch (type){
            case BaseInfo:
                return databaseRef.child("base_info");
            case Requests:
                return databaseRef.child("requests");
            case TestRequests:
                return databaseRef.child("test_requests");
            case Locations:
                return FirebaseDatabase.getInstance().getReference().child("locations");
        }
        return null;
    }


    private void startSync(){
        databaseRef.addValueEventListener(dataSyncEvent);
    }


    private void stopSync(){
        databaseRef.removeEventListener(dataSyncEvent);
    }


    private ValueEventListener createDataSyncEvent(){
        DataSource dataSource = this;
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                receiver.onDataChanged(dataSource,convertSnapshotToHashMap(dataSnapshot));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO Handle Errors
            }
        };
    }


    private HashMap<String, Object> convertSnapshotToHashMap(DataSnapshot snapshot){
        HashMap<String, Object> results = new HashMap<>();

        for (DataSnapshot entry : snapshot.getChildren()) {
            String key = entry.getKey();
            Object data = entry.getValue();
            results.put(key, data);
        }

        return  results;
    }
}