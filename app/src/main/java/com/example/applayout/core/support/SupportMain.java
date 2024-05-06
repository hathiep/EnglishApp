package com.example.applayout.core.support;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.applayout.R;
import com.example.applayout.core.Profile;
import com.example.applayout.core.exam.ExamMain;
import com.example.applayout.core.exercise.ExerciseMain;
import com.example.applayout.core.learn.LearnMain;
import com.example.applayout.core.support.Activities.AnnouncementSupport;
import com.example.applayout.core.support.Activities.PlanSupport;
import com.example.applayout.core.support.Activities.TrackerSupport;

public class SupportMain extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.support_general_plan);

            ImageView backBtn = findViewById(R.id.generalBackBtn);
            backBtn.setOnClickListener(v -> {
                Intent intent = new Intent(
                        SupportMain.this,
                        com.example.applayout.core.MainActivity.class
                );
                startActivity(intent);
            });

            ImageView imV_support = findViewById(R.id.imV_support);
            TextView tv_support = findViewById(R.id.tv_support);
            imV_support.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imV_support.setImageResource(R.drawable.icon_support2);
            tv_support.setTextAppearance(R.style.menu_text);

            TextView progressBtn = findViewById(R.id.btn_progress);
            progressBtn.setOnClickListener(v -> {
                Intent intent = new Intent(
                        SupportMain.this,
                        TrackerSupport.class
                );
                startActivity(intent);
            });
            TextView planBtn = findViewById(R.id.btn_plan);
            planBtn.setOnClickListener(v -> {
                Intent intent = new Intent(
                        SupportMain.this,
                        PlanSupport.class
                );
                startActivity(intent);
            });
            TextView announcementBtn = findViewById(R.id.btn_annoucement);
            announcementBtn.setOnClickListener(v -> {
                Intent intent = new Intent(
                        SupportMain.this,
                        AnnouncementSupport.class
                );
                startActivity(intent);
            });

            ImageView imV_home = findViewById(R.id.imV_home);
            ImageView imV_learn = findViewById(R.id.imV_learn);
            ImageView imV_exercise = findViewById(R.id.imV_exercise);
            ImageView imV_profile = findViewById(R.id.imV_profile);
            ImageView imv_exam = findViewById(R.id.imV_exam);

            imV_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), com.example.applayout.core.MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            imV_learn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), LearnMain.class);
                    startActivity(intent);
                    finish();
                }
            });
            imV_exercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ExerciseMain.class);
                    startActivity(intent);
                    finish();
                }
            });
            imv_exam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ExamMain.class);
                    startActivity(intent);
                    finish();
                }
            });
            imV_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Profile.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
}