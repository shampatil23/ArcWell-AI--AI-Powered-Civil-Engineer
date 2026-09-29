package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EngineerDetailActivity extends AppCompatActivity {
    private ImageView engineerImage;
    private TextView engineerName, engineerDescription, engineerEmail, engineerSkills, engineerExperience;
    private Button contactButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineer_detail);

        engineerImage = findViewById(R.id.detail_engineer_image);
        engineerName = findViewById(R.id.detail_engineer_name);
        engineerDescription = findViewById(R.id.detail_engineer_description);
        engineerEmail = findViewById(R.id.detail_engineer_email);
        engineerSkills = findViewById(R.id.detail_engineer_skills);
        engineerExperience = findViewById(R.id.detail_engineer_experience);
        contactButton = findViewById(R.id.btn_contact);

        // Get engineer data from the intent
        EngineerModel engineer = (EngineerModel) getIntent().getSerializableExtra("engineer");
        if (engineer != null) {

            engineerName.setText(engineer.getName());
            engineerDescription.setText(engineer.getShortDescription());
            engineerEmail.setText(engineer.getEmail());
            engineerSkills.setText(engineer.getSkills());
            engineerExperience.setText(engineer.getExperience());
        }

        // Open email client when "Contact" is clicked
        contactButton.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                    Uri.fromParts("mailto", engineer.getEmail(), null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Inquiry");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        });
    }
}
