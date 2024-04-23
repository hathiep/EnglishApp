package com.example.applayout.core.learn;

import com.google.gson.annotations.SerializedName;

public class TextToSpeechResponse {
    @SerializedName("consumedCredits")
    private int consumedCredits;

    @SerializedName("fileDownloadUrl")
    private String fileDownloadUrl;

    public TextToSpeechResponse() {
    }

    public TextToSpeechResponse(int consumedCredits, String fileDownloadUrl) {
        this.consumedCredits = consumedCredits;
        this.fileDownloadUrl = fileDownloadUrl;
    }

    public int getConsumedCredits() {
        return consumedCredits;
    }

    public void setConsumedCredits(int consumedCredits) {
        this.consumedCredits = consumedCredits;
    }

    public String getFileDownloadUrl() {
        return fileDownloadUrl;
    }

    public void setFileDownloadUrl(String fileDownloadUrl) {
        this.fileDownloadUrl = fileDownloadUrl;
    }
}
