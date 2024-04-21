package com.example.applayout.core.learn;

public class TextToSpeechRequestBody {
    private String sentence;
    private String language;
    private String voice;
    private String engine;
    private boolean withSpeechMarks;

    public TextToSpeechRequestBody(String sentence, String language, String voice, String engine, boolean withSpeechMarks) {
        this.sentence = sentence;
        this.language = language;
        this.voice = voice;
        this.engine = engine;
        this.withSpeechMarks = withSpeechMarks;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public boolean isWithSpeechMarks() {
        return withSpeechMarks;
    }

    public void setWithSpeechMarks(boolean withSpeechMarks) {
        this.withSpeechMarks = withSpeechMarks;
    }
}