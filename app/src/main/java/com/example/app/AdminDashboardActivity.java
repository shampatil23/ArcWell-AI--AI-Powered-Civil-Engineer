package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.*;
import java.util.ArrayList;
import java.util.List;

public class   AdminDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EngineerListAdapter adapter;
    private List<Engineer> engineerList





































































            




































            ;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns in Grid

        engineerList = new ArrayList<>();
        adapter = new EngineerListAdapter(engineerList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Fetch Data from Firestore
        fetchEngineers();
    }

    private void fetchEngineers() {
        db.collection("engineers").addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore Error", "Error fetching data: " + error.getMessage());
                return;
            }

            if (value == null || value.isEmpty()) {
                Log.w("Firestore", "No data found in 'engineers' collection.");
                return;
            }

            List<Engineer> newList = new ArrayList<>();
            for (DocumentSnapshot doc : value.getDocuments()) {
                Engineer engineer = doc.toObject(Engineer.class);
                if (engineer != null) {
                    newList.add(engineer);
                }
            }

            // Update RecyclerView safely
            adapter.setData(newList);
        });
    }
}
