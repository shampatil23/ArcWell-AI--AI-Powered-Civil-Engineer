package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;

public class ManualPlanActivity extends BaseActivity {

    private static final String TAG = "ManualPlanActivity";
    private DrawingCanvas drawingCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_plan);

        setupToolbar();
        initializeCanvas();
        setupControls();
        loadPlanFromIntent();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initializeCanvas() {
        try {
            drawingCanvas = findViewById(R.id.drawing_canvas);
            if (drawingCanvas == null) throw new NullPointerException("DrawingCanvas not found in layout");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing canvas: " + e.getMessage(), e);
            showToast("Error: Unable to initialize canvas");
            finish();
        }
    }

    private void loadPlanFromIntent() {
        String planPath = getIntent().getStringExtra("plan_path");
        if (planPath != null) {
            loadSavedPlan(planPath);
        }
    }

    private void loadSavedPlan(String path) {
        File file = new File(path);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            drawingCanvas.setBackgroundBitmap(bitmap);
            showToast("Loaded saved plan successfully!");
        } else {
            showToast("Failed to load saved plan.");
        }
    }

    private void setupControls() {
        try {
            setupButton(R.id.btn_line, () -> drawingCanvas.setTool("line"));
            setupButton(R.id.btn_rectangle, () -> drawingCanvas.setTool("rectangle"));
            setupButton(R.id.btn_circle, () -> drawingCanvas.setTool("circle"));
            setupButton(R.id.btn_undo, () -> drawingCanvas.undo());
            setupButton(R.id.btn_clear, () -> drawingCanvas.clearCanvas());
            setupButton(R.id.btn_save, this::promptForFileName);
            setupButton(R.id.btn_download, this::downloadPlan);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up controls: " + e.getMessage(), e);
            showToast("Failed to initialize controls");
        }
    }

    private void setupButton(int buttonId, Runnable action) {
        Button button = findViewById(buttonId);
        if (button != null) button.setOnClickListener(v -> action.run());
    }

    private void promptForFileName() {
        final EditText input = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Plan Name")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {
                    String fileName = input.getText().toString().trim();
                    if (!fileName.isEmpty()) {
                        savePlanWithFileName(fileName + ".png");
                    } else {
                        showToast("File name can't be empty!");
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }

    private void savePlanWithFileName(String fileName) {
        try {
            drawingCanvas.savePlanWithName(fileName);
            showToast("Plan saved successfully as " + fileName);
        } catch (Exception e) {
            Log.e(TAG, "Error saving plan: " + e.getMessage(), e);
            showToast("Failed to save plan.");
        }
    }

    private void downloadPlan() {
        try {
            drawingCanvas.downloadPlan();
            showToast("Plan downloaded successfully!");
        } catch (Exception e) {
            Log.e(TAG, "Error downloading plan: " + e.getMessage(), e);
            showToast("Failed to download plan.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Inflate the menu for toolbar options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawing_menu, menu);
        return true;
    }

    // Handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_saved_plans) {
            startActivity(new Intent(this, SavedPlansActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
