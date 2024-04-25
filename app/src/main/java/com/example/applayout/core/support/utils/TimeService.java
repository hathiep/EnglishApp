package com.example.applayout.core.support.utils;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.applayout.R;

public class TimeService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Call the method to send notification
        sendNotification();

        // Return START_STICKY to restart the service if it is killed by the system
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // This method is used to send notification
    private void sendNotification() {
        NotificationHelper.showNotification(
                getApplicationContext(),
                "Dậy học Tiếng Anh thôi nào bạn ơi!!!",
                "Bắt đầu ngày mới bằng việc học tiếng ANH một cách hiệu quả",
                2002,
                R.drawable.notification
        );
        System.out.println("Notification sent");

        // Using Handler to recall the method after 5 seconds
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendNotification(); // Recall the method
            }
        }, 5000);
    }
}
