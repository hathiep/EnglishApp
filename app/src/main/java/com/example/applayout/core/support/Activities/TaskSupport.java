package com.example.applayout.core.support.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applayout.R;
import com.example.applayout.core.Profile;
import com.example.applayout.core.exam.ExamMain;
import com.example.applayout.core.exercise.ExerciseMain;
import com.example.applayout.core.learn.LearnMain;
import com.example.applayout.core.support.Adapters.TaskAdapter;
import com.example.applayout.core.support.Domains.TaskDomain;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TaskSupport extends AppCompatActivity {
    private RecyclerView.Adapter adapterTask;
    private RecyclerView taskRecycler;

    private ImageView back_button;
    private ViewFlipper viewFlipper;
    private Button nextFlipperButton;
    private Button backFlipperButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String taskType = intent.getStringExtra("taskType");
        System.out.println(taskType);

        setContentView(R.layout.support_task);
        TextView taskTypeText = findViewById(R.id.taskType);
        taskTypeText.setText(taskType);

        ImageView backBtn = findViewById(R.id.taskBackBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(
                    this,
                    TrackerSupport.class
            );
            startActivity(intent1);
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        ArrayList<TaskDomain> taskList = new ArrayList<>();
        taskList.add(new TaskDomain(
                1,
                "Khoa học",
                "Đây là thứ vĩ đại",
                14,
                "pic1"
        ));
        taskList.add(new TaskDomain(
                2,
                "Chính trị",
                "Đây là thứ vĩ đại",
                70,
                "pic1"
        ));
        taskList.add(new TaskDomain(
                3,
                "Đời sống",
                "Đây là thứ vĩ đại",
                45,
                "pic1"
        ));
        taskList.add(new TaskDomain(
                4,
                "Báo chí",
                "Đây là thứ vĩ đại",
                34,
                "pic1"
        ));
        taskList.add(new TaskDomain(
                5,
                "Xã hội",
                "Đây là thứ vĩ đại",
                20,
                "pic1"
        ));
        taskList.add(new TaskDomain(
                5,
                "Xã hội",
                "Đây là thứ vĩ đại",
                20,
                "pic1"
        ));
        taskList.add(new TaskDomain(
                5,
                "Xã hội",
                "Đây là thứ vĩ đại",
                20,
                "pic1"
        ));
        taskList.add(new TaskDomain(
                5,
                "Xã hội",
                "Đây là thứ vĩ đại",
                20,
                "pic1"
        ));
        taskList.add(new TaskDomain(
                5,
                "Xã hội",
                "Đây là thứ vĩ đại",
                20,
                "pic1"
        ));
        taskList.add(new TaskDomain(
                5,
                "Xã hội",
                "Đây là thứ vĩ đại",
                20,
                "pic1"
        ));
        taskList.add(new TaskDomain(
                5,
                "Xã hội",
                "Đây là thứ vĩ đại",
                20,
                "pic1"
        ));        taskList.add(new TaskDomain(
                5,
                "Xã hội",
                "Đây là thứ vĩ đại",
                20,
                "pic1"
        ));

        System.out.println(taskList.size());
        taskRecycler = findViewById(R.id.view_task);
        taskRecycler.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));

        adapterTask = new TaskAdapter(taskList);
        taskRecycler.setAdapter(adapterTask);

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