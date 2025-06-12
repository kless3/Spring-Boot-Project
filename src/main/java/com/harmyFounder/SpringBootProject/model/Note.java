package com.harmyFounder.SpringBootProject.model;

import jakarta.persistence.*;

@Entity
public class Note {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tittle;
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Note() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
