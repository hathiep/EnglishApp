package com.example.applayout.core.support.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
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
import com.example.applayout.core.support.Domains.UserDomain;
import com.example.applayout.core.support.SupportMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PlanSupport extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private UserDomain user = new UserDomain();
    private UserDomain.Note note;
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
        ArrayList<UserDomain.Note> items = new ArrayList<>();

        recyclerViewPlan = findViewById(R.id.view_plan);
        recyclerViewPlan.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));


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

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        user.setId(currentUser.getUid());

        System.out.println(user.getId());

        mDatabase = FirebaseDatabase.getInstance().getReference("User").child(user.getId()).child("notes");

        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            UserDomain.Note note = dataSnapshot.getValue(UserDomain.Note.class);
                            note.setId(dataSnapshot.getKey());
                            System.out.println(note.getCreatedDateReal());
                            items.add(note);
                        }
                        Collections.sort(items);
                        adapterPlan = new PlanAdapter(items, currentUser);
                        recyclerViewPlan.setAdapter(adapterPlan);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Error", error.getMessage());
                    }
                }
        );
    }
}