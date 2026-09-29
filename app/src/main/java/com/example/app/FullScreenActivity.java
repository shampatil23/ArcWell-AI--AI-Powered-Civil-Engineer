package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class FullScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        TouchImageView fullScreenImage = findViewById(R.id.fullScreenImage);

        // Get the image resource ID from intent
        int imageResId = getIntent().getIntExtra("image", 0);

        // Set the image to TouchImageView
        fullScreenImage.setImageResource(imageResId);
    }
}
