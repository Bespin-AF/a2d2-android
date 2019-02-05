package com.example.bespinaf.a2d2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.bespinaf.a2d2.utilities.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RideRequests extends AppCompatActivity {

    @BindView(R.id.ride_requests_available_recycler_view)
    RecyclerView rideRequestsAvailableRecyclerView;
    @BindView(R.id.ride_requests_in_progress_recycler_view)
    RecyclerView rideRequestsInProgressRecyclerView;
    @BindView(R.id.ride_requests_completed_recycler_view)
    RecyclerView rideRequestsCompletedRecyclerView;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRequestsReference;

    public ArrayList<Request> mCurrentRequests = new ArrayList<Request>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_requests);
        ButterKnife.bind(this);

        rideRequestsAvailableRecyclerView.setHasFixedSize(true);
        rideRequestsCompletedRecyclerView.setHasFixedSize(true);
        rideRequestsInProgressRecyclerView.setHasFixedSize(true);

        //set up database connection
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRequestsReference = mFirebaseDatabase.getReference().child("requests");


        mRequestsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Request request = dataSnapshot.getValue(Request.class);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Request currentRequest = new Request();
                    currentRequest = ds.getValue(Request.class);
                    Log.e("***Request populated***", ds.getValue().toString() + currentRequest.toString());
                    //getting each object and adding it to the list of objects
//                    currentRequest.setDriver(ds.child("driver").getValue().toString());
//                    currentRequest.setGender(ds.child("gender").getValue().toString());
//                    currentRequest.setPhone(ds.child("phone").getValue().toString());
//                    currentRequest.setName(ds.child("name").getValue().toString());
//                    currentRequest.setGroupSize(Integer.parseInt(ds.child("groupSize").getValue().toString()));
//                    currentRequest.setLon((double) ds.child("lon").getValue());
//                    currentRequest.setLat((double) ds.child("lat").getValue());
//                    currentRequest.setRemarks(ds.child("remarks").getValue().toString());
//                    currentRequest.setStatus(ds.child("status").getValue().toString());
//                    currentRequest.setTimestamp(ds.child("timestamp").getValue().toString());
                    mCurrentRequests.add(currentRequest);
                }

                setRecyclerViews(mCurrentRequests);

                Toast.makeText(RideRequests.this, dataSnapshot.toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RideRequests.this, "THERE WAS AN ERROR CONNECTING",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setRecyclerViews(List<Request> ourRequests) {
        for (Request requestItem : ourRequests) {

        }
    }
}
