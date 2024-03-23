package com.example.applayout.core.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applayout.R;
import com.example.applayout.core.MainActivity;

public class ExerciseMain extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.exercise_main_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvUnit1 = findViewById(R.id.unit_1);
        TextView tvNangCao = findViewById(R.id.tv_nangcao);
        ImageView ic_back = findViewById(R.id.ic_back);

        //Đánh dấu activity hiện tại trên thanh menu
        ImageView imV_exercise = findViewById(R.id.imV_exercise);
        TextView tv_exercise = findViewById(R.id.tv_exercise);
        imV_exercise.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imV_exercise.setImageResource(R.drawable.icon_exercise2);
        tv_exercise.setTextAppearance(R.style.menu_text);

        //next trang unit1 co ban
        tvUnit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExerciseUnit1BasicActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //next trang nang cao
        tvNangCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExerciseAdvancedActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //back trang home
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //---------------------

    }
}