package com.example.bespinaf.a2d2.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bespinaf.a2d2.R;

import java.util.HashMap;

public class RideRequestDetailAdapter extends RecyclerView.Adapter<RideRequestDetailAdapter.DetailViewHolder>{

    public class DetailViewHolder extends RecyclerView.ViewHolder{

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    HashMap<String,Object> details = new HashMap<>();

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View requestDetail = inflater.inflate(R.layout.card_ride_request_detail, viewGroup, false);
        DetailViewHolder detailViewHolder = new DetailViewHolder(requestDetail);

        detailViewHolder.itemView

        return detailViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder detailViewHolder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
