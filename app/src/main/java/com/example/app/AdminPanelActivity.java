package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminPanelActivity extends AppCompatActivity {
    private EditText etOptionTitle, etOptionDescription,etskill,etphone,etexp,etemail;
    private Button btnAddOption;
    private DatabaseReference engineersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        etskill=findViewById(R.id.skill);
        etphone=findViewById(R.id.number);
        etexp=findViewById(R.id.exp);
        etemail=findViewById(R.id.email);

        etOptionTitle = findViewById(R.id.etOptionTitle);
        etOptionDescription = findViewById(R.id.etOptionDescription);
        btnAddOption = findViewById(R.id.btnAddOption);


        // Initialize Firebase reference
        engineersRef = FirebaseDatabase.getInstance().getReference("engineers");

        btnAddOption.setOnClickListener(v -> {
            String title = etOptionTitle.getText().toString().trim();
            String description = etOptionDescription.getText().toString().trim();
            String E=etemail.getText().toString().trim();
            String EE=etexp.getText().toString().trim();
            String s=etskill.getText().toString().trim();
            String p=etphone.getText().toString().trim();

            if (title.isEmpty() || description.isEmpty()|| p.isEmpty()|| s.isEmpty()|| EE.isEmpty()|| E.isEmpty()) {
                Toast.makeText(AdminPanelActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Use a default image for admin‑added option (ensure drawable exists)
            int defaultImage = R.drawable.enggg;
            // For admin-added options, you may leave email, skills, experience empty or set default values.
            EngineerModel newOption = new EngineerModel(defaultImage, title, description, E, s, p);

            String key = engineersRef.push().getKey();
            if (key != null) {
                engineersRef.child(key).setValue(newOption)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(AdminPanelActivity.this, "Engineer added successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AdminPanelActivity.this, "Error in adding an Engineer", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}