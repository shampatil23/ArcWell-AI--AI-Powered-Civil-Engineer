package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class PlanDetailsActivity extends AppCompatActivity {

    private ImageView[] planImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);

        // Initialize ImageView array
        planImages = new ImageView[]{
                findViewById(R.id.planImage1),
                findViewById(R.id.planImage2),
                findViewById(R.id.planImage3),
                findViewById(R.id.planImage4),
                findViewById(R.id.planImage5)
        };

        // Retrieve images from intent
        Intent intent = getIntent();
        if (intent != null) {
            int[] imageArray = intent.getIntArrayExtra("imageArray");
            String planName = intent.getStringExtra("name");

            if (planName != null) {
                setTitle(planName);  // Set Activity title
            }

            if (imageArray != null) {
                for (int i = 0; i < Math.min(imageArray.length, planImages.length); i++) {
                    planImages[i].setImageResource(imageArray[i]);

                    // Click listener to open image in fullscreen
                    final int imageRes = imageArray[i];
                    planImages[i].setOnClickListener(v -> openFullScreen(imageRes));
                }
            }
        }
    }

    private void openFullScreen(int imageRes) {
        Intent intent = new Intent(this, FullScreenActivity.class);
        intent.putExtra("image", imageRes);
        startActivity(intent);
    }
}
