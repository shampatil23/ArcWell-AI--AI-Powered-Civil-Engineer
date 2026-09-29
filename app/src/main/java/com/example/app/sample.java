package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;

public class sample extends BaseActivity {

    GridView gridView;

    String[] planNames = {
            "Home Plan", "School Plan", "College Plan", "Office Plan", "Restaurant Plan",
            "Shopping Mall Plan", "Hospital Plan", "Hotel Plan", "Gym Plan", "Park Plan"
    };

    int[][] planImages = {
            {R.drawable.h2000, R.drawable.h2000, R.drawable.h2500, R.drawable.h3000, R.drawable.h4000},
            {R.drawable.s1, R.drawable.s2, R.drawable.s3, R.drawable.s4, R.drawable.s5},
            {R.drawable.c1, R.drawable.c2, R.drawable.c3, R.drawable.c4, R.drawable.c5},
            {R.drawable.o1, R.drawable.o2, R.drawable.o3, R.drawable.o4, R.drawable.o5},
            {R.drawable.r1, R.drawable.r2, R.drawable.r3, R.drawable.r4, R.drawable.r5},
            {R.drawable.s1, R.drawable.s2, R.drawable.s3, R.drawable.s4, R.drawable.s5},
            {R.drawable.h1, R.drawable.h3, R.drawable.h3, R.drawable.h4, R.drawable.h5},
            {R.drawable.hh1, R.drawable.hh2, R.drawable.hh3, R.drawable.hh4, R.drawable.hh5},
            {R.drawable.j1, R.drawable.j2, R.drawable.j3, R.drawable.j4, R.drawable.j5},
            {R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5}
    };

    // New GridView Images
    int[] gridImages = {
            R.drawable.h1111, R.drawable.sp, R.drawable.cp,
            R.drawable.op, R.drawable.rp, R.drawable.spp,
            R.drawable.h1111, R.drawable.hpp, R.drawable.gp, R.drawable.pp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        gridView = findViewById(R.id.gridView);

        PlanAdapter adapter = new PlanAdapter(this, planNames, gridImages);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(sample.this, PlanDetailsActivity.class);
                intent.putExtra("imageArray", planImages[position]);
                intent.putExtra("name", planNames[position]);
                startActivity(intent);
            }
        });
    }
}
