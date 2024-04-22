package com.example.applayout.core.learn;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.applayout.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewWordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewWordFragment extends Fragment {

    public ReviewWordFragment() {
        // Required empty public constructor
    }

    // Không cần tham số trong phương thức newInstance
    public static ReviewWordFragment newInstance() {
        return new ReviewWordFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.learn_fragment_review_word, container, false);

        Button learnButton = view.findViewById(R.id.learn_button);

        learnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),FlashCardActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}