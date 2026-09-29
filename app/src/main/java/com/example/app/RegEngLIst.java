package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegEngLIst extends AppCompatActivity {
    private RecyclerView recyclerViewEngineers;
    private EngineerAdapter adapter;
    private List<EngineerModel> engineerList;
    private DatabaseReference engineersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reglist);

        recyclerViewEngineers = findViewById(R.id.recyclerViewEngineers);
        recyclerViewEngineers.setLayoutManager(new LinearLayoutManager(this));
        engineerList = new ArrayList<>();
        adapter = new EngineerAdapter(this, engineerList);
        recyclerViewEngineers.setAdapter(adapter);

        // Initialize Firebase reference to "engineers" node
        engineersRef = FirebaseDatabase.getInstance().getReference("reg");

        // Listen for real-time updates
        engineersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                EngineerModel engineer = snapshot.getValue(EngineerModel.class);
                if (engineer != null) {
                    engineerList.add(engineer);
                    adapter.notifyItemInserted(engineerList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                // Implement if you need to update changes
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Implement removal logic if needed
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Adding two options: Register Engineer and Admin Panel
        menu.add("Register Engineer");
        menu.add("Admin Panel");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = item.getTitle().toString();
        if ("Register Engineer".equals(title)) {
            startActivity(new Intent(this, RegisterEngineerActivity.class));
            return true;
        } else if ("Admin Panel".equals(title)) {
            startActivity(new Intent(this, AdminPanelActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
