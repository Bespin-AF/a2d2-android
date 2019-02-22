package com.example.bespinaf.a2d2.controllers;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public class ButterKnifeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void bind(int layoutID){
        setContentView(layoutID);
        ButterKnife.bind(this);
    }
}