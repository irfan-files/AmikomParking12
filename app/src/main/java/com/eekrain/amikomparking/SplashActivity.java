package com.eekrain.amikomparking;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    String nim, nama;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sessionManager = new SessionManager(this);


        HashMap<String, String> user = sessionManager.getUserDetail();
        nim = user.get(SessionManager.NIM);
        nama = user.get(SessionManager.NAME);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        boolean delayed = new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent homeIntent = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(homeIntent);
//                finish();
                sessionManager.checkLogin();
            }
        }, SPLASH_TIME_OUT);
    }
}
