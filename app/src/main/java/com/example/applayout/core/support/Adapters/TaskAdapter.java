package com.example.applayout.core.support.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.learning_app.Domains.TaskDomain;
import com.example.learning_app.R;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{
    private ArrayList<TaskDomain> items;

    public TaskAdapter(ArrayList<TaskDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.viewholder_task,
                parent,
                false
        );
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.subjectTxt.setText(items.get(position).getSubject());
        holder.subjectProgressTxt.setText(items.get(position).getProgress() + "%");
        holder.subjectProgressBar.setProgress(items.get(position).getProgress());
        int drawableResId = holder.itemView.getResources().getIdentifier(
                items.get(position).getImgPath(),
                "drawable",
                holder.itemView.getContext().getOpPackageName()
        );
        Glide.with(holder.itemView.getContext())
                .load(drawableResId)
                .into(holder.subjectImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView subjectImage;
        private TextView subjectTxt;
        private ProgressBar subjectProgressBar;
        private TextView subjectProgressTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectImage = itemView.findViewById(R.id.subjectImage);
            subjectTxt = itemView.findViewById(R.id.subjectTxt);
            subjectProgressBar = itemView.findViewById(R.id.subjectProgressBar);
            subjectProgressTxt = itemView.findViewById(R.id.subjectProgressTxt);
        }
    }
}
