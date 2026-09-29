package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

public class ImageDetailActivity extends AppCompatActivity {

    private ImageView fullImageView;
    private Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        // Initialize views
        fullImageView = findViewById(R.id.full_image_view);
        closeButton = findViewById(R.id.close_button);

        // Get image path from intent
        String imagePath = getIntent().getStringExtra("imagePath");

        if (imagePath != null) {
            // Load image from assets
            loadImageFromAssets(imagePath);
        } else {
            // Show error message if image path is not provided
            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if no image path is provided
        }

        // Set click listener for the close button
        closeButton.setOnClickListener(v -> finish());
    }

    // Load image from assets
    private void loadImageFromAssets(String imagePath) {
        AssetManager assetManager = getAssets();
        try (InputStream inputStream = assetManager.open("images/" + imagePath)) {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap != null) {
                fullImageView.setImageBitmap(bitmap);
            } else {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity if the image cannot be loaded
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if an exception occurs
        }
    }
}