package com.example.applayout.core.support.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applayout.R;
import com.example.applayout.core.support.Domains.PlanDomain;
import com.example.applayout.core.support.Domains.UserDomain;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    private ArrayList<UserDomain.Note> notes;
    private FirebaseUser user;

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

    @SuppressLint("SetTextI18n")
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

        // Set time of the note
        holder.datePlan.setText(
                "Ngày tạo: " +
                notes.get(position).getCreatedDate3()
        );

        // Set due date of the note
        holder.dueDatePlan.setText(
                "Tới hạn: " +
                notes.get(position).getDueDate2()
        );

        // Set the status of the note
        if(notes.get(position).getStatus().equals("active")) {
            holder.checkBox.setChecked(true);
            holder.checkBox.setEnabled(false);
            // Strike through the text
            holder.checkBox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = holder.cardViewPlan.getResources().getDrawable(
                    R.drawable.gradient_background_ischecked,
                    null
            );
            holder.cardViewPlan.setBackground(drawable);
        } else {
            holder.checkBox.setChecked(false);
        }

        // Update the status of the note
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Call the dialog to confirm the update
            if(isChecked) {
                showDialogActive(
                        buttonView,
                        position,
                        isChecked,
                        holder
                );
            }
        });

        // Delete the note
        holder.imageButton.setOnClickListener(v -> {
            // Call the dialog to confirm the deletion
            showDialogDelete(
                    v,
                    position,
                    holder
            );
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private ImageView imageButton;
        private TextView bodyPlan;
        private TextView datePlan;
        private TextView dueDatePlan;
        private ConstraintLayout cardViewPlan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewPlan = itemView.findViewById(R.id.cardview_plan);
            checkBox = itemView.findViewById(R.id.checkBox);
            imageButton = itemView.findViewById(R.id.deleteButton);
            bodyPlan = itemView.findViewById(R.id.bodyPlan);
            datePlan = itemView.findViewById(R.id.datePlan);
            dueDatePlan = itemView.findViewById(R.id.dueDatePlan);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showDialogActive(CompoundButton buttonView, int position, boolean isChecked, ViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(buttonView.getContext());

        // Set title and message
        builder.setTitle("Hoành thành kế hoạch");
        builder.setMessage("Bạn muốn xác nhận đã hoàn thành kế hoạch học tập này không");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Check if the note is active
            if (isChecked) {
                notes.get(position).setStatus("active");
            }
            // Update the status of the note
            mDataBase.child(user.getUid()).child("notes").child(notes.get(position).getId()).setValue(notes.get(position))
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Updated");
                        notifyDataSetChanged();
                        Toast.makeText(buttonView.getContext(), "Updated", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(
                            e -> Toast.makeText(buttonView.getContext(), "Failed to update", Toast.LENGTH_SHORT).show()
                    );
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing
            System.out.println("Do nothing!!!");
            holder.checkBox.setChecked(false);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showDialogDelete(View view, int position, ViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        // Set title and message
        builder.setTitle("Xóa kế hoạch");
        builder.setMessage("Bạn muốn xóa kế hoạch học tập này không");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Delete the note
            mDataBase.child(user.getUid()).child("notes").child(notes.get(position).getId()).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Deleted");
                        notes.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(
                            e -> Toast.makeText(view.getContext(), "Failed to delete", Toast.LENGTH_SHORT).show()
                    );
            Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing
            System.out.println("Do nothing!!!");
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
