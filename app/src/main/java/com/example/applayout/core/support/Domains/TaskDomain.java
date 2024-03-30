package com.example.applayout.core.support.Domains;

import java.io.Serializable;

public class TaskDomain implements Serializable {
    private int id;
    private String subject;
    private String description;
    private int progress;
    private String imgPath;

    public TaskDomain() {
    }

    public TaskDomain(int id, String subject, String description, int progress, String imgPath) {
        this.id = id;
        this.subject = subject;
        this.description = description;
        this.progress = progress;
        this.imgPath = imgPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    public String toString() {
        return "TaskDomain{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", progress=" + progress +
                ", imgPath='" + imgPath + '\'' +
                '}';
    }
}
