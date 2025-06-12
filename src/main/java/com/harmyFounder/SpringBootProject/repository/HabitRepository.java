package com.harmyFounder.SpringBootProject.repository;

import com.harmyFounder.SpringBootProject.model.Habit;
import com.harmyFounder.SpringBootProject.model.Note;
import com.harmyFounder.SpringBootProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {

    Optional<List<Habit>> findAllByUser(User user);

}
