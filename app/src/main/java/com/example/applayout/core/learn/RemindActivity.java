package com.example.applayout.core.learn;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applayout.R;
import com.example.applayout.databinding.LearnActivityRemindBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class RemindActivity extends AppCompatActivity {
    private LearnActivityRemindBinding binding;
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.learn_activity_remind);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding = LearnActivityRemindBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createNotificationChannel();
        binding.selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Set Alarm Time")
                        .build();
                timePicker.show(getSupportFragmentManager(), "remind");
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(timePicker.getHour() >12){
                            binding.selectTime.setText(
                                String.format("%02d",(timePicker.getHour()-12)) +":"+ String.format("%02d", timePicker.getMinute())+"PM"
                            );
                        }else {
                            binding.selectTime.setText(timePicker.getHour()+":" + timePicker.getMinute()+ "AM");
                        }
                        calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                        calendar.set(Calendar.MINUTE, timePicker.getMinute());
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                    }
                });
            }
        });

        binding.startRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(RemindActivity.this, ReminderBroadcastReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(RemindActivity.this, 0,intent,PendingIntent.FLAG_IMMUTABLE);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),2 * 60 *1000,pendingIntent);
                Toast.makeText(RemindActivity.this, "Alarm set", Toast.LENGTH_SHORT).show();

            }
        });
        binding.cancelRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemindActivity.this, ReminderBroadcastReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(RemindActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                if(alarmManager == null){
                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                };
                alarmManager.cancel(pendingIntent);
                Toast.makeText(RemindActivity.this, "Alarm canceled", Toast.LENGTH_SHORT).show();
            }
        });




    }
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "remindChannel";
            String des = "This is a channel to remind";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("remind",name,importance);
            notificationChannel.setDescription(des);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }


}
