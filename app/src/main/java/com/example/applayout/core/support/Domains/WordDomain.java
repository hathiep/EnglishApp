package com.example.applayout.core.support.Domains;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WordDomain {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private LocalDateTime date;
    private String subject;
    private String time;

    public WordDomain(String subject, String time) {
        this.subject = subject;
        this.time = time;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        date = LocalDateTime.parse(time, formatter);
    }

    @Override
    public String toString() {
        return "WordDomain{" +
                "subject='" + subject + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
