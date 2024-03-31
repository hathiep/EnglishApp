package com.example.applayout.core.support.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applayout.R;
import com.example.applayout.core.support.Adapters.TaskAdapter;
import com.example.applayout.core.support.Domains.TaskDomain;

import java.util.ArrayList;

public class TaskSupport extends AppCompatActivity {
    private RecyclerView.Adapter adapterTask;
    private RecyclerView taskRecycler;
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

    }
}