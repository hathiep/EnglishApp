package com.example.applayout.core.support.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.learning_app.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrackerSupport extends AppCompatActivity {

    private List<String> xValues = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

    private ProgressBar progressBar;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;

    private TextView progressText;
    private TextView progressText1;
    private TextView progressText2;
    private TextView progressText3;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_tracker);

        CardView vocabularyCardView = findViewById(R.id.vocabularyCardView);
        vocabularyCardView.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    TaskSupport.class
            );
            intent.putExtra("taskType", "Vocabulary");
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
            intent.putExtra("taskType", "Testing");
            startActivity(intent);
        });

        ImageView backBtn = findViewById(R.id.progressBackBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    SupportSupport.class
            );
            startActivity(intent);
        });

        progressBar = findViewById(R.id.weeklyProgressBar);
        progressText = findViewById(R.id.weeklyTotalTxt);

        progressBar1 = findViewById(R.id.vocabularyProgressBar);
        progressText1 = findViewById(R.id.vocabularyTotalTxt);

        progressBar2 = findViewById(R.id.exerciseProgressBar);
        progressText2 = findViewById(R.id.exerciseTotalTxt);

        progressBar3 = findViewById(R.id.testingProgressBar);
        progressText3 = findViewById(R.id.testingTotalTxt);

        progressBar.setProgress(75);
        progressText.setText("75%");
        progressBar1.setProgress(90);
        progressText1.setText("90%");
        progressBar2.setProgress(60);
        progressText2.setText("60%");
        progressBar3.setProgress(80);
        progressText3.setText("80%");


        BarChart barChart = findViewById(R.id.barChart);
        barChart.getAxisRight().setDrawLabels(false);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 45f));
        entries.add(new BarEntry(1, 80f));
        entries.add(new BarEntry(2, 65f));
        entries.add(new BarEntry(3, 38f));
        entries.add(new BarEntry(4, 38f));
        entries.add(new BarEntry(5, 90f));
        entries.add(new BarEntry(6, 70f));

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);

        BarDataSet barDataSet = new BarDataSet(
                entries,
                "Weekly Progress"
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