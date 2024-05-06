package com.example.applayout.core.learn;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.applayout.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
                    createCardView(topicName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });

        return view;
    }

    private void createCardView(String topicName) {
        if (learnFragLayout != null) {
            DatabaseReference imgReference = firebaseDatabase.getReference("NW_IMG");
            imgReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Lấy link ảnh từ dataSnapshot
                    String imageUrl = snapshot.child(topicName).getValue(String.class);
                    if (imageUrl != null) {
                        // Tạo card view
                        CardView cardView = new CardView(requireContext());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutParams.setMargins(16, 16, 16, 16);
                        cardView.setLayoutParams(layoutParams);
                        cardView.setRadius(8);
                        cardView.setCardElevation(8);

                        // Tạo layout chứa ảnh và text
                        LinearLayout layout = new LinearLayout(requireContext());
                        layout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        layout.setPadding(16, 16, 16, 16);

                        // Tạo ImageView để hiển thị ảnh
                        ImageView imageView = new ImageView(requireContext());
                        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                                200,
                                200
                        );
                        imageView.setLayoutParams(imageParams);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        Picasso.get().load(imageUrl).resize(200, 200).centerCrop().into(imageView); // Load ảnh từ URL và giữ kích thước 200x200

                        // Thêm ImageView vào layout
                        layout.addView(imageView);

                        // Tạo text view để hiển thị topic name
                        TextView textView = new TextView(requireContext());
                        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        textParams.setMargins(16, 0, 0, 0);
                        textView.setLayoutParams(textParams);
                        textView.setText(topicName);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        textView.setTextColor(Color.BLACK);

                        // Thêm text view vào layout
                        layout.addView(textView);

                        // Set OnClickListener cho card view
                        cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Xử lý khi card view được nhấn, tương tự như trước
                                String uid = firebaseAuth.getUid();
                                if (uid != null) {
                                    DatabaseReference userReference = firebaseDatabase.getReference("User")
                                            .child(uid)
                                            .child("newword")
                                            .child(topicName); // Sử dụng topic name làm key
                                    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (!dataSnapshot.exists()) {
                                                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                                userReference.setValue(currentDate);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Xử lý lỗi nếu có
                                        }
                                    });
                                }

                                Intent intent = new Intent(requireContext(), FlashCardActivity.class);
                                intent.putExtra("topicName", topicName);
                                startActivity(intent);
                            }
                        });

                        // Thêm layout vào card view
                        cardView.addView(layout);

                        // Thêm card view vào layout chính
                        learnFragLayout.addView(cardView);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý khi có lỗi
                }
            });
        }
    }




}
