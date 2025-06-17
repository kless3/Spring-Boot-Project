package com.harmyFounder.SpringBootProject.service;

import com.harmyFounder.SpringBootProject.model.Habit;
import com.harmyFounder.SpringBootProject.model.User;
import com.harmyFounder.SpringBootProject.repository.HabitRepository;
import com.harmyFounder.SpringBootProject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HabitRepository habitRepository;

    @InjectMocks
    private HabitService habitService;

    private User testUser;
    private Habit testHabit;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);

        testHabit = new Habit();
        testHabit.setId(1L);
        testHabit.setUser(testUser);
        testHabit.setDone(false);
    }

    @Test
    void getHabitListByUser_WhenUserExistsAndHasHabits_ReturnsHabits() {

        List<Habit> expectedHabits = Collections.singletonList(testHabit);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(habitRepository.findAllByUser(testUser)).thenReturn(Optional.of(expectedHabits));

        List<Habit> result = habitService.getHabitListByUser(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testHabit, result.get(0));
        verify(userRepository).findById(1L);
        verify(habitRepository).findAllByUser(testUser);
    }

    @Test
    void getHabitListByUser_WhenUserNotExists_ThrowsException() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> habitService.getHabitListByUser(1L));
        verify(userRepository).findById(1L);
        verifyNoInteractions(habitRepository);
    }

    @Test
    void getHabitListByUser_WhenUserHasNoHabits_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(habitRepository.findAllByUser(testUser)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> habitService.getHabitListByUser(1L));
        verify(userRepository).findById(1L);
        verify(habitRepository).findAllByUser(testUser);
    }

    @Test
    void addHabitToList_WhenUserExists_SavesAndReturnsHabit() {
        Habit newHabit = new Habit();


        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(habitRepository.save(any(Habit.class))).thenAnswer(invocation -> invocation.getArgument(0));


        Habit result = habitService.addHabitToList(newHabit, 1L);


        assertNotNull(result);
        assertFalse(result.isDone());
        assertEquals(testUser, result.getUser());
        verify(userRepository).findById(1L);
        verify(habitRepository).save(newHabit);
    }

    @Test
    void addHabitToList_WhenUserNotExists_ThrowsException() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(IllegalStateException.class, () -> habitService.addHabitToList(testHabit, 1L));
        verify(userRepository).findById(1L);
        verifyNoInteractions(habitRepository);
    }

    @Test
    void changeHabitStatus_WhenHabitExists_TogglesStatus() {

        when(habitRepository.findById(1L)).thenReturn(Optional.of(testHabit));
        when(habitRepository.save(any(Habit.class))).thenAnswer(invocation -> invocation.getArgument(0));


        Habit result = habitService.changeHabitStatus(1L);


        assertTrue(result.isDone());


        testHabit.setDone(true);
        when(habitRepository.findById(1L)).thenReturn(Optional.of(testHabit));

        result = habitService.changeHabitStatus(1L);

        assertFalse(result.isDone());
        verify(habitRepository, times(2)).findById(1L);
        verify(habitRepository, times(2)).save(any(Habit.class));
    }

    @Test
    void changeHabitStatus_WhenHabitNotExists_ThrowsException() {

        when(habitRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(IllegalStateException.class, () -> habitService.changeHabitStatus(1L));
        verify(habitRepository).findById(1L);
        verify(habitRepository, never()).save(any());
    }

    @Test
    void deleteHabitById_WhenUserAndHabitExist_DeletesHabit() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));


        habitService.deleteHabitById(1L, 1L);


        verify(userRepository).findById(1L);
        verify(habitRepository).deleteById(1L);
    }

    @Test
    void deleteHabitById_WhenUserNotExists_ThrowsException() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> habitService.deleteHabitById(1L, 1L));
        verify(userRepository).findById(1L);
        verifyNoInteractions(habitRepository);
    }
}