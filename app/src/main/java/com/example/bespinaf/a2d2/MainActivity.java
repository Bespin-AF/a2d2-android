package com.example.bespinaf.a2d2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * //Open the rules page
     * @param view the Request Ride Navigation Button
     */
    public void openRules(View view) {
        Intent intent = new Intent(this, Rules.class);
        startActivity(intent);
    }
}
