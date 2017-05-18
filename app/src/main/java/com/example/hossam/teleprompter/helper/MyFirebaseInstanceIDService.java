package com.example.hossam.teleprompter.helper;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Mina William on 5/18/17.
 * Xdigital Group company
 */

public class MyFirebaseInstanceIDService  extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d("****", "Refreshed token: " + refreshedToken);
    }
}


