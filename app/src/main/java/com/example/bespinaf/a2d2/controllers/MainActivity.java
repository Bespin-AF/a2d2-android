package com.example.bespinaf.a2d2.controllers;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.utilities.ActivityUtils;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;
import com.example.bespinaf.a2d2.utilities.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends ButterKnifeActivity {

    @BindView(R.id.button_navigate_to_rules)
    MaterialButton buttonNavigateToRules;
    @BindView(R.id.button_main_driver_login)
    MaterialButton buttonMainDriverLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_main);

        //Initialize application resources
        FormatUtils.initializeDateFormatters();
        DataSourceUtils.loadA2D2_PhoneNumber(null);
        DataSourceUtils.loadA2D2_BaseLocation(null);
    }


    @OnClick(R.id.button_navigate_to_rules)
    public void navigateToRulesPage(View view) {
        ActivityUtils.navigate(this, Rules.class);
    }


    @OnClick(R.id.button_main_driver_login)
    public void navigateToDriverLogin(View view) { ActivityUtils.navigate(this, DriverLogin.class); }
}
