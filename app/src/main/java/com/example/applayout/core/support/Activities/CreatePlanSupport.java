package com.example.applayout.core.support.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applayout.R;
import com.example.applayout.core.support.Domains.UserDomain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;


public class CreatePlanSupport extends AppCompatActivity {
    private final DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference("User");
    private ImageView back_button;
    private ViewFlipper viewFlipper;
    private Button nextFlipperButton;
    private Button backFlipperButton;
    private UserDomain.Note note;

    private EditText title;
    private EditText body;
    private TextView datePickerDate;
    private LocalDateTime datePickerTime;
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_create_plan);

        title = findViewById(R.id.editTextTitle);
        body = findViewById(R.id.editTextNote);
        Button datePickerButton = findViewById(R.id.datePickerButton);
        datePickerDate = findViewById(R.id.datePickerTxt);

        datePickerButton.setOnClickListener(
                v -> showDatePickerDialog()
        );

        Button createPlanBtn = findViewById(R.id.buttonSaveNote);
        createPlanBtn.setOnClickListener(v -> {
            if(validateInput()) {
                createPlan();
            }
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

        String dueDate = datePickerTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        note = new UserDomain.Note();
        note.setTitle(titleText);
        note.setBody(bodyText);
        note.setStatus("inactive");
        note.setCreatedDate(getCurrent());
        note.setDueDate(dueDate);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        assert currentUser != null;
        mDataBase.child(currentUser.getUid()).child("notes").push().setValue(note);
        Toast.makeText(this, "Note created", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(
                this,
                PlanSupport.class
        );
        startActivity(intent);
    }

    private String getCurrent() {
        LocalDateTime now = LocalDateTime.now();
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return now.format(formatter);
    }

    private void showDatePickerDialog() {
        LocalDateTime now = LocalDateTime.now();
        // Get the current date
        int yearNow = now.getYear();
        int monthNow = now.getMonthValue();
        int dayNow = now.getDayOfMonth();
        // Get the current time
        int hourNow = now.getHour();
        int minuteNow = now.getMinute();

        System.out.println(yearNow + " " + monthNow + " " + dayNow);

        // Show DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (dateView, year, month, dayOfMonth) -> {
                    // Show TimePickerDialog
                    @SuppressLint("SetTextI18n") TimePickerDialog timePickerDialog = new TimePickerDialog(
                            this,
                            (timeView, hourOfDay, minute) -> {
                                // 2024-05-02T14:34:21
                                String current = year + "-" + month + "-" + dayOfMonth + " " + hourOfDay + ":" + minute;
                                datePickerTime = LocalDateTime.of(year, month, dayOfMonth, hourOfDay, minute);
                                datePickerDate.setText(current);
                                System.out.println(current);
                            },
                            hourNow,
                            minuteNow,
                            true
                    );
                    timePickerDialog.show();
                },
                yearNow,
                monthNow - 1,
                dayNow
        );
        datePickerDialog.show();
    }


    // Validate form input
    private boolean validateInput() {
        AtomicReference<Boolean> isValid = new AtomicReference<>(true);
        if (title.getText().toString().isEmpty()) {
            title.setError("Title is required");
            isValid.set(false);
        }
        if (body.getText().toString().isEmpty()) {
            body.setError("Body is required");
            isValid.set(false);
        }
        if (datePickerDate.getText().toString().isEmpty()) {
            datePickerDate.setError("Date is required");
            isValid.set(false);
        }
        return isValid.get();
    }
}