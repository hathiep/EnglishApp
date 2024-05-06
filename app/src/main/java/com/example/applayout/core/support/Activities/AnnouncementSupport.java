package com.example.applayout.core.support.Activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.applayout.R;
import com.example.applayout.core.Profile;
import com.example.applayout.core.exam.ExamMain;
import com.example.applayout.core.exercise.ExerciseMain;
import com.example.applayout.core.learn.LearnMain;
import com.example.applayout.core.main_class.User;
import com.example.applayout.core.support.Adapters.PlanAdapter;
import com.example.applayout.core.support.Domains.ExerciseDomain;
import com.example.applayout.core.support.Domains.UserDomain;
import com.example.applayout.core.support.SupportMain;
import com.example.applayout.core.support.utils.AlarmReceiver;
import com.example.applayout.core.support.utils.NotificationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class AnnouncementSupport extends AppCompatActivity {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");
    private List<UserDomain.Note> items = new ArrayList<>();
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_announcement);

        ImageView backBtn = findViewById(R.id.announceBackBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    SupportMain.class
            );
            startActivity(intent);
        });

        Switch switch1 = findViewById(R.id.switch1);
        switch1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"UseCompatLoadingForDrawables", "ShortAlarm"})
            @Override
            public void onClick(View view) {
                if (switch1.isChecked()) {
                    scheduleAlarm(getApplicationContext());
                } else {
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    if (alarmManager == null) {
                        return;
                    } else {
                        Log.d("Alarm", "Alarm is open");
                    }
                    Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    alarmManager.cancel(pendingIntent);
                }
            }
        });

        Switch switch2 = findViewById(R.id.switch2);
        switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switch2.isChecked()){
                    checkTaskAndShowNotification();
                } else {
                    items.clear();
                }
            }
        });
        Switch switch3 = findViewById(R.id.switch3);
        switch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                boolean a  =false;
                if(pendingIntent != null) {
                    a = true;
                }
                Toast.makeText(getApplicationContext(), "Open " + a, Toast.LENGTH_SHORT).show();
            }
        });

        ImageView imV_home = findViewById(R.id.imV_home);
        ImageView imV_learn = findViewById(R.id.imV_learn);
        ImageView imV_exercise = findViewById(R.id.imV_exercise);
        ImageView imV_support = findViewById(R.id.imV_support);
        ImageView imV_profile = findViewById(R.id.imV_profile);
        ImageView imv_exam = findViewById(R.id.imV_exam);
        TextView tv_support = findViewById(R.id.tv_support);
        imV_support.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imV_support.setImageResource(R.drawable.icon_support2);
        tv_support.setTextAppearance(R.style.menu_text);

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
    @SuppressLint("ScheduleExactAlarm")
    public static void scheduleAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            return;
        }

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.add(Calendar.MINUTE, 1);

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                60000 * 2,
                pendingIntent
        );
    }
    private void checkTaskAndShowNotification() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;

        mDatabase = FirebaseDatabase.getInstance().getReference("User").child(currentUser.getUid()).child("notes");
        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            UserDomain.Note note = dataSnapshot.getValue(UserDomain.Note.class);
                            assert note != null;
                            note.setId(dataSnapshot.getKey());
                            items.add(note);
                            int count = 0;
                            for(UserDomain.Note not : items) {
                                if(not.getStatus().equals("inactive")) {
                                    count++;
                                }
                            }
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(2001, NotificationHelper.createNotification(
                                    getApplicationContext(),
                                    "Hoàn thiện kế hoạch",
                                    "Bạn đang có " + count + " kế hoạch chưa hoàn thiện!!!",
                                    R.drawable.logo2
                            ));
                        }
                    }
                    @Override
                    public void onCancelled (@NonNull DatabaseError error){
                        Log.d("Error", error.getMessage());
                    }
                }
        );
    }
}