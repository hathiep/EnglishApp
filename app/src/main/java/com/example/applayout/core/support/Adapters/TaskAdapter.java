package com.example.applayout.core.support.Adapters;

import android.annotation.SuppressLint;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.applayout.R;
import com.example.applayout.core.support.Domains.ExamDomain;
import com.example.applayout.core.support.Domains.ExerciseDomain;
import com.example.applayout.core.support.Domains.TaskDomain;
import com.example.applayout.core.support.Domains.WordDomain;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{
    private ArrayList<WordDomain> words;
    private ArrayList<ExamDomain> exams;
    private List<Pair<String, Float>> tasks;
    private String taskType;

    private List<String> resources;

    public TaskAdapter() {
    }

    public TaskAdapter(List<Pair<String, Float>> tasks, String taskType) {
        this.tasks = tasks;
        this.taskType = taskType;
    }

    public TaskAdapter(ArrayList<WordDomain> words, String taskType) {
        this.words = words;
        this.taskType = taskType;
    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    public ArrayList<ExamDomain> getExams() {
        return exams;
    }

    public void setExams(ArrayList<ExamDomain> exams) {
        this.exams = exams;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
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

    @SuppressLint({"SetTextI18n", "DiscouragedApi"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Declare drawableResId for image resource
        int drawableResId;
        switch (taskType) {
            case "Learn":
                holder.subjectTxt.setText(words.get(position).getSubject());
                holder.subjectProgressTxt.setVisibility(View.GONE);
                holder.subjectProgressBar.setVisibility(View.GONE);
                holder.timeWord.setText("Completion date: " + words.get(position).getTime());


                drawableResId = holder.itemView.getResources().getIdentifier(
                        resources.get(position),
                        "drawable",
                        holder.itemView.getContext().getOpPackageName()
                );

                Glide.with(holder.itemView.getContext()).load(drawableResId).transform(
                        new CenterCrop(),
                        new GranularRoundedCorners(40, 40, 40, 40)
                ).into(holder.subjectImage);
                break;

            case "Exercise":
                System.out.println("Exercise");
                holder.subjectTxt.setText(tasks.get(position).first);
                System.out.println(tasks.get(position).first);
                holder.subjectProgressTxt.setText(tasks.get(position).second + "%");
                holder.subjectProgressBar.setProgress((int) (tasks.get(position).second * 1));
                holder.subjectProgressBar.setScaleY(3f);
                holder.timeWord.setVisibility(View.GONE);

                drawableResId = holder.itemView.getResources().getIdentifier(
                        resources.get(position),
                        "drawable",
                        holder.itemView.getContext().getOpPackageName()
                );

                Glide.with(holder.itemView.getContext()).load(drawableResId).transform(
                        new CenterCrop(),
                        new GranularRoundedCorners(40, 40, 40, 40)
                ).into(holder.subjectImage);
                break;

            case "Exam":
                String title = Character.toUpperCase(exams.get(position).getTitle().charAt(0))
                        + exams.get(position).getTitle().substring(1);
                holder.subjectTxt.setText(title);
                holder.subjectProgressTxt.setText(exams.get(position).getScore() + "");
                holder.subjectProgressBar.setProgress(exams.get(position).getScore() * 10);
                holder.subjectProgressBar.setScaleY(3f);
                holder.timeWord.setVisibility(View.GONE);

                drawableResId = holder.itemView.getResources().getIdentifier(
                        resources.get(position),
                        "drawable",
                        holder.itemView.getContext().getOpPackageName()
                );

                Glide.with(holder.itemView.getContext()).load(drawableResId).transform(
                        new CenterCrop(),
                        new GranularRoundedCorners(40, 40, 40, 40)
                ).into(holder.subjectImage);
                break;

        }

    }

    @Override
    public int getItemCount() {
        if (taskType.equals("Exercise")) {
            return tasks.size();
        } else if (taskType.equals("Learn")) {
            return words.size();
        } else {
            return exams.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView subjectImage;
        private TextView subjectTxt;
        private ProgressBar subjectProgressBar;
        private TextView subjectProgressTxt;
        private TextView timeWord;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectImage = itemView.findViewById(R.id.subjectImage);
            subjectTxt = itemView.findViewById(R.id.subjectTxt);
            subjectProgressBar = itemView.findViewById(R.id.subjectProgressBar);
            timeWord = itemView.findViewById(R.id.timeWord);
            subjectProgressTxt = itemView.findViewById(R.id.subjectProgressTxt);
        }
    }
}
