# ArcWell AI - Lightweight Machine Learning Floor Plan Engine

## 🧠 Architecture Overview
This directory houses the standalone Machine Learning engine powering ArcWell AI's floor plan matching and recommendation system.

The algorithm uses **Feature-Normalized Weighted k-Nearest Neighbors (k-NN)** in a 4-dimensional hypercube:
- **Square Footage ($x_1$)**: Weight = 0.25
- **Bedrooms ($x_2$)**: Weight = 0.35 (Highest user priority)
- **Bathrooms ($x_3$)**: Weight = 0.25
- **Garages ($x_4$)**: Weight = 0.15

## ⚡ Intel Core i3 Optimization
- **Zero Heavy Dependencies**: Uses standard Python 3 `math` and `csv` modules (no CUDA, PyTorch, or TensorFlow required).
- **Execution Speed**: **< 5 milliseconds** across 2,640+ candidate floor plans.
- **Memory Footprint**: **< 15 MB RAM**, preventing CPU bottlenecks or laptop heating.

## 🚀 Usage Instructions
### Interactive Mode:
```bash
python ml_plan_generator.py
```

### Direct CLI Inference:
```bash
python predict.py 1800 3 2 1
```
*(Arguments: `<SquareFeet>` `<Bedrooms>` `<Bathrooms>` `<Garages>`)*

### Custom Parametric Query:
```bash
python ml_plan_generator.py --sqft 2200 --beds 3 --baths 2.5 --garages 2 --k 5
```
