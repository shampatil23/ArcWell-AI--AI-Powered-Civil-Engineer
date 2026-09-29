package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PlanViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_viewer);

        ImageView ivPlan = findViewById(R.id.iv_plan);
        View rulerOverlay = findViewById(R.id.ruler_overlay);
        TextView cityArea = findViewById(R.id.city_area_overlay);

        // Get extras from intent
        int drawableId = getIntent().getIntExtra("DRAWABLE_ID", 0);
        boolean showRuler = getIntent().getBooleanExtra("SHOW_RULER", false);
        boolean showCityArea = getIntent().getBooleanExtra("SHOW_CITY_AREA", false);

        // Set image
        ivPlan.setImageResource(drawableId);

        // Handle overlays
        rulerOverlay.setVisibility(showRuler ? View.VISIBLE : View.GONE);
        cityArea.setVisibility(showCityArea ? View.VISIBLE : View.GONE);

        // Add ruler drawing logic
        rulerOverlay.setBackground(new RulerDrawable());
    }

    private class RulerDrawable extends Drawable {
        private Paint paint = new Paint();

        public RulerDrawable() {
            paint.setColor(Color.BLACK);
            paint.setTextSize(24);
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            // Draw horizontal ruler
            for(int i=0; i<canvas.getWidth(); i+=100){
                canvas.drawLine(i, 0, i, 50, paint);
                canvas.drawText(i+"px", i, 80, paint);
            }

            // Draw vertical ruler
            for(int i=0; i<canvas.getHeight(); i+=100){
                canvas.drawLine(0, i, 50, i, paint);
                canvas.drawText(i+"px", 60, i+30, paint);
            }
        }

        @Override
        public void setAlpha(int alpha) {}
        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {}
        @Override
        public int getOpacity() { return PixelFormat.OPAQUE; }
    }
}