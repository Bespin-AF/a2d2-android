package com.example.bespinaf.a2d2.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.bespinaf.a2d2.R;

public class NetworkUtils {
    public static boolean checkInternetConnectivity(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return true;
        }
        else return false;
    }
    public static void displayNetworkError(Context context){
        Toast.makeText(
                context,
                R.string.connection_error,
                Toast.LENGTH_SHORT
        ).show();
    }

}