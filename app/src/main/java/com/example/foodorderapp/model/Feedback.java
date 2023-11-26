package com.example.foodorderapp.model;

public class Feedback {
    private String email;
    private String comment;
    public Feedback() {
    }

    public Feedback(String email, String comment) {
        this.email = email;
        this.comment = comment;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
