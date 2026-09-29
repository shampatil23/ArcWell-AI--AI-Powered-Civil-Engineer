package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MaterialEstimationActivity extends BaseActivity {

    @Override
    protected int getCurrentNavItem() {
        return R.id.nav_estimate;
    }

    // Multipliers for quantity estimation
    private static final double CEMENT_MULTIPLIER = 0.43;  // kg of cement per sq.ft
    private static final double IRON_MULTIPLIER = 2.5;     // kg of iron per sq.ft
    private static final double SAND_MULTIPLIER = 1.25;     // cubic feet of sand per sq.ft
    private static final double BRICKS_MULTIPLIER = 18;     // number of bricks per sq.ft
    private static final double TILES_MULTIPLIER = 1.1;      // number of floor tiles per sq.ft (10% extra for wastage)

    // Unit prices in INR (approximate current market rates)
    private static final double CEMENT_UNIT_PRICE = 7;   // ₹ per kg (approx. ₹350 per 50kg bag)
    private static final double IRON_UNIT_PRICE = 55;    // ₹ per kg
    private static final double SAND_UNIT_PRICE = 50;    // ₹ per cubic foot
    private static final double BRICK_UNIT_PRICE = 7;    // ₹ per brick
    private static final double TILE_UNIT_PRICE = 70;    // ₹ per tile (assuming 1 sq.ft coverage)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_estimation);

        final EditText editPlotSize = findViewById(R.id.editPlotSize);
        Button btnCalculate = findViewById(R.id.btnCalculate);
        final CardView cardResults = findViewById(R.id.cardResults);

        final TextView tvCementQuantity = findViewById(R.id.tvCementQuantity);
        final TextView tvCementCost = findViewById(R.id.tvCementCost);
        final TextView tvIronQuantity = findViewById(R.id.tvIronQuantity);
        final TextView tvIronCost = findViewById(R.id.tvIronCost);
        final TextView tvSandQuantity = findViewById(R.id.tvSandQuantity);
        final TextView tvSandCost = findViewById(R.id.tvSandCost);
        final TextView tvBricksQuantity = findViewById(R.id.tvBricksQuantity);
        final TextView tvBricksCost = findViewById(R.id.tvBricksCost);
        final TextView tvTilesQuantity = findViewById(R.id.tvTilesQuantity);
        final TextView tvTilesCost = findViewById(R.id.tvTilesCost);
        final TextView tvTotalBudget = findViewById(R.id.tvTotalBudget);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String plotSizeStr = editPlotSize.getText().toString();
                if (plotSizeStr.isEmpty()) {
                    Toast.makeText(MaterialEstimationActivity.this, "Please enter a plot size", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    double plotSize = Double.parseDouble(plotSizeStr);

                    // Calculate quantities using the provided multipliers
                    double cementQuantity = plotSize * CEMENT_MULTIPLIER;   // in kg
                    double ironQuantity = plotSize * IRON_MULTIPLIER;         // in kg
                    double sandQuantity = plotSize * SAND_MULTIPLIER;         // in cubic feet
                    double bricksQuantity = plotSize * BRICKS_MULTIPLIER;     // number of bricks
                    double tilesQuantity = plotSize * TILES_MULTIPLIER;       // number of floor tiles

                    // Calculate costs
                    double cementCost = cementQuantity * CEMENT_UNIT_PRICE;
                    double ironCost = ironQuantity * IRON_UNIT_PRICE;
                    double sandCost = sandQuantity * SAND_UNIT_PRICE;
                    double bricksCost = bricksQuantity * BRICK_UNIT_PRICE;
                    double tilesCost = tilesQuantity * TILE_UNIT_PRICE;

                    // Total estimated budget (cost)
                    double totalBudget = cementCost + ironCost + sandCost + bricksCost + tilesCost;

                    // Display the results with proper units
                    tvCementQuantity.setText(String.format("%.2f kg", cementQuantity));
                    tvCementCost.setText(String.format("₹%.2f", cementCost));

                    tvIronQuantity.setText(String.format("%.2f kg", ironQuantity));
                    tvIronCost.setText(String.format("₹%.2f", ironCost));

                    tvSandQuantity.setText(String.format("%.2f cu.ft", sandQuantity));
                    tvSandCost.setText(String.format("₹%.2f", sandCost));

                    tvBricksQuantity.setText(String.format("%.0f bricks", bricksQuantity));
                    tvBricksCost.setText(String.format("₹%.2f", bricksCost));

                    tvTilesQuantity.setText(String.format("%.0f tiles", tilesQuantity));
                    tvTilesCost.setText(String.format("₹%.2f", tilesCost));

                    tvTotalBudget.setText(String.format("₹%.2f", totalBudget));

                    // Show the results card
                    cardResults.setVisibility(View.VISIBLE);

                } catch (NumberFormatException e) {
                    Toast.makeText(MaterialEstimationActivity.this, "Invalid plot size", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
