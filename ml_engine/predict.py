#!/usr/bin/env python3
"""
ArcWell AI - Quick CLI Floor Plan Predictor
Usage:
    python predict.py
    python predict.py 1800 3 2 1
    python predict.py --sqft 2500 --beds 4 --baths 3 --garages 2
"""
import sys
import os

from ml_plan_generator import LightweightMLPlanRecommender

def run():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    csv_path = os.path.join(script_dir, "house_plans_details.csv")
    if not os.path.exists(csv_path):
        csv_path = os.path.join(script_dir, "..", "app", "src", "main", "assets", "house_plans_details.csv")
    
    recommender = LightweightMLPlanRecommender(csv_path)

    # If 4 positional arguments are passed: sqft beds baths garages
    if len(sys.argv) == 5:
        try:
            sqft = float(sys.argv[1])
            beds = float(sys.argv[2])
            baths = float(sys.argv[3])
            garages = float(sys.argv[4])
        except ValueError:
            print("Error: Arguments must be numbers: python predict.py <sqft> <beds> <baths> <garages>")
            sys.exit(1)
        
        results, exec_time = recommender.predict(sqft, beds, baths, garages, top_k=5)
        print(f"\n[ML Results] Top matches for {sqft:.0f} sqft, {beds:.0f} BHK, {baths:.1f} Bath, {garages:.0f} Garage (Computed in {exec_time:.2f} ms):")
        print("-" * 65)
        for i, res in enumerate(results, 1):
            p = res["plan"]
            print(f"#{i} Match: {res['match_score']:.1f}% | {p['beds']:.0f} Beds, {p['baths']:.1f} Baths, {p['sqft']:.0f} sq ft -> images/{p['image']}")
        print("-" * 65)
    else:
        # Fallback to standard CLI or interactive
        from ml_plan_generator import main
        main()

if __name__ == "__main__":
    run()
