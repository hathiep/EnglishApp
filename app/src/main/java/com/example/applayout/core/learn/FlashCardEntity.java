package com.example.applayout.core.learn;

public class FlashCardEntity {
    private String example;
    private String image;
    private String meaning;
    private String sound;
    private String spell;
    private String word;

    public FlashCardEntity(String example, String image, String meaning, String sound, String spell, String word) {
        this.example = example;
        this.image = image;
        this.meaning = meaning;
        this.sound = sound;
        this.spell = spell;
        this.word = word;
    }

    public FlashCardEntity() {
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
