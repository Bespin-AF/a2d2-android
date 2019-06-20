package af.bespin.a2d2.controllers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.view.View;
import android.widget.Toast;

import af.bespin.a2d2.R;
import af.bespin.a2d2.models.DataSource;
import af.bespin.a2d2.models.DataSourceType;
import af.bespin.a2d2.utilities.ActivityUtils;
import af.bespin.a2d2.utilities.DataSourceUtils;
import af.bespin.a2d2.utilities.FormatUtils;

import af.bespin.a2d2.utilities.NetworkUtils;
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
        if (NetworkUtils.checkInternetConnectivity(getApplicationContext())) {
            ActivityUtils.navigate(this, Rider_Rules.class);
        }
        else {
            NetworkUtils.displayNetworkError(this);
        }
    }


    @OnClick(R.id.button_main_driver_login)
    public void navigateToDriverLogin(View view) {
        if (NetworkUtils.checkInternetConnectivity(getApplicationContext())) {
            ActivityUtils.navigate(this, Driver_Login.class);
        }
        else {
            NetworkUtils.displayNetworkError(this);
        }
    }
}
