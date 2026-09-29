package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DraftPlansActivity extends BaseActivity {
    private ListView listView;
    private List<String> draftPlans;
    private File draftsDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_plans);

        listView = findViewById(R.id.list_view);
        draftsDirectory = getFilesDir();

        loadDraftPlans();
    }

    private void loadDraftPlans() {
        draftPlans = new ArrayList<>();
        File[] files = draftsDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith("_draft.png")) {
                    draftPlans.add(file.getName());
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, draftPlans);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedFile = draftPlans.get(position);
            Intent intent = new Intent(DraftPlansActivity.this, ManualPlanActivity.class);
            intent.putExtra("file_name", selectedFile);
            startActivity(intent);
        });
    }
}
