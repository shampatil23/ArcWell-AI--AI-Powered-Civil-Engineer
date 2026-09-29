package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class Home_page extends BaseActivity {
    private Toolbar toolbar;
    private ViewPager2 imageSlider;
    private Handler sliderHandler;
    private Runnable sliderRunnable;
    // Changed from Button to View to support the premium CardView design
    private View btnGenerate, btnGenerateWithAI, btnlocl, esttt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Changed from Button to View
        View sample = findViewById(R.id.see_sample_plans);
        View eng = findViewById(R.id.help_engineer);
        View manu = findViewById(R.id.manually);

        if (manu != null) {
            manu.setOnClickListener(view -> {
                Intent i = new Intent(Home_page.this, ManualPlanActivity.class);
                startActivity(i);
            });
        }

        btnlocl = findViewById(R.id.generateplab);
        if (btnlocl != null) {
            btnlocl.setOnClickListener(view -> {
                Intent i = new Intent(Home_page.this, GeneratePlan.class);
                startActivity(i);
            });
        }

        esttt = findViewById(R.id.est);
        if (esttt != null) {
            esttt.setOnClickListener(view -> {
                Intent i = new Intent(Home_page.this, MaterialEstimationActivity.class);
                startActivity(i);
            });
        }

        if (eng != null) {
            eng.setOnClickListener(view -> {
                Intent i = new Intent(Home_page.this, EngineersListActivity.class);
                startActivity(i);
            });
        }

        if (sample != null) {
            sample.setOnClickListener(view -> {
                Intent i = new Intent(Home_page.this, sample.class);
                startActivity(i);
            });
        }

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        imageSlider = findViewById(R.id.image_slider);
        btnGenerate = findViewById(R.id.generatee);
        btnGenerateWithAI = findViewById(R.id.generate_with_openai);

        if (btnGenerate != null) {
            btnGenerate.setOnClickListener(view -> {
                Intent i = new Intent(Home_page.this, ai.class);
                startActivity(i);
            });
        }

        if (btnGenerateWithAI != null) {
            btnGenerateWithAI.setOnClickListener(view -> {
                Intent i = new Intent(Home_page.this, GenerateAIActivity.class);
                startActivity(i);
            });
        }

        // Image Slider Code
        List<Integer> images = Arrays.asList(
                R.drawable.img7,
                R.drawable.img4,
                R.drawable.img5,
                R.drawable.a,
                R.drawable.aa,
                R.drawable.aaa,
                R.drawable.fffff,
                R.drawable.f,
                R.drawable.ff,
                R.drawable.fff,
                R.drawable.ffff,
                R.drawable.image1,
                R.drawable.image2,
                R.drawable.image3
        );

        if (imageSlider != null) {
            ImageSliderAdapter adapter = new ImageSliderAdapter(this, images);
            imageSlider.setAdapter(adapter);

            // Auto-slide logic
            sliderHandler = new Handler(Looper.getMainLooper());
            sliderRunnable = new Runnable() {
                @Override
                public void run() {
                    int currentItem = imageSlider.getCurrentItem();
                    int nextItem = (currentItem + 1) % images.size();
                    imageSlider.setCurrentItem(nextItem, true);
                    sliderHandler.postDelayed(this, 3000);
                }
            };
            sliderHandler.postDelayed(sliderRunnable, 3000);

            imageSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    sliderHandler.removeCallbacks(sliderRunnable);
                    sliderHandler.postDelayed(sliderRunnable, 3000);
                }
            });
        }
    }

    public void openSmartAIActivity(View view) {
        startActivity(new Intent(this, SmartAIActivity.class));
    }

    public void openFastOutputActivity(View view) {
        startActivity(new Intent(this, FastOutputActivity.class));
    }

    public void openBestDesignActivity(View view) {
        startActivity(new Intent(this, BestDesignActivity.class));
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.info) {
            startActivity(new Intent(this, informationtool.class));
            showToast("Information");
        } else if (id == R.id.help) {
            startActivity(new Intent(this, helptool.class));
            showToast("Help");
        } else if (id == R.id.contact) {
            startActivity(new Intent(this, contacttool.class));
            showToast("Contact Details");
        } else if (id == R.id.feedback) {
            sendFeedbackEmail();
        } else if (id == R.id.admin) {
            startActivity(new Intent(this, RegEng.class));
            showToast("Register");
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void sendFeedbackEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:shampatil7275@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for the App");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Write your feedback here...");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send Feedback"));
        } catch (android.content.ActivityNotFoundException ex) {
            showToast("No email app installed");
        }
    }
}
