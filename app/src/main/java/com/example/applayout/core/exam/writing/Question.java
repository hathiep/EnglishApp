package com.example.applayout.core.exam.writing;

import java.util.ArrayList;
import java.util.HashMap;

public class Question {
    private String context;
    private ArrayList<String> answer;

    public Question() {
    }

    public Question(String context, ArrayList<String> answer) {
        this.context = context;
        this.answer = answer;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<String> answer) {
        this.answer = answer;
    }
}
