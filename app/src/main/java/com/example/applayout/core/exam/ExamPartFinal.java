package com.example.applayout.core.exam;

import android.content.Context;
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
import com.example.applayout.core.Profile;
import com.example.applayout.core.exercise.ExerciseMain;
import com.example.applayout.core.learn.LearnMain;
import com.example.applayout.core.support.SupportMain;

public class ExamPartFinal extends AppCompatActivity {
    ImageView imV_back, imV_home, imV_learn, imV_exercise, imV_exam, imV_support, imV_profile;
    TextView tv_exam, tv_part, tv_exam_name, tv_heading, tv_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.exam_part_final);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        initUi();
        // Gán giá trị text
        setTextUi();
        // Gọi hàm onClick
        try {
            setOnClickListener();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    // Hàm ánh xạ view
    private void initUi() {
        // Ánh xạ view header và body
        tv_part = findViewById(R.id.tv_part);
        tv_exam_name = findViewById(R.id.tv_exam_name);
        tv_heading = findViewById(R.id.tv_heading);
        tv_point = findViewById(R.id.tv_point);
        // Ánh xạ view menu
        imV_back = findViewById(R.id.imV_back);
        imV_home = findViewById(R.id.imV_home);
        imV_learn = findViewById(R.id.imV_learn);
        imV_exercise = findViewById(R.id.imV_exercise);
        imV_support = findViewById(R.id.imV_support);
        imV_profile = findViewById(R.id.imV_profile);
        //Đánh dấu activity hiện tại trên thanh menu
        imV_exam = findViewById(R.id.imV_exam);
        tv_exam = findViewById(R.id.tv_exam);
        imV_exam.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imV_exam.setImageResource(R.drawable.icon_exam2);
        tv_exam.setTextAppearance(R.style.menu_text);
    }
    private void setTextUi(){
        tv_part.setText(Result.part);
        tv_exam_name.setText(Result.exam_name);
        tv_heading.setText("Chúc mừng bạn đã hoàn thành bài kiểm tra " + Result.part + " - " + Result.exam_name + " với số điểm là");
        tv_point.setText(Result.point);
    }

    // Hàm onClickListener
    private void setOnClickListener() throws IllegalAccessException, InstantiationException {
        // Menu dưới màn hình
        onClickImVMenu(imV_back, ExamMain.class.newInstance());
        onClickImVMenu(imV_home, ExamMain.class.newInstance());
        onClickImVMenu(imV_learn, LearnMain.class.newInstance());
        onClickImVMenu(imV_exercise, ExerciseMain.class.newInstance());
        onClickImVMenu(imV_support, SupportMain.class.newInstance());
        onClickImVMenu(imV_profile, Profile.class.newInstance());
    }

    // Hàm onClickImageView
    private void onClickImVMenu(ImageView imV, Context context) {
        imV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), context.getClass());
                startActivity(intent);
            }
        });
    }

}