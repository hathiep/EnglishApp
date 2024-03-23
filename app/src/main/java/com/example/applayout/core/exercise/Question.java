package com.example.applayout.core.exercise;

import android.media.MediaPlayer;

import java.util.List;

public class Question {
    private int number;
    private String content;

    private MediaPlayer voice;
    private List<Answer> listAnswer;

    public Question(int number, String content, MediaPlayer voice, List<Answer> listAnswer) {
        this.number = number;
        this.content = content;
        this.voice = voice;
        this.listAnswer = listAnswer;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MediaPlayer getVoice() {
        return voice;
    }

    public void setVoice(MediaPlayer voice) {
        this.voice = voice;
    }

    public List<Answer> getListAnswer() {
        return listAnswer;
    }

    public void setListAnswer(List<Answer> listAnswer) {
        this.listAnswer = listAnswer;
    }
}
