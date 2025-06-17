package com.harmyFounder.SpringBootProject.controller;

import com.harmyFounder.SpringBootProject.model.Habit;
import com.harmyFounder.SpringBootProject.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("habits")
public class HabitController {

    private final HabitService habitService;

    @Autowired
    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping("/{userId}/tracker")
    public ResponseEntity<?> getHabitTracker(@PathVariable Long userId) {
        try {
            List<Habit> habits = habitService.getHabitListByUser(userId);
            if (habits.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(habits);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{userId}/tracker")
    public ResponseEntity<?> addHabitToTracker(
            @RequestBody Habit habit,
            @PathVariable Long userId) {
        try {
            // Валидация
            if (habit.getTittle() == null || habit.getTittle().isEmpty()) {
                return ResponseEntity.badRequest().body("Habit title must not be empty");
            }
            return ResponseEntity.ok(habitService.addHabitToList(habit, userId));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{habitId}/tracker/status")
    public ResponseEntity<?> changeHabitStatus(@PathVariable Long habitId) {
        try {
            return ResponseEntity.ok(habitService.changeHabitStatus(habitId));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/{id}")
    public ResponseEntity<?> deleteHabit(
            @PathVariable Long userId,
            @PathVariable Long id) {
        try {
            habitService.deleteHabitById(userId, id);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
