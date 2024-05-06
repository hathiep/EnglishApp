package com.example.applayout.core.learn;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.applayout.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    String topics ;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    @Override
    public void onReceive(Context context, Intent intent) {
        topics = "Các chủ đề cần ôn lại: ";

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        String userKey = firebaseAuth.getUid();
        databaseReference = firebaseDatabase.getReference("User/" + userKey + "/newword");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot topicSnapshot : dataSnapshot.getChildren()) {
                    String topicName = topicSnapshot.getKey();
                    String date = topicSnapshot.getValue(String.class);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    long diffInDays = 0;
                    try {
                        Date date1 = sdf.parse(date);
                        Date currentDate = new Date(); //

                        long diffInMillis = currentDate.getTime() - date1.getTime();

                        // Chuyển đổi số mili giây thành số ngày
                        diffInDays = diffInMillis / (1000 * 60 * 60 * 24);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(diffInDays % 3 == 0){
                        topics += topicName +", ";
                    }
                }
                if(topics.equals("Các chủ đề cần ôn lại:")){
                    topics = "Cùng học từ mới nhé";
                }
                // Gửi thông báo sau khi lấy xong dữ liệu từ Firebase
                sendNotification(context, topics);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    private void sendNotification(Context context, String topics) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Tạo một NotificationChannel
            NotificationChannel channel = new NotificationChannel(
                    "remind",
                    "remind",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            // Lấy NotificationManager
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // Đăng ký channel với NotificationManager
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "remind")
                .setSmallIcon(R.drawable.icon_learn)
                .setContentTitle("Bạn hãy ôn lại từ mới nhé!!!")
                .setContentText(topics)
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
