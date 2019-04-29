package com.example.bespinaf.a2d2.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DataSource {
    private DatabaseReference databaseRef;
    private DataReceiver receiver;
    private ValueEventListener dataSyncEvent;

    public DataSource(DataSourceType type){
    	String table = getTableNameFromType(type);
    	databaseRef = FirebaseDatabase.getInstance().getReference().child(table);
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


    private String getTableNameFromType(DataSourceType type){
        switch (type){
            case Resource:
                return "Resources";
            case Requests:
                return "requests";
            case TestRequests:
                return "test_requests";
        }
        return "";
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