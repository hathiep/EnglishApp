package com.example.applayout.core.learn;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.applayout.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewWordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewWordFragment extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    LinearLayout reviewFragLayout;
    public ReviewWordFragment() {
    }

    public static ReviewWordFragment newInstance() {
        return new ReviewWordFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.learn_fragment_review_word, container, false);
        

        return view;
    }
}