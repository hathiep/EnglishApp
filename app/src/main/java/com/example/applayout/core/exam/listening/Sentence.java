package com.example.applayout.core.exam.listening;

public class Sentence {
    private String context;
    private int status;

    public Sentence() {
    }

    public Sentence(String context, int status) {
        this.context = context;
        this.status = status;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
