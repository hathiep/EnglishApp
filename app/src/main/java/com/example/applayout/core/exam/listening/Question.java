package com.example.applayout.core.exam.listening;

public class Question {
    private String answer1, answer2, answer3, answer4, image, audio;
    private int correct_ans;

    public Question() {
    }

    public Question(String answer1, String answer2, String answer3, String answer4, String image, String audio, int correct_ans) {
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.image = image;
        this.audio = audio;
        this.correct_ans = correct_ans;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public int getCorrect_ans() {
        return correct_ans;
    }

    public void setCorrect_ans(int correct_ans) {
        this.correct_ans = correct_ans;
    }
}
