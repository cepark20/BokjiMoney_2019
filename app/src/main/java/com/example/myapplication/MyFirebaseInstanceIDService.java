package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public MyFirebaseInstanceIDService() {
    }

    //새로운 토큰 확인 시 호출
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("onTokenRefresh","token: " + token);
        saveToken(token);
    }

    private void saveToken(String token){
        Log.d("토큰값",token);
        pref=getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("token",token);
        editor.apply();
    }
}
