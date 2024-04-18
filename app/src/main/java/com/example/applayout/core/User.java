package com.example.applayout.core;

public class User {
    private String email;
    private String exam;
    private String exercise;
    private String newword;

    public User() {
    }

    public User(String email, String exam, String exercise, String newword) {
        this.email = email;
        this.exam = exam;
        this.exercise = exercise;
        this.newword = newword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getNewword() {
        return newword;
    }

    public void setNewword(String newword) {
        this.newword = newword;
    }
}
