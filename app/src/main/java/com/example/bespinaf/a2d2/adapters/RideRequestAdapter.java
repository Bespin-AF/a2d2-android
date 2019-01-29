package com.example.bespinaf.a2d2.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.bespinaf.a2d2.utilities.Request;

import java.util.List;

public class RideRequestAdapter extends ArrayAdapter<Request> {
    public RideRequestAdapter(Context context, int resource, List<Request> objects) {
        super(context, resource, objects);
    }
}