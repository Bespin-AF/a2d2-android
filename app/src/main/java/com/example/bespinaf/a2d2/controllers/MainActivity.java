package com.example.bespinaf.a2d2.controllers;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.view.View;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.models.DataSource;
import com.example.bespinaf.a2d2.models.DataSourceType;
import com.example.bespinaf.a2d2.utilities.ActivityUtils;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;
import com.example.bespinaf.a2d2.utilities.FormatUtils;

import butterknife.BindView;
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
    }


    @OnClick(R.id.button_navigate_to_rules)
    public void navigateToRulesPage(View view) {
        ActivityUtils.navigate(this, Rider_Rules.class);
    }


    @OnClick(R.id.button_main_driver_login)
    public void navigateToDriverLogin(View view) { ActivityUtils.navigate(this, Driver_Login.class); }
}
