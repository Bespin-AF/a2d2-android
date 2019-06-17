package com.example.bespinaf.a2d2.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.bespinaf.a2d2.LoginCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthorizationUtils {
    private static FirebaseAuth firebaseAuthorization = FirebaseAuth.getInstance();
    private static FirebaseUser user = null;
    private static LoginCallback loginCallback = null;

    private static OnCompleteListener loginCompleteListener = (task) -> {
        if(loginCallback != null){
            runLoginCallback(task.isSuccessful());
        }
    };


    public static void authorizeUser(String email, String password, LoginCallback callback){
        loginCallback = callback;
        firebaseAuthorization.signInWithEmailAndPassword(email, password).addOnCompleteListener(loginCompleteListener);
    }


    private static void runLoginCallback(boolean wasLoginSuccessful){
        loginCallback.callback(wasLoginSuccessful);
    }


    public static FirebaseUser getCurrentUser(){
        return firebaseAuthorization.getCurrentUser();
    }
}
