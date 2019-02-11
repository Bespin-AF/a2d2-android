package com.example.bespinaf.a2d2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button_navigate_to_rules)
    MaterialButton buttonNavigateToRules;
    @BindView(R.id.button_main_driver_login)
    MaterialButton buttonMainDriverLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    /**
     * //Open the rules page
     *
     * @param view the Request Ride Navigation Button
     */
    @OnClick(R.id.button_navigate_to_rules)
    public void openRules(View view) {
        //TODO: nav to rules
        Intent intent = new Intent(this, RideRequests.class);
        startActivity(intent);
    }

    /**
     * //Open the driver login page
     *
     * @param view the Request Ride Navigation Button
     */
    @OnClick(R.id.button_main_driver_login)
    public void openDriverLogin(View view) {
        Intent intent = new Intent(this, DriverLogin.class);
        startActivity(intent);
    }
}
