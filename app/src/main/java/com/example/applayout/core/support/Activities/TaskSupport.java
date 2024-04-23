package com.example.applayout.core.support.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applayout.R;
import com.example.applayout.core.Profile;
import com.example.applayout.core.exam.ExamMain;
import com.example.applayout.core.exercise.ExerciseMain;
import com.example.applayout.core.learn.LearnMain;
import com.example.applayout.core.support.Adapters.TaskAdapter;
import com.example.applayout.core.support.Domains.ExamDomain;
import com.example.applayout.core.support.Domains.ExerciseDomain;
import com.example.applayout.core.support.Domains.WordDomain;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.github.mikephil.charting.charts.BarChart;

public class TaskSupport extends AppCompatActivity {

    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private TaskAdapter adapterTask;
    private RecyclerView taskRecycler;
    private String taskType;
    private BarChart barChart;
    private CardView taskChart;

    private List<String> xValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Init layout
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        taskType = intent.getStringExtra("taskType");
        System.out.println(taskType);
        setContentView(R.layout.support_task);

        // Find chart components
        taskChart = findViewById(R.id.taskChart);
        barChart = findViewById(R.id.barTaskChart);
        barChart.getAxisRight().setDrawLabels(false);

        // Set task type
        TextView taskTypeText = findViewById(R.id.taskType);
        taskTypeText.setText(taskType);

        // Back button
        ImageView backBtn = findViewById(R.id.taskBackBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(
                    this,
                    TrackerSupport.class
            );
            startActivity(intent1);
        });

        // Call method
        initRecyclerView();
        initTaskBar();
    }

    private void initRecyclerView() {

        taskRecycler = findViewById(R.id.view_task);
        taskRecycler.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));

        switch (taskType) {
            case "Learn":
                ArrayList<WordDomain> words = new ArrayList<>();

                mDatabase.child(user.getUid())
                        .child("newword")
                        .addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            String time = dataSnapshot.getValue(String.class);
                                            String key = dataSnapshot.getKey();
                                            WordDomain word = new WordDomain(key, time);
                                            words.add(word);
                                        }
                                        words.remove(0);
                                        adapterTask = new TaskAdapter(words, taskType);
                                        System.out.println(words.get(0));
                                        taskRecycler.setAdapter(adapterTask);

                                        taskChart.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        System.out.println("Error");
                                    }
                                }
                        );

                break;
            case "Exercise":
                ArrayList<ExerciseDomain.Task> tasks = new ArrayList<>();
                xValues = Arrays.asList(
                        "An",
                        "Ar",
                        "Const",
                        "Cor",
                        "Eco",
                        "Ente",
                        "Envi",
                        "Hea",
                        "His"
                );

                mDatabase.child(user.getUid())
                        .child("exercise")
                        .addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @SuppressLint("DefaultLocale")
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            ExerciseDomain.Task task = dataSnapshot.getValue(ExerciseDomain.Task.class);
                                            tasks.add(task);
                                        }

                                        List<Pair<String, Integer>> taskProgress = new ArrayList<>();

                                        Pair<String, Integer> pair = new Pair<>("Animals", (tasks.get(0).getAnimals() + tasks.get(1).getAnimals()) / 2 * 10);
                                        taskProgress.add(pair);
                                        pair = new Pair<>("Art", (tasks.get(0).getArt() + tasks.get(1).getArt()) / 2 * 10);
                                        taskProgress.add(pair);
                                        pair = new Pair<>("Construction", (tasks.get(0).getConstruction() + tasks.get(1).getConstruction()) / 2 * 10);
                                        taskProgress.add(pair);
                                        pair = new Pair<>("Correspondence", (tasks.get(0).getCorrespondence() + tasks.get(1).getCorrespondence()) / 2 * 10);
                                        taskProgress.add(pair);
                                        pair = new Pair<>("Economic", (tasks.get(0).getEconomic() + tasks.get(1).getEconomic()) / 2 * 10);
                                        taskProgress.add(pair);
                                        pair = new Pair<>("Entertainment", (tasks.get(0).getEntertainment() + tasks.get(1).getEntertainment()) / 2 * 10);
                                        taskProgress.add(pair);
                                        pair = new Pair<>("Environment", (tasks.get(0).getEnvironment() + tasks.get(1).getEnvironment()) / 2 * 10);
                                        taskProgress.add(pair);
                                        pair = new Pair<>("Health", (tasks.get(0).getHealth() + tasks.get(1).getHealth()) / 2 * 10);
                                        taskProgress.add(pair);
                                        pair = new Pair<>("History", (tasks.get(0).getHistory() + tasks.get(1).getHistory()) / 2 * 10);
                                        taskProgress.add(pair);
                                        pair = new Pair<>("Sport", (tasks.get(0).getSport() + tasks.get(1).getSport()) / 2 * 10);
                                        taskProgress.add(pair);

                                        // Resource
                                        ArrayList<String> resources = new ArrayList<>();
                                        resources.add("support_animals");
                                        resources.add("support_art");
                                        resources.add("support_construction");
                                        resources.add("support_correctpondance");
                                        resources.add("support_economy");
                                        resources.add("support_entertaiment");
                                        resources.add("support_environment");
                                        resources.add("support_health");
                                        resources.add("support_history");
                                        resources.add("support_sport");

                                        // Set adapter
                                        adapterTask = new TaskAdapter(taskProgress, taskType);
                                        adapterTask.setResources(resources);
                                        taskRecycler.setAdapter(adapterTask);

                                        // Building chart
                                        ArrayList<BarEntry> entries = new ArrayList<>();
                                        entries.add(new BarEntry(0, taskProgress.get(0).second));
                                        entries.add(new BarEntry(1, taskProgress.get(1).second));
                                        entries.add(new BarEntry(2, taskProgress.get(2).second));
                                        entries.add(new BarEntry(3, taskProgress.get(3).second));
                                        entries.add(new BarEntry(4, taskProgress.get(4).second));
                                        entries.add(new BarEntry(5, taskProgress.get(5).second));
                                        entries.add(new BarEntry(6, taskProgress.get(6).second));
                                        entries.add(new BarEntry(7, taskProgress.get(7).second));
                                        entries.add(new BarEntry(8, taskProgress.get(8).second));

                                        YAxis yAxis = barChart.getAxisLeft();
                                        yAxis.setAxisMinimum(0f);
                                        yAxis.setAxisMaximum(100f);
                                        yAxis.setAxisLineWidth(2f);
                                        yAxis.setAxisLineColor(Color.BLACK);
                                        yAxis.setLabelCount(10);

                                        BarDataSet barDataSet = new BarDataSet(
                                                entries,
                                                "Exercise"
                                        );
                                        barDataSet.setColors(
                                                ColorTemplate.MATERIAL_COLORS
                                        );

                                        BarData barData = new BarData(barDataSet);
                                        barChart.setData(barData);
                                        barChart.getDescription().setEnabled(false);
                                        barChart.invalidate();
                                        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
                                        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                        barChart.getXAxis().setGranularity(1f);
                                        barChart.getXAxis().setGranularityEnabled(true);

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        System.out.println("Error");
                                    }
                                }
                        );
                break;
            case "Exam":
                ArrayList<ExamDomain> exams = new ArrayList<>();

                mDatabase.child(user.getUid())
                        .child("exam")
                        .addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            int score = dataSnapshot.getValue(Integer.class);
                                            String title = dataSnapshot.getKey();
                                            ExamDomain exam = new ExamDomain(title, score);
                                            exams.add(exam);

                                            // Set resources
                                            ArrayList<String> resources = new ArrayList<>();
                                            resources.add("support_grammar_exam");
                                            resources.add("support_listening_exam");
                                            resources.add("support_synthetic_exam");
                                            resources.add("support_vocabulary_exam");
                                            resources.add("support_writing_exam");

                                            // Set adapter
                                            TaskAdapter adapterTask = new TaskAdapter();
                                            adapterTask.setExams(exams);
                                            adapterTask.setTaskType(taskType);
                                            adapterTask.setResources(resources);
                                            taskRecycler.setAdapter(adapterTask);

                                            // Building chart
                                            // Set columns
                                            xValues = new ArrayList<>();
                                            for (ExamDomain examDomain : exams) {
                                                xValues.add(
                                                        Character.toUpperCase(examDomain.getTitle().charAt(0))
                                                                + examDomain.getTitle().substring(1)
                                                );
                                            }

                                            // Set data entries
                                            ArrayList<BarEntry> entries = new ArrayList<>();
                                            for(int i = 0; i < exams.size(); i++) {
                                                entries.add(new BarEntry(i, exams.get(i).getScore()));
                                            }

                                            YAxis yAxis = barChart.getAxisLeft();
                                            yAxis.setAxisMinimum(0f);
                                            yAxis.setAxisMaximum(10f);
                                            yAxis.setAxisLineWidth(2f);
                                            yAxis.setAxisLineColor(Color.BLACK);
                                            yAxis.setLabelCount(10);

                                            BarDataSet barDataSet = new BarDataSet(
                                                    entries,
                                                    "Exam"
                                            );
                                            barDataSet.setColors(
                                                    ColorTemplate.MATERIAL_COLORS
                                            );

                                            BarData barData = new BarData(barDataSet);
                                            barChart.setData(barData);
                                            barChart.getDescription().setEnabled(false);
                                            barChart.invalidate();
                                            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
                                            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                            barChart.getXAxis().setGranularity(1f);
                                            barChart.getXAxis().setGranularityEnabled(true);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                }
                        );
                break;
        }
    }

    private void initTaskBar() {
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