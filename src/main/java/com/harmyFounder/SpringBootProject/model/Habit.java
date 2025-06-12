package com.harmyFounder.SpringBootProject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String tittle;
    private boolean isDone;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Habit() {

    }
}
