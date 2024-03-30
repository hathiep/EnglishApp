package com.example.applayout.core.support.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learning_app.R;

public class SupportSupport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_general_plan);
        Button progressBtn = findViewById(R.id.btn_progress);
        progressBtn.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SupportSupport.this,
                    TrackerSupport.class
            );
            startActivity(intent);
        });
        Button planBtn = findViewById(R.id.btn_plan);
        planBtn.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SupportSupport.this,
                    PlanSupport.class
            );
            startActivity(intent);
        });
        Button announcementBtn = findViewById(R.id.btn_annoucement);
        announcementBtn.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SupportSupport.this,
                    AnnouncementSupport.class
            );
            startActivity(intent);
        });
    }
}