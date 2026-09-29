package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SavedPlansActivity extends BaseActivity {
    private ListView listView;
    private List<String> savedPlansList = new ArrayList<>();
    private List<String> savedPlanPaths = new ArrayList<>();  // Store full paths internally

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_plans);

        listView = findViewById(R.id.list_saved_plans);

        // Load saved plans from the Pictures directory
        loadSavedPlans();

        if (savedPlansList.isEmpty()) {
            Toast.makeText(this, "No saved plans found!", Toast.LENGTH_SHORT).show();
        } else {
            // Show only the file names (no paths)
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    savedPlansList
            );
            listView.setAdapter(adapter);

            // Click to load the corresponding plan in ManualPlanActivity
            listView.setOnItemClickListener((parent, view, position, id) -> {
                String selectedPlanPath = savedPlanPaths.get(position);
                Intent intent = new Intent(this, ManualPlanActivity.class);
                intent.putExtra("plan_path", selectedPlanPath);
                startActivity(intent);
            });
        }
    }

    private void loadSavedPlans() {
        File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File[] files = picturesDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".png")) { // Only list PNG plans
                    savedPlansList.add(file.getName()); // Show only the file name
                    savedPlanPaths.add(file.getAbsolutePath()); // Keep full path hidden internally
                }
            }
        }
    }
}
