package com.example.applayout.core.support.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.applayout.R;
import com.example.applayout.core.Profile;
import com.example.applayout.core.exam.ExamMain;
import com.example.applayout.core.exercise.ExerciseMain;
import com.example.applayout.core.learn.LearnMain;
import com.example.applayout.core.main_class.Exam;
import com.example.applayout.core.support.Domains.ExerciseDomain;
import com.example.applayout.core.support.SupportMain;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.common.util.concurrent.AtomicDouble;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TrackerSupport extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    List<ExerciseDomain.Task> tasks = new ArrayList<>();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");
    private List<String> xValues = Arrays.asList("Learn", "Exercise", "Exam");

    private float learnScore = 0, exerciseScore = 0, examScore = 0;
    AtomicDouble averageScore = new AtomicDouble(0);

    // Progress bar
    private ProgressBar progressBar;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;

    private TextView progressText;
    private TextView progressText1;
    private TextView progressText2;
    private TextView progressText3;

    private BarChart barChart;

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void updateTotalSum() {
        averageScore.set(learnScore + exerciseScore + examScore);
        progressBar.setProgress((int) averageScore.get() / 3);
        progressText.setText(String.format("%.1f", (averageScore.get() / 3)));
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, learnScore));
        entries.add(new BarEntry(1, exerciseScore));
        entries.add(new BarEntry(2, examScore));

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);

        BarDataSet barDataSet = new BarDataSet(
                entries,
                "Average Score"
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_tracker);

        Map<String, String> newWord = new LinkedHashMap<>();

        mDatabase.child(user.getUid())
                .child("newword")
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    System.out.println(dataSnapshot.getValue());
                                    newWord.put(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
                                }

                                learnScore = (float)(newWord.size() - 1) * 10;

                                updateTotalSum();


                                progressBar1.setProgress((int) learnScore);
                                progressText1.setText(String.format("%.1f", learnScore));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }
                );

        mDatabase.child(user.getUid())
                .child("exercise")
                .addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            System.out.println(dataSnapshot.getValue());
                            ExerciseDomain.Task task = dataSnapshot.getValue(ExerciseDomain.Task.class);
                            tasks.add(task);
                        }

                        for(ExerciseDomain.Task task : tasks) {
                            exerciseScore += task.getAnimals();
                            exerciseScore += task.getArt();
                            exerciseScore += task.getConstruction();
                            exerciseScore += task.getCorrespondence();
                            exerciseScore += task.getEconomic();
                            exerciseScore += task.getEntertainment();
                            exerciseScore += task.getEnvironment();
                            exerciseScore += task.getHealth();
                            exerciseScore += task.getHistory();
                            exerciseScore += task.getSport();
                        }

                        exerciseScore = (exerciseScore / 20) * 10;

                        updateTotalSum();

                        progressBar2.setProgress((int) exerciseScore);
                        progressText2.setText(String.format("%.1f", exerciseScore));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

        mDatabase.child(user.getUid())
                .addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if(dataSnapshot.getKey().equals("exam")) {

                                Exam exam = dataSnapshot.getValue(Exam.class);
                                examScore = ((float)(
                                        exam.getVocabulary() + exam.getGrammar() + exam.getListening() + exam.getWriting() + exam.getSynthetic()
                                ) / 5) * 10;

                                updateTotalSum();

                                progressBar3.setProgress((int) examScore);
                                progressText3.setText(String.format("%.1f", examScore));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


        setupCardViews();

        setupTaskbarButtons();

        // Setup progress bars
        progressBar = findViewById(R.id.weeklyProgressBar);
        progressText = findViewById(R.id.weeklyTotalTxt);

        progressBar1 = findViewById(R.id.vocabularyProgressBar);
        progressText1 = findViewById(R.id.vocabularyTotalTxt);

        progressBar2 = findViewById(R.id.exerciseProgressBar);
        progressText2 = findViewById(R.id.exerciseTotalTxt);

        progressBar3 = findViewById(R.id.testingProgressBar);
        progressText3 = findViewById(R.id.testingTotalTxt);

        // Building chart
        barChart = findViewById(R.id.barChart);
        barChart.getAxisRight().setDrawLabels(false);
    }

    // Setup CardView buttons
    private void setupCardViews() {
        CardView vocabularyCardView = findViewById(R.id.vocabularyCardView);
        vocabularyCardView.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    TaskSupport.class
            );
            intent.putExtra("taskType", "Learn");
            startActivity(intent);
        });

        CardView exerciseCardView = findViewById(R.id.exerciseCardView);
        exerciseCardView.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    TaskSupport.class
            );
            intent.putExtra("taskType", "Exercise");
            startActivity(intent);
        });

        CardView testingCardView = findViewById(R.id.testingCardView);
        testingCardView.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    TaskSupport.class
            );
            intent.putExtra("taskType", "Exam");
            startActivity(intent);
        });

        ImageView backBtn = findViewById(R.id.progressBackBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    SupportMain.class
            );
            startActivity(intent);
        });
    }

    // Setup Taskbar buttons
    private void setupTaskbarButtons() {
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