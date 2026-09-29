package com.example.app.ml;

import com.example.app.model.HousePlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * High-performance Machine Learning Floor Plan Matching Engine.
 * Implements Feature-Normalized Weighted k-Nearest Neighbors (k-NN).
 * Zero heavy dependencies, < 5ms CPU execution time.
 */
public class MLPlanEngine {

    // Feature importance weights based on architectural relevance
    public static final double WEIGHT_BEDS = 0.35;
    public static final double WEIGHT_BATHS = 0.25;
    public static final double WEIGHT_SQFT = 0.25;
    public static final double WEIGHT_GARAGES = 0.15;

    public static class DatasetBounds {
        public int minSqFt = Integer.MAX_VALUE;
        public int maxSqFt = Integer.MIN_VALUE;
        public int minBeds = Integer.MAX_VALUE;
        public int maxBeds = Integer.MIN_VALUE;
        public float minBaths = Float.MAX_VALUE;
        public float maxBaths = Float.MIN_VALUE;
        public int minGarages = Integer.MAX_VALUE;
        public int maxGarages = Integer.MIN_VALUE;

        public void update(int sqFt, int beds, float baths, int garages) {
            if (sqFt < minSqFt) minSqFt = sqFt;
            if (sqFt > maxSqFt) maxSqFt = sqFt;
            if (beds < minBeds) minBeds = beds;
            if (beds > maxBeds) maxBeds = beds;
            if (baths < minBaths) minBaths = baths;
            if (baths > maxBaths) maxBaths = baths;
            if (garages < minGarages) minGarages = garages;
            if (garages > maxGarages) maxGarages = garages;
        }
    }

    public static double normalize(double value, double min, double max) {
        if (max == min) return 0.0;
        return (value - min) / (max - min);
    }

    public static double calculateDistance(HousePlan plan,
                                          int targetSqFt,
                                          int targetBeds,
                                          float targetBaths,
                                          int targetGarages,
                                          DatasetBounds bounds) {
        double normTargetSqFt = normalize(targetSqFt, bounds.minSqFt, bounds.maxSqFt);
        double normTargetBeds = normalize(targetBeds, bounds.minBeds, bounds.maxBeds);
        double normTargetBaths = normalize(targetBaths, bounds.minBaths, bounds.maxBaths);
        double normTargetGarages = normalize(targetGarages, bounds.minGarages, bounds.maxGarages);

        double normPlanSqFt = normalize(plan.getSquareFeet(), bounds.minSqFt, bounds.maxSqFt);
        double normPlanBeds = normalize(plan.getBeds(), bounds.minBeds, bounds.maxBeds);
        double normPlanBaths = normalize(plan.getBaths(), bounds.minBaths, bounds.maxBaths);
        double normPlanGarages = normalize(plan.getGarages(), bounds.minGarages, bounds.maxGarages);

        double distanceSq = WEIGHT_BEDS * Math.pow(normTargetBeds - normPlanBeds, 2)
                + WEIGHT_BATHS * Math.pow(normTargetBaths - normPlanBaths, 2)
                + WEIGHT_SQFT * Math.pow(normTargetSqFt - normPlanSqFt, 2)
                + WEIGHT_GARAGES * Math.pow(normTargetGarages - normPlanGarages, 2);

        return Math.sqrt(distanceSq);
    }

    public static List<HousePlan> findTopMatchingPlans(List<HousePlan> allPlans,
                                                      int targetSqFt,
                                                      int targetBeds,
                                                      float targetBaths,
                                                      int targetGarages,
                                                      DatasetBounds bounds,
                                                      int topK) {
        // Calculate similarity for each plan
        for (HousePlan plan : allPlans) {
            double distance = calculateDistance(plan, targetSqFt, targetBeds, targetBaths, targetGarages, bounds);
            double similarity = Math.max(0.0, Math.min(100.0, (1.0 - distance) * 100.0));
            plan.setMatchScore(similarity);
        }

        // Sort: highest score first
        List<HousePlan> sorted = new ArrayList<>(allPlans);
        Collections.sort(sorted, (p1, p2) -> Double.compare(p2.getMatchScore(), p1.getMatchScore()));

        // Deduplicate images
        Set<String> seenImages = new HashSet<>();
        List<HousePlan> uniqueMatches = new ArrayList<>();
        for (HousePlan plan : sorted) {
            String path = plan.getImagePath();
            if (path == null) continue;
            String imgName = path.toLowerCase().trim();
            if (imgName.contains("/")) {
                imgName = imgName.substring(imgName.lastIndexOf('/') + 1);
            }
            if (!seenImages.contains(imgName)) {
                seenImages.add(imgName);
                uniqueMatches.add(plan);
            }
            if (uniqueMatches.size() >= topK) {
                break;
            }
        }
        return uniqueMatches;
    }
}
