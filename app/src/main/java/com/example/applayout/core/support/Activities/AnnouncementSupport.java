package com.example.applayout.core.support.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applayout.R;
import com.example.applayout.core.Profile;
import com.example.applayout.core.exam.ExamMain;
import com.example.applayout.core.exercise.ExerciseMain;
import com.example.applayout.core.learn.LearnMain;
import com.example.applayout.core.support.SupportMain;
import com.example.applayout.core.support.utils.NotificationHelper;


public class AnnouncementSupport extends AppCompatActivity {
    private ImageView back_button;
    private ViewFlipper viewFlipper;
    private Button nextFlipperButton;
    private Button backFlipperButton;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_announcement);

        ImageView backBtn = findViewById(R.id.announceBackBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    SupportMain.class
            );
            startActivity(intent);
        });

        Switch switch1 = findViewById(R.id.switch1);
        switch1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if (switch1.isChecked()) {
                    NotificationHelper.showNotification(
                            getApplicationContext(),
                            "New Announcement",
                            "New announcement from the support team",
                            2002,
                            "https://pbs.twimg.com/media/GLtoRg5WYAAvJtQ?format=jpg&name=large"
                    );
                } else {
                    NotificationHelper.cancelNotification(
                            getApplicationContext(),
                            2002
                    );
                }
            }
        });

        ImageView imV_home = findViewById(R.id.imV_home);
        ImageView imV_learn = findViewById(R.id.imV_learn);
        ImageView imV_exercise = findViewById(R.id.imV_exercise);
        ImageView imV_support = findViewById(R.id.imV_support);
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