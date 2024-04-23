package com.example.applayout.core.main_class;

public class Exam {
    private int vocabulary;
    private int grammar;
    private int listening;
    private int writing;
    private int synthetic;

    public Exam() {
    }

    public Exam(int vocabulary, int grammar, int listening, int writing, int synthetic) {
        this.vocabulary = vocabulary;
        this.grammar = grammar;
        this.listening = listening;
        this.writing = writing;
        this.synthetic = synthetic;
    }

    public int getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(int vocabulary) {
        this.vocabulary = vocabulary;
    }

    public int getGrammar() {
        return grammar;
    }

    public void setGrammar(int grammar) {
        this.grammar = grammar;
    }

    public int getListening() {
        return listening;
    }

    public void setListening(int listening) {
        this.listening = listening;
    }

    public int getWriting() {
        return writing;
    }

    public void setWriting(int writing) {
        this.writing = writing;
    }

    public int getSynthetic() {
        return synthetic;
    }

    public void setSynthetic(int synthetic) {
        this.synthetic = synthetic;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "vocabulary=" + vocabulary +
                ", grammar=" + grammar +
                ", listening=" + listening +
                ", writing=" + writing +
                ", synthetic=" + synthetic +
                '}';
    }
}
