package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.content.Intent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * BaseActivity injects the bottom navigation bar into every screen that extends it.
 * Screens that should NOT have the bottom nav (Login, Register, Splash) must extend
 * AppCompatActivity directly instead of BaseActivity.
 *
 * Navigation map:
 *   nav_home     → Home_page
 *   nav_plans    → SamplePlansActivity
 *   nav_estimate → MaterialEstimationActivity
 *   nav_profile  → EngineersListActivity (Get Help / Profile)
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        // 1. Inflate a root FrameLayout that will hold both the screen content and the nav bar
        FrameLayout fullLayout = new FrameLayout(this);
        fullLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // 2. Inflate the actual screen content
        android.view.LayoutInflater inflater = getLayoutInflater();
        android.view.View contentView = inflater.inflate(layoutResID, fullLayout, false);
        fullLayout.addView(contentView);

        // 3. Inflate and add the bottom nav on top
        BottomNavigationView bottomNav = (BottomNavigationView)
                inflater.inflate(R.layout.layout_bottom_nav, fullLayout, false);

        // Anchor nav bar to bottom by using a FrameLayout.LayoutParams
        FrameLayout.LayoutParams navParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.bottom_nav_height)
        );
        navParams.gravity = android.view.Gravity.BOTTOM;
        bottomNav.setLayoutParams(navParams);
        fullLayout.addView(bottomNav);

        // 4. Add bottom padding to content so it doesn't hide behind the nav bar
        contentView.setPadding(
                contentView.getPaddingLeft(),
                contentView.getPaddingTop(),
                contentView.getPaddingRight(),
                getResources().getDimensionPixelSize(R.dimen.bottom_nav_height)
        );

        super.setContentView(fullLayout);

        // 5. Wire up navigation
        setupBottomNav(bottomNav);
    }

    private void setupBottomNav(@NonNull BottomNavigationView nav) {
        // Highlight the correct tab for the current screen
        nav.setSelectedItemId(getCurrentNavItem());

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            int current = getCurrentNavItem();

            // Don't re-launch the same screen
            if (id == current) return true;

            Intent intent = null;
            if (id == R.id.nav_home) {
                intent = new Intent(this, Home_page.class);
            } else if (id == R.id.nav_plans) {
                intent = new Intent(this, SamplePlansActivity.class);
            } else if (id == R.id.nav_estimate) {
                intent = new Intent(this, MaterialEstimationActivity.class);
            } else if (id == R.id.nav_profile) {
                intent = new Intent(this, ProfileActivity.class);
            }

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation for tab switches
                return true;
            }
            return false;
        });
    }

    /**
     * Each subclass overrides this to return its nav tab ID so the correct tab is highlighted.
     * Default is nav_home.
     */
    protected int getCurrentNavItem() {
        return R.id.nav_home;
    }
}
