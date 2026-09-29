package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class SamplePlansActivity extends BaseActivity {

    @Override
    protected int getCurrentNavItem() {
        return R.id.nav_plans;
    }

    private Map<Integer, Integer> planMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_plans);

        // Map button IDs to drawable resources
        planMap.put(R.id.btn_500, R.drawable.panso);
        planMap.put(R.id.btn_1000, R.drawable.hazar);
        planMap.put(R.id.btn_1500, R.drawable.pandhara);
        planMap.put(R.id.btn_2000, R.drawable.h2000);
        planMap.put(R.id.btn_2500, R.drawable.h2500);
        planMap.put(R.id.btn_3000, R.drawable.h3000);
        planMap.put(R.id.btn_3500, R.drawable.h3500);
        planMap.put(R.id.btn_4000, R.drawable.h4000);
        planMap.put(R.id.btn_4500, R.drawable.h4500);
        planMap.put(R.id.btn_5000, R.drawable.h5000);
    }

    public void openPlan(View view) {
        int buttonId = view.getId();
        Integer drawableId = planMap.get(buttonId);

        if (drawableId != null) {
            Intent intent = new Intent(this, PlanViewerActivity.class);
            intent.putExtra("DRAWABLE_ID", drawableId);
            startActivity(intent);
        }
    }
}