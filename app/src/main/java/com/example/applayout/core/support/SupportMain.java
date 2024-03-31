package com.example.applayout.core.support;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applayout.R;
import com.example.applayout.core.support.Activities.AnnouncementSupport;
import com.example.applayout.core.support.Activities.PlanSupport;
import com.example.applayout.core.support.Activities.TrackerSupport;

public class SupportMain extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.support_general_plan);

            ImageView imV_support = findViewById(R.id.imV_support);
            TextView tv_support = findViewById(R.id.tv_support);
            imV_support.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imV_support.setImageResource(R.drawable.icon_support2);
            tv_support.setTextAppearance(R.style.menu_text);

            Button progressBtn = findViewById(R.id.btn_progress);
            progressBtn.setOnClickListener(v -> {
                Intent intent = new Intent(
                        SupportMain.this,
                        TrackerSupport.class
                );
                startActivity(intent);
            });
            Button planBtn = findViewById(R.id.btn_plan);
            planBtn.setOnClickListener(v -> {
                Intent intent = new Intent(
                        SupportMain.this,
                        PlanSupport.class
                );
                startActivity(intent);
            });
            Button announcementBtn = findViewById(R.id.btn_annoucement);
            announcementBtn.setOnClickListener(v -> {
                Intent intent = new Intent(
                        SupportMain.this,
                        AnnouncementSupport.class
                );
                startActivity(intent);
            });
    }
}