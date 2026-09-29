package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText username, password;
    private FirebaseAuth firebaseAuth;
    private TextView registerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();

        // Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Handle login button click
        loginButton.setOnClickListener(view -> loginUser());

        // Navigate to register page when user clicks "Sign Up"
        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, register_page.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth != null && firebaseAuth.getCurrentUser() != null) {
            String email = firebaseAuth.getCurrentUser().getEmail();
            if (email != null && email.equalsIgnoreCase("admin@example.com")) {
                startActivity(new Intent(MainActivity.this, AdminHomeActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, Home_page.class));
            }
            finish();
        }
    }

    private void initializeUI() {
        username = findViewById(R.id.user_input);
        password = findViewById(R.id.pass_input);
        loginButton = findViewById(R.id.button);
        registerTextView = findViewById(R.id.tv_signup);
    }

    private void loginUser() {
        String email = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            username.setError("Email is required!");
            username.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }

        // Firebase authentication
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                // Check if user is admin
                if (email.equals("admin@example.com")) {  // Change this to your admin email
                    startActivity(new Intent(MainActivity.this, AdminHomeActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, Home_page.class));
                }
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
