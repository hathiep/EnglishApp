package com.example.applayout.core.support.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applayout.R;


public class CreatePlanSupport extends AppCompatActivity {
    private ImageView back_button;
    private ViewFlipper viewFlipper;
    private Button nextFlipperButton;
    private Button backFlipperButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_create_plan);

        ImageView backBtn = findViewById(R.id.createdPlanBackBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    PlanSupport.class
            );
            startActivity(intent);
        });
    }
}