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
import com.example.applayout.R;
import com.example.applayout.core.main.Exam;
import com.example.applayout.core.support.Domains.ExamDomain;
import com.example.applayout.core.support.Domains.ExerciseDomain;
import com.example.applayout.core.support.Domains.TaskDomain;
import com.example.applayout.core.support.Domains.WordDomain;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{
    private ArrayList<WordDomain> words;
    private ArrayList<ExamDomain> exams;
    private List<Pair<String, Integer>> tasks;
    private String taskType;

    public TaskAdapter() {
    }

    public TaskAdapter(List<Pair<String, Integer>> tasks, String taskType) {
        this.tasks = tasks;
        this.taskType = taskType;
    }

    public TaskAdapter(ArrayList<WordDomain> words, String taskType) {
        this.words = words;
        this.taskType = taskType;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        switch (taskType) {
            case "Learn":
                holder.subjectTxt.setText(words.get(position).getSubject());
                holder.subjectProgressTxt.setText(words.get(position).getTime());
                break;
            case "Exercise":
                System.out.println("Exercise");
                holder.subjectTxt.setText(tasks.get(position).first);
                System.out.println(tasks.get(position).first);
                holder.subjectProgressTxt.setText(tasks.get(position).second + "points");
                break;
            case "Exam":
                holder.subjectTxt.setText(exams.get(position).getTitle());
                holder.subjectProgressTxt.setText(exams.get(position).getScore() + "points");
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectImage = itemView.findViewById(R.id.subjectImage);
            subjectTxt = itemView.findViewById(R.id.subjectTxt);
            subjectProgressBar = itemView.findViewById(R.id.subjectProgressBar);
            subjectProgressTxt = itemView.findViewById(R.id.subjectProgressTxt);
        }
    }
}
