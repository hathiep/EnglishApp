package com.example.applayout.core.support.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applayout.R;
import com.example.applayout.core.support.Domains.PlanDomain;
import com.example.applayout.core.support.Domains.UserDomain;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    private final ArrayList<UserDomain.Note> notes;
    private final FirebaseUser user;

    DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference("User");

    public PlanAdapter(ArrayList<UserDomain.Note> notes, FirebaseUser user) {
        this.notes = notes;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(
                parent.getContext()
        ).inflate(
                R.layout.viewholder_plan,
                parent,
                false
        );
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set the title of the note
        holder.checkBox.setText(
                notes.get(position).getTitle()
        );

        // Set the body of the note
        holder.bodyPlan.setText(
                notes.get(position).getBody()
        );

        // Set the status of the note
        if(notes.get(position).getStatus().equals("active")) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        // Update the status of the note
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                notes.get(position).setStatus("active");
            } else {
                notes.get(position).setStatus("inactive");
            }
            mDataBase.child(user.getUid()).child("notes").child(notes.get(position).getId()).setValue(notes.get(position))
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Updated");
                        Toast.makeText(buttonView.getContext(), "Updated", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(
                            e -> Toast.makeText(buttonView.getContext(), "Failed to update", Toast.LENGTH_SHORT).show()
                    );
        });

        // Delete the note
        holder.imageButton.setOnClickListener(v -> {
            mDataBase.child(user.getUid()).child("notes").child(notes.get(position).getId()).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Deleted");
                        notes.remove(position);
                        Toast.makeText(v.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(
                            e -> Toast.makeText(v.getContext(), "Failed to delete", Toast.LENGTH_SHORT).show()
                    );

            Toast.makeText(v.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkBox;
        private final ImageView imageButton;
        private final TextView bodyPlan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            imageButton = itemView.findViewById(R.id.deleteButton);
            bodyPlan = itemView.findViewById(R.id.bodyPlan);
        }

    }
}
