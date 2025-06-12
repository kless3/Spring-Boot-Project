package com.harmyFounder.SpringBootProject.controller;

import com.harmyFounder.SpringBootProject.model.Habit;
import com.harmyFounder.SpringBootProject.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("habits")
public class HabitController {

    private HabitService habitService;

    @Autowired
    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping("/{userId}/tracker")
    public List<Habit> getHabitTracker(@PathVariable Long userId){
        return habitService.getHabitListByUser(userId);
    }

    @PostMapping("/{userId}/tracker")
    public Habit addHabitToTracker(@RequestBody Habit habit, @PathVariable Long userId){
        return habitService.addHabitToList(habit, userId);
    }

    @PostMapping("/{habitId}/tracker/status")
    public Habit changeHabitStatus(@PathVariable Long habitId){
        return habitService.changeHabitStatus(habitId);
    }

    @DeleteMapping("/{userId}/{id}")
    public void deleteHabit(@PathVariable Long userId, @PathVariable Long id){
        habitService.deleteHabitById(userId, id);
    }

}
