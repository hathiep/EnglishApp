package com.example.applayout.core.support.Domains;

import com.example.applayout.core.main_class.Exam;
import com.example.applayout.core.support.Domains.ExerciseDomain;
import com.google.firebase.database.Exclude;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserDomain {
    private String id;
    private Exam exam;
    private List<Note> notes;
    private ExerciseDomain exercise;
    private List<String> newword;
    private String phone;

    public UserDomain() {
    }

    public UserDomain(
            String id,
            Exam exam,
            List<Note> notes,
            ExerciseDomain exercise,
            List<String> newword,
            String phone
    ) {
        this.id = id;
        this.exam = exam;
        this.notes = notes;
        this.exercise = exercise;
        this.newword = newword;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "UserDomain{" +
                "id='" + id + '\'' +
                ", exam=" + exam +
                ", notes=" + notes +
                ", exercise=" + exercise +
                ", newword=" + newword +
                ", phone='" + phone + '\'' +
                '}';
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    @Exclude
    public ExerciseDomain getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseDomain exercise) {
        this.exercise = exercise;
    }

    public List<String> getNewword() {
        return newword;
    }

    public void setNewword(List<String> newword) {
        this.newword = newword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public static class Note implements Comparable<Note> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        private LocalDateTime createdDateReal;
        private String id;
        private String name;
        private String createdDate;
        private String title;
        private String body;
        private String status;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        @Exclude
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
            this.createdDateReal = LocalDateTime.parse(createdDate, formatter);
        }
        @Exclude
        public LocalDateTime getCreatedDateReal() {
            return createdDateReal;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Note{" +
                    "id=" + id +
                    ", createdDate='" + createdDate + '\'' +
                    ", title='" + title + '\'' +
                    ", body='" + body + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }

        @Override
        public int compareTo(Note o) {
            return o.getCreatedDateReal().compareTo(this.getCreatedDateReal());
        }
    }

}
