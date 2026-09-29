package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.ai.client.generativeai.common.RequestOptions;


public class SplashActivity extends AppCompatActivity {

    public static String TAG= SplashActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash);


        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        ImageView splash = findViewById(R.id.splash_iv);


        Glide.with(this)
                .load(R.drawable.gif_splash)

                .into(splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                com.google.firebase.auth.FirebaseUser currentUser =
                        com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {
                    String email = currentUser.getEmail();
                    if (email != null && email.equalsIgnoreCase("admin@example.com")) {
                        startActivity(new Intent(SplashActivity.this, AdminHomeActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, Home_page.class));
                    }
                } else {
                    final Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                }

                overridePendingTransition(R.anim.fade_in, 0);
                SplashActivity.this.finish();
            }
        }, 3000);


}

        }




