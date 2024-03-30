package com.example.applayout.core.support.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learning_app.Adapters.PlanAdapter;
import com.example.learning_app.Domains.PlanDomain;
import com.example.learning_app.R;

import java.util.ArrayList;

public class PlanSupport extends AppCompatActivity {
    private RecyclerView.Adapter adapterPlan;
    private RecyclerView recyclerViewPlan;

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
                    SupportSupport.class
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

    }
}