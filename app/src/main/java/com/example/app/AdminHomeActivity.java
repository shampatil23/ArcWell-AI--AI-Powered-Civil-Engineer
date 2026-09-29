package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHomeActivity extends AppCompatActivity {
    private Button logoutButton, viewEngineersButton, registerEngineerButton,apibtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logoutButton = findViewById(R.id.btn_logout);
        viewEngineersButton = findViewById(R.id.btn_view_engineers);
        registerEngineerButton = findViewById(R.id.btn_register_engineer);

        // Logout button
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AdminHomeActivity.this, MainActivity.class));
            finish();
        });
apibtn=findViewById(R.id.api);
apibtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i=new Intent(AdminHomeActivity.this,ApiKeyActivity.class);
        startActivity(i);
    }
});
        // View engineers button
        viewEngineersButton.setOnClickListener(v -> {
            startActivity(new Intent(AdminHomeActivity.this,RegEngLIst.class));
        });

        // Register engineer button
        registerEngineerButton.setOnClickListener(v -> {
            startActivity(new Intent(AdminHomeActivity.this, AdminPanelActivity.class));
        });
    }
}
