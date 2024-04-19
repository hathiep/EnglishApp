package com.example.applayout.core.support.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applayout.R;
import com.example.applayout.core.support.Domains.UserDomain;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreatePlanSupport extends AppCompatActivity {
    DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference("users");
    private ImageView back_button;
    private ViewFlipper viewFlipper;
    private Button nextFlipperButton;
    private Button backFlipperButton;
    private UserDomain.Note note;

    private EditText title;
    private EditText body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_create_plan);

        title = findViewById(R.id.editTextTitle);
        body = findViewById(R.id.editTextNote);
        Button createPlanBtn = findViewById(R.id.buttonSaveNote);

        createPlanBtn.setOnClickListener(v -> {
            createPlan();
        });

        ImageView backBtn = findViewById(R.id.createdPlanBackBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    PlanSupport.class
            );
            startActivity(intent);
        });
    }

    private void createPlan() {
        EditText title = findViewById(R.id.editTextTitle);
        EditText body = findViewById(R.id.editTextNote);
        String titleText = title.getText().toString();
        String bodyText = body.getText().toString();
        note = new UserDomain.Note();

        note.setTitle(titleText);
        note.setBody(bodyText);
        note.setStatus("inactive");

        mDataBase.child("0").child("notes").push().setValue(note);
        Toast.makeText(this, "Note created", Toast.LENGTH_SHORT).show();
    }
}