package com.harmyFounder.SpringBootProject.service;

import com.harmyFounder.SpringBootProject.model.Habit;
import com.harmyFounder.SpringBootProject.model.Note;
import com.harmyFounder.SpringBootProject.model.User;
import com.harmyFounder.SpringBootProject.repository.HabitRepository;
import com.harmyFounder.SpringBootProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HabitService {

    private UserRepository userRepository;

    private HabitRepository habitRepository;

    @Autowired
    public HabitService(UserRepository userRepository, HabitRepository habitRepository) {
        this.userRepository = userRepository;
        this.habitRepository = habitRepository;
    }


    public List<Habit> getHabitListByUser(Long id) {

        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalStateException("User not found");
        }

        Optional<List<Habit>> notes = habitRepository.findAllByUser(user.get());
        if (notes.isEmpty()) {
            throw new IllegalStateException("Notes not found");
        }
        return notes.get();
    }


    public Habit addHabitToList(Habit habit, Long id){

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()){
            throw new IllegalStateException("User with this id is not exists!");
        }

        habit.setDone(false);
        habit.setUser(userOptional.get());

        return habitRepository.save(habit);
    }


    public Habit changeHabitStatus(Long id){

        Optional<Habit> habitOptional = habitRepository.findById(id);
        if(habitOptional.isEmpty()){
            throw new IllegalStateException("Habit not found");
        }
        Habit habit = habitOptional.get();
        habit.setDone(!habit.isDone());

        return habitRepository.save(habit);
    }

    public void deleteHabitById(Long userId, Long id){
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()){
            throw new IllegalStateException("User with this id is not exists!");
        }

        habitRepository.deleteById(id);
    }

}