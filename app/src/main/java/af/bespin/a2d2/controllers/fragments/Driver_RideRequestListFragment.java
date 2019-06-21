package af.bespin.a2d2.controllers.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import af.bespin.a2d2.R;
import af.bespin.a2d2.adapters.RideRequestAdapter;
import af.bespin.a2d2.models.Request;

public class Driver_RideRequestListFragment extends Fragment {
    Context mContext;
    Request[] mRequests;
    RecyclerView mRequestList;

    public Driver_RideRequestListFragment(){

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View recyclerViewTab = inflater.inflate(R.layout.fragment_ride_request_list, container, false);
        mContext = container.getContext();
        mRequestList = recyclerViewTab.findViewById(R.id.ride_requests_recyclerview);

        return recyclerViewTab;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if(getArguments() != null){
            mRequests = (Request[]) getArguments().getSerializable("requests");
        }

        populateRecyclerView();
    }

    public void populateRecyclerView() {
        RideRequestAdapter adapter = new RideRequestAdapter(mRequests);
        adapter.SortByDate();

        LinearLayoutManager llmRequestManager = new LinearLayoutManager(mContext);
        mRequestList.setLayoutManager(llmRequestManager);
        mRequestList.setHasFixedSize(true);

        //Divider line between items in recycler view
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mContext,
                llmRequestManager.getOrientation()
        );

        Drawable listItemDivider = mContext.getDrawable(R.drawable.ride_requests_divideritemdecoration);
        if (listItemDivider != null) {
            dividerItemDecoration.setDrawable(listItemDivider);
        }

        mRequestList.addItemDecoration(dividerItemDecoration);
        mRequestList.setAdapter(adapter);
    }
}
