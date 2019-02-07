package com.example.bespinaf.a2d2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.bespinaf.a2d2.adapters.RideRequestAdapter;
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
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Request currentRequest = ds.getValue(Request.class);

                    mCurrentRequests.add(currentRequest);
                }

                setRecyclerViews(mCurrentRequests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RideRequests.this, "THERE WAS AN ERROR CONNECTING",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setRecyclerViews(List<Request> ourRequests) {

        ArrayList<Request> listAvailable = new ArrayList<>();
        ArrayList<Request> listInProgress = new ArrayList<>();
        ArrayList<Request> listCompleted = new ArrayList<>();
        //TODO Handle if there is a status that is not listed
        // List<Request> listNA;

        for (Request requestItem : ourRequests) {
            Log.e("**STATUS", requestItem.getStatus());
            if(requestItem.getStatus().equals("Available")){
                listAvailable.add(requestItem);
            }else if(requestItem.getStatus().equals("In Progress")){
                listInProgress.add(requestItem);
            }else if(requestItem.getStatus().equals("Completed")){
                listCompleted.add(requestItem);
            }
        }

        Log.e("***LIST VALUES",  listAvailable.toString() + listInProgress + listCompleted);
        //inflate the recycler views

        inflateRecyclerView(rideRequestsAvailableRecyclerView, listAvailable);
        inflateRecyclerView(rideRequestsInProgressRecyclerView, listInProgress);
        inflateRecyclerView(rideRequestsCompletedRecyclerView, listCompleted);
    }

    public void inflateRecyclerView(RecyclerView view, ArrayList<Request> list){
        RideRequestAdapter adapter = new RideRequestAdapter(list);
        LinearLayoutManager llmRequestManager = new LinearLayoutManager(this);
        view.setLayoutManager(llmRequestManager);

        //Sets the dividers between items in the recycler view
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                view.getContext(),
                llmRequestManager.getOrientation());
        view.addItemDecoration(dividerItemDecoration);

        view.setAdapter(adapter);
    }
}
