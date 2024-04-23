package com.example.applayout.core.support.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.applayout.R;
import com.example.applayout.core.support.Activities.TrackerSupport;

public class NotificationHelper {

    private static final String CHANNEL_ID = "Notification_2002";
    private static final String CHANNEL_NAME = "Notification Channel";
    private static final String CHANNEL_DESC = "Receive updates and alerts from My App";

    public static void showNotification(Context context, String title, String body, int notificationId, String imageUrl) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel for Android Oreo and above
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription(CHANNEL_DESC);
        notificationManager.createNotificationChannel(channel);

        // Create an Intent for the activity you want to open when the notification is clicked
        Intent intent = new Intent(context, TrackerSupport.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Load image from URL if available
        Bitmap bitmap = null;
        if (imageUrl != null && !imageUrl.isEmpty()) {
            bitmap = BitmapFactory.decodeFile(imageUrl);
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_email)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true); // Dismiss the notification when clicked

        // Set large icon if available
        if (bitmap != null) {
            builder.setLargeIcon(bitmap);
        }

        // Set style for big picture
        if (bitmap != null) {
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
            bigPictureStyle.bigPicture(bitmap);
            bigPictureStyle.setBigContentTitle(title);
            bigPictureStyle.setSummaryText(body);
            builder.setStyle(bigPictureStyle);
        }

        // Issue the notification
        notificationManager.notify(notificationId, builder.build());
    }

    public static void cancelNotification(Context applicationContext, int i) {
        NotificationManager notificationManager =
                (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(i);
    }
}