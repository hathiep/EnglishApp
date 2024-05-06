package com.example.applayout.core.learn;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.applayout.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LearnFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class LearnFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    LinearLayout learnFragLayout;

    public LearnFragment() {
        // Required empty public constructor
    }

    public static LearnFragment newInstance() {
        return new LearnFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = firebaseDatabase.getReference("NewWords");



        View view = inflater.inflate(R.layout.learn_fragment, container, false);
        learnFragLayout = view.findViewById(R.id.learn_frag_layout);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot topicSnapshot : dataSnapshot.getChildren()) {
                    String topicName = topicSnapshot.getKey();
                    createButton(topicName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });

        return view;
    }

    private void createButton(String topicName) {
        if (learnFragLayout != null) {
            Button button = new Button(requireContext());
            button.setText(topicName);
            button.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uid = firebaseAuth.getUid();
                    System.out.println(uid);
                    if (uid != null) {
                        DatabaseReference userReference = firebaseDatabase.getReference("User")
                                .child(uid)
                                .child("newword")
                                .child(topicName); // Sử dụng topic name làm key
                        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                        userReference.setValue(currentDate); // Đặt giá trị là ngày hiện tại
                    }

                    Intent intent = new Intent(requireContext(), FlashCardActivity.class);
                    intent.putExtra("topicName", topicName);
                    startActivity(intent);
                }
            });
            learnFragLayout.addView(button);
        }
    }
}
