package com.example.applayout.core.support.Domains;

public class ExamDomain {
    private String title;
    private int score;

    public ExamDomain() {
    }

    public ExamDomain(String title, int score) {
        this.title = title;
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "ExamDomain{" +
                "title='" + title + '\'' +
                ", score=" + score +
                '}';
    }
}
