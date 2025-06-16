package com.harmyFounder.SpringBootProject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;



    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }



    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Note> notes;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Habit> habits;


    public User() {
    }

    public User(Long id, String name, String email, LocalDate dateOfBirth, Integer age) {
        this.id = id;
        this.username = name;
        this.email = email;

    }


}
