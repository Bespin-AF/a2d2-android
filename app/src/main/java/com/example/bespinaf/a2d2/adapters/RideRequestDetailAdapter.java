package com.example.bespinaf.a2d2.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bespinaf.a2d2.R;

import java.util.HashMap;
import java.util.List;

public class RideRequestDetailAdapter extends RecyclerView.Adapter<RideRequestDetailAdapter.DetailViewHolder>{

    public class DetailViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView value;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.request_detail_title);
            value = itemView.findViewById(R.id.request_detail_value);
        }
    }

    //First value is the detail's title, Second value is the detail's value
    List<Pair<String, String>> details;

    public RideRequestDetailAdapter(List<Pair<String, String>> adapterDetails) {
        details = adapterDetails;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View requestDetail = inflater.inflate(R.layout.card_ride_request_detail, viewGroup, false);
        DetailViewHolder detailViewHolder = new DetailViewHolder(requestDetail);

        return detailViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder detailViewHolder, int position) {
        Pair<String, String> detail = details.get(position);
        detailViewHolder.title.setText(detail.first + ":");
        detailViewHolder.value.setText(detail.second);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

}
