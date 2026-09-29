package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterEngineerActivity extends AppCompatActivity {
    private EditText etName, etShortDescription, etEmail, etSkills, etExperience;
    private Button btnRegister;
    private ProgressBar progressBar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_engineer);

        etName = findViewById(R.id.etName);
        etShortDescription = findViewById(R.id.etShortDescription);
        etEmail = findViewById(R.id.etEmail);
        etSkills = findViewById(R.id.etSkills);
        etExperience = findViewById(R.id.etExperience);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance();

        btnRegister.setOnClickListener(v -> registerEngineer());
    }

    private void registerEngineer() {
        String name = etName.getText().toString().trim();
        String shortDescription = etShortDescription.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String skills = etSkills.getText().toString().trim();
        String experience = etExperience.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(shortDescription) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Name, Description, and Email are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isInternetAvailable()) {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);

        // Generate a unique ID for the engineer
        String engineerId = UUID.randomUUID().toString();

        // Prepare data for Firebase Firestore
        Map<String, Object> engineer = new HashMap<>();
        engineer.put("id", engineerId);
        engineer.put("name", name);
        engineer.put("description", shortDescription);
        engineer.put("email", email);
        engineer.put("skills", skills);
        engineer.put("experience", experience);

        // Use .set() to avoid duplicate entries
        db.collection("engineers").document(engineerId)
                .set(engineer)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(RegisterEngineerActivity.this, "Engineer Registered Successfully", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    finish(); // Close the activity after successful submission
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterEngineerActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    btnRegister.setEnabled(true);
                });
    }

    // Check Internet Connection
    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
