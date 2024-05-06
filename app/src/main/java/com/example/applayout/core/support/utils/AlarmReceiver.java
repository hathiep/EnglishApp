package com.example.applayout.core.support.utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.applayout.R;
import com.example.applayout.core.support.Domains.UserDomain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AlarmReceiver extends BroadcastReceiver {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");
    private List<UserDomain.Note> items = new ArrayList<>();
    @Override
    public void onReceive(Context context, Intent intent) {
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
                            if (count > 0) {
                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(2002, NotificationHelper.createNotification(
                                        context,
                                        "Hoàn thiện kế hoạch",
                                        "Bạn đang có " + count + " kế hoạch chưa hoàn thiện!!! Nhanh chóng vào hoàn thiện chúng nào!!!",
                                        R.drawable.logo2
                                ));
                                Log.d("Notification", "Notification created");
                            } else {
                                Log.d("Notification", "No notification");
                            }
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