package com.example.applayout.core.support.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applayout.R;
import com.example.applayout.core.Profile;
import com.example.applayout.core.exam.ExamMain;
import com.example.applayout.core.exercise.ExerciseMain;
import com.example.applayout.core.learn.LearnMain;
import com.example.applayout.core.support.Adapters.PlanAdapter;
import com.example.applayout.core.support.Domains.PlanDomain;
import com.example.applayout.core.support.SupportMain;

import java.util.ArrayList;

public class PlanSupport extends AppCompatActivity {
    private RecyclerView.Adapter adapterPlan;
    private RecyclerView recyclerViewPlan;

    private ImageView back_button;
    private ViewFlipper viewFlipper;
    private Button nextFlipperButton;
    private Button backFlipperButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_plan);
        initRecyclerView();
        Button progressBtn = findViewById(R.id.add_btn);
        progressBtn.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    CreatePlanSupport.class
            );
            startActivity(intent);
        });

        ImageView backBtn = findViewById(R.id.planBackBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    SupportMain.class
            );
            startActivity(intent);
        });
    }

    private void initRecyclerView() {
        ArrayList<PlanDomain> items = new ArrayList<>();
        items.add(new PlanDomain(
                1,
                "Học 10 từ vựng tiếng Anh",
                "Hôm nay tôi sẽ học 10 từ vựng tiếng anh và tôi sẽ cố gắng để đạt được điều đó",
                null,
                true
        ));
        items.add(new PlanDomain(
                2,
                "Học 10 từ vựng tiếng Anh",
                "Hôm nay tôi sẽ học 10 từ vựng tiếng anh và tôi sẽ cố gắng để đạt được điều đó",
                null,
                false
        ));
        items.add(new PlanDomain(
                3,
                "Học 10 từ vựng tiếng Anh",
                "Hôm nay tôi sẽ học 10 từ vựng tiếng anh và tôi sẽ cố gắng để đạt được điều đó",
                null,
                false
        ));
        items.add(new PlanDomain(
                4,
                "Học 10 từ vựng tiếng Anh",
                "Hôm nay tôi sẽ học 10 từ vựng tiếng anh và tôi sẽ cố gắng để đạt được điều đó",
                null,
                false
        ));
        items.add(new PlanDomain(
                5,
                "Học 10 từ vựng tiếng Anh",
                "Hôm nay tôi sẽ học 10 từ vựng tiếng anh và tôi sẽ cố gắng để đạt được điều đó",
                null,
                false
        ));
        items.add(new PlanDomain(
                6,
                "Học 10 từ vựng tiếng Anh",
                "Hôm nay tôi sẽ học 10 từ vựng tiếng anh và tôi sẽ cố gắng để đạt được điều đó",
                null,
                true
        ));
        items.add(new PlanDomain(
                7,
                "Học 10 từ vựng tiếng Anh",
                "Hôm nay tôi sẽ học 10 từ vựng tiếng anh và tôi sẽ cố gắng để đạt được điều đó",
                null,
                false
        ));

        recyclerViewPlan = findViewById(R.id.view_plan);
        recyclerViewPlan.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));

        adapterPlan = new PlanAdapter(items);
        recyclerViewPlan.setAdapter(adapterPlan);

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