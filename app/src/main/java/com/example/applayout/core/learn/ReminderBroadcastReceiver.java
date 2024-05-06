package com.example.applayout.core.learn;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.applayout.R;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Tạo một NotificationChannel
            NotificationChannel channel = new NotificationChannel(
                    "remind",
                    "remind",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            // Tùy chỉnh cài đặt khác nếu cần
            // channel.setDescription("Description");
            // channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            // Lấy NotificationManager
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // Đăng ký channel với NotificationManager
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "remind")
                .setSmallIcon(R.drawable.icon_learn)
                .setContentTitle("Thông báo")
                .setContentText("Nội dung thông báo")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Hiển thị thông báo
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // Gửi thông báo
        notificationManager.notify(0, builder.build());

    }
    /*private void sendNotification() {
        // Xây dựng thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NotificationWorker.CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_learn)
                .setContentTitle("Thông báo")
                .setContentText("Nội dung thông báo")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Hiển thị thông báo
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        // Gửi thông báo
        notificationManager.notify(0, builder.build());
    }

    // Hàm tạo NotificationChannel
    private void createNotificationChannel() {
        // Chỉ cần tạo channel trên Android 8.0 trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Tạo một NotificationChannel
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            // Tùy chỉnh cài đặt khác nếu cần
            // channel.setDescription("Description");
            // channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            // Lấy NotificationManager
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            // Đăng ký channel với NotificationManager
            notificationManager.createNotificationChannel(channel);
        }
    }*/
}
