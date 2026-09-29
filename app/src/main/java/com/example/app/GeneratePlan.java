package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class GeneratePlan extends BaseActivity {

    EditText inputSquareFeet, inputBeds, inputBaths, inputGarages;
    Button searchButton;
    RecyclerView recyclerView;
    List<HousePlan> housePlans;
    HousePlanAdapter adapter;

    // Feature normalization boundaries computed dynamically from dataset
    private double minSqFt = Double.MAX_VALUE;
    private double maxSqFt = Double.MIN_VALUE;
    private double minBeds = Double.MAX_VALUE;
    private double maxBeds = Double.MIN_VALUE;
    private double minBaths = Double.MAX_VALUE;
    private double maxBaths = Double.MIN_VALUE;
    private double minGarages = Double.MAX_VALUE;
    private double maxGarages = Double.MIN_VALUE;

    // Domain-informed ML feature weights
    private static final double WEIGHT_BEDS = 0.35;
    private static final double WEIGHT_BATHS = 0.25;
    private static final double WEIGHT_SQFT = 0.25;
    private static final double WEIGHT_GARAGES = 0.15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_plan);

        // Initialize views
        inputSquareFeet = findViewById(R.id.input_square_feet);
        inputBeds = findViewById(R.id.input_beds);
        inputBaths = findViewById(R.id.input_baths);
        inputGarages = findViewById(R.id.input_garages);
        searchButton = findViewById(R.id.search_button);
        recyclerView = findViewById(R.id.recyclerView);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load and normalize house plans from CSV
        housePlans = loadCSVData();

        // Set click listener for the search button
        searchButton.setOnClickListener(v -> findUniqueHousePlans());
    }

    private List<HousePlan> loadCSVData() {
        List<HousePlan> plans = new ArrayList<>();
        Set<String> existingAssets = new HashSet<>();
        try {
            String[] list = getAssets().list("images");
            if (list != null) {
                for (String file : list) {
                    existingAssets.add(file.toLowerCase().trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStream is = getAssets().open("house_plans_details.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 5) {
                    int squareFeet = Integer.parseInt(values[0].trim());
                    int beds = Integer.parseInt(values[1].trim());
                    float baths = Float.parseFloat(values[2].trim());
                    int garages = Integer.parseInt(values[3].trim());
                    String imagePath = values[4].trim();
                    if (imagePath.startsWith("images/")) {
                        imagePath = imagePath.substring(7);
                    }
                    String cleanName = imagePath.toLowerCase().trim();

                    // Strict validation: Only include records whose images actually exist in assets
                    if (!existingAssets.isEmpty() && !existingAssets.contains(cleanName)) {
                        continue;
                    }

                    plans.add(new HousePlan(squareFeet, beds, baths, garages, imagePath));

                    // Track bounds for ML Feature Scaling
                    if (squareFeet < minSqFt) minSqFt = squareFeet;
                    if (squareFeet > maxSqFt) maxSqFt = squareFeet;
                    if (beds < minBeds) minBeds = beds;
                    if (beds > maxBeds) maxBeds = beds;
                    if (baths < minBaths) minBaths = baths;
                    if (baths > maxBaths) maxBaths = baths;
                    if (garages < minGarages) minGarages = garages;
                    if (garages > maxGarages) maxGarages = garages;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return plans;
    }

    private void findUniqueHousePlans() {
        try {
            String sqFtStr = inputSquareFeet.getText().toString().trim();
            String bedsStr = inputBeds.getText().toString().trim();
            String bathsStr = inputBaths.getText().toString().trim();
            String garagesStr = inputGarages.getText().toString().trim();

            if (sqFtStr.isEmpty() || bedsStr.isEmpty() || bathsStr.isEmpty() || garagesStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all home specifications", Toast.LENGTH_SHORT).show();
                return;
            }

            int squareFeet = Integer.parseInt(sqFtStr);
            int beds = Integer.parseInt(bedsStr);
            float baths = Float.parseFloat(bathsStr);
            int garages = Integer.parseInt(garagesStr);

            // Compute similarity scores for all candidate plans
            for (HousePlan plan : housePlans) {
                double distance = calculateDistance(plan, squareFeet, beds, baths, garages);
                // Convert distance to similarity percentage [0.0 - 100.0]
                double similarity = Math.max(0.0, Math.min(100.0, (1.0 - distance) * 100.0));
                plan.setMatchScore(similarity);
            }

            // Sort plans: highest similarity percentage first
            Collections.sort(housePlans, (p1, p2) -> Double.compare(p2.getMatchScore(), p1.getMatchScore()));

            // Extract top unique matching plans
            Set<String> seenImages = new HashSet<>();
            List<HousePlan> uniquePlans = new ArrayList<>();

            for (HousePlan plan : housePlans) {
                String imageName = extractImageName(plan.getImagePath());
                if (!seenImages.contains(imageName)) {
                    seenImages.add(imageName);
                    uniquePlans.add(plan);
                }
                if (uniquePlans.size() >= 5) { // Return top 5 ML matches
                    break;
                }
            }

            // Show results in RecyclerView
            if (uniquePlans.isEmpty()) {
                Toast.makeText(this, "No matching house plans found", Toast.LENGTH_SHORT).show();
            } else {
                adapter = new HousePlanAdapter(this, uniquePlans);
                recyclerView.setAdapter(adapter);
                Toast.makeText(this, "Found " + uniquePlans.size() + " optimal ML matches!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Invalid input. Please enter valid numeric values.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Feature-Normalized Weighted Euclidean k-NN Distance Metric.
     * Maps multi-scale features (SqFt, Beds, Baths, Garages) to normalized [0.0, 1.0] space.
     */
    private double calculateDistance(HousePlan plan, int targetSquareFeet, int targetBeds, float targetBaths, int targetGarages) {
        double normTargetSqFt = normalize(targetSquareFeet, minSqFt, maxSqFt);
        double normTargetBeds = normalize(targetBeds, minBeds, maxBeds);
        double normTargetBaths = normalize(targetBaths, minBaths, maxBaths);
        double normTargetGarages = normalize(targetGarages, minGarages, maxGarages);

        double normPlanSqFt = normalize(plan.getSquareFeet(), minSqFt, maxSqFt);
        double normPlanBeds = normalize(plan.getBeds(), minBeds, maxBeds);
        double normPlanBaths = normalize(plan.getBaths(), minBaths, maxBaths);
        double normPlanGarages = normalize(plan.getGarages(), minGarages, maxGarages);

        double distSq = WEIGHT_BEDS * Math.pow(normTargetBeds - normPlanBeds, 2)
                      + WEIGHT_BATHS * Math.pow(normTargetBaths - normPlanBaths, 2)
                      + WEIGHT_SQFT * Math.pow(normTargetSqFt - normPlanSqFt, 2)
                      + WEIGHT_GARAGES * Math.pow(normTargetGarages - normPlanGarages, 2);

        return Math.sqrt(distSq);
    }

    private double normalize(double val, double min, double max) {
        if (max <= min) return 0.0;
        return (val - min) / (max - min);
    }

    private String extractImageName(String imagePath) {
        return imagePath.substring(imagePath.lastIndexOf("/") + 1).toLowerCase().trim();
    }
}