package com.example.applayout.core.support.utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.applayout.R;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Reschedule the task using WorkManager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(2002, NotificationHelper.createNotification(
                context,
                "Kiêm tra kế hoạch",
                "Một vài kế hoạch bạn có thể bỏ lỡ",
                R.drawable.logo2
        ));
    }
}