package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends BaseActivity {

    private TextView tvProfileName, tvProfileEmail, tvProfileBadge, tvDetailEmail, tvDetailUid;
    private MaterialButton btnLogout;
    private FirebaseAuth firebaseAuth;

    @Override
    protected int getCurrentNavItem() {
        return R.id.nav_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvProfileName  = findViewById(R.id.tv_profile_name);
        tvProfileEmail = findViewById(R.id.tv_profile_email);
        tvProfileBadge = findViewById(R.id.tv_profile_badge);
        tvDetailEmail  = findViewById(R.id.tv_detail_email);
        tvDetailUid    = findViewById(R.id.tv_detail_uid);
        btnLogout      = findViewById(R.id.btn_logout);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String email = currentUser.getEmail();
            String uid = currentUser.getUid();
            String displayName = currentUser.getDisplayName();

            if (displayName == null || displayName.trim().isEmpty()) {
                if (email != null && email.contains("@")) {
                    displayName = email.substring(0, email.indexOf("@"));
                } else {
                    displayName = "ArcWell User";
                }
            }

            tvProfileName.setText(displayName);
            tvProfileEmail.setText(email != null ? email : "No email registered");
            tvDetailEmail.setText(email != null ? email : "N/A");
            tvDetailUid.setText(uid);

            if (email != null && email.equalsIgnoreCase("admin@example.com")) {
                tvProfileBadge.setText("✦ Administrator");
            } else {
                tvProfileBadge.setText("✦ Active Member");
            }
        } else {
            tvProfileName.setText("Guest User");
            tvProfileEmail.setText("Not signed in");
            tvDetailEmail.setText("N/A");
            tvDetailUid.setText("N/A");
            tvProfileBadge.setText("Guest");
        }

        // Logout
        btnLogout.setOnClickListener(v -> {
            firebaseAuth.signOut();
            Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
