package com.example.applayout.core.support.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learning_app.R;

public class AnnouncementSupport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_announcement);

        ImageView backBtn = findViewById(R.id.announceBackBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    SupportSupport.class
            );
            startActivity(intent);
        });
    }
}