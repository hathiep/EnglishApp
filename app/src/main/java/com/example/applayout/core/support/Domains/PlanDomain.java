package com.example.applayout.core.support.Domains;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PlanDomain implements Serializable {
    private int id;
    private String title;
    private String body;
    private LocalDateTime createdDate;
    private boolean isDone;

    public PlanDomain() {
    }

    public PlanDomain(int id, String title, String body, LocalDateTime createdDate, boolean isDone) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.createdDate = createdDate;
        this.isDone = isDone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @NonNull
    @Override
    public String toString() {
        return "Plan{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", createdDate=" + createdDate +
                ", isDone=" + isDone +
                '}';
    }
}
