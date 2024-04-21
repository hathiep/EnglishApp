package com.example.applayout.core.support.Domains;

import com.google.firebase.database.Exclude;

import java.util.List;

public class UserDomain {
    private String id;
    private String username;
    private List<Note> notes;

    public UserDomain() {
    }

    public UserDomain(String username, List<Note> notes) {
        this.username = username;
        this.notes = notes;
    }

    public UserDomain(String id, String username, List<Note> notes) {
        this.id = id;
        this.username = username;
        this.notes = notes;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public static class Note {
        private String id;
        private String createdDate;
        private String title;
        private String body;
        private String status;

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
    }

    @Override
    public String toString() {
        return "UserDomain{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", notes=" + notes +
                '}';
    }
}
