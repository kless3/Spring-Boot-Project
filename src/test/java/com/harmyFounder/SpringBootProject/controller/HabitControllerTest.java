package com.harmyFounder.SpringBootProject.controller;

import com.harmyFounder.SpringBootProject.model.Habit;
import com.harmyFounder.SpringBootProject.service.HabitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(MockitoExtension.class)
class HabitControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HabitService habitService;

    @InjectMocks
    private HabitController habitController;

    private Habit testHabit;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(habitController).build();

        testHabit = new Habit();
        testHabit.setId(1L);
        testHabit.setTittle("Test Habit");
        testHabit.setDone(false);
    }

    @Test
    void getHabitTracker_ShouldReturnHabits() throws Exception {

        when(habitService.getHabitListByUser(anyLong()))
                .thenReturn(List.of(testHabit));


        mockMvc.perform(get("/habits/1/tracker"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].tittle").value("Test Habit"));
    }

    @Test
    void getHabitTracker_WhenNoHabits_ShouldReturnNoContent() throws Exception {

        when(habitService.getHabitListByUser(anyLong()))
                .thenReturn(Collections.emptyList());


        mockMvc.perform(get("/habits/1/tracker"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getHabitTracker_WhenUserNotFound_ShouldReturnBadRequest() throws Exception {

        when(habitService.getHabitListByUser(anyLong()))
                .thenThrow(new IllegalStateException("User not found"));


        mockMvc.perform(get("/habits/999/tracker"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

    @Test
    void addHabitToTracker_ShouldAddHabit() throws Exception {

        when(habitService.addHabitToList(any(Habit.class), anyLong()))
                .thenReturn(testHabit);


        mockMvc.perform(post("/habits/1/tracker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tittle\":\"Test Habit\",\"done\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tittle").value("Test Habit"));
    }

    @Test
    void addHabitToTracker_WithEmptyTitle_ShouldReturnBadRequest() throws Exception {

        mockMvc.perform(post("/habits/1/tracker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tittle\":\"\",\"done\":false}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Habit title must not be empty"));
    }

    @Test
    void changeHabitStatus_ShouldChangeStatus() throws Exception {

        Habit updatedHabit = new Habit();
        updatedHabit.setId(1L);
        updatedHabit.setTittle("Test Habit");
        updatedHabit.setDone(true);

        when(habitService.changeHabitStatus(anyLong()))
                .thenReturn(updatedHabit);


        mockMvc.perform(post("/habits/1/tracker/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void deleteHabit_ShouldDeleteHabit() throws Exception {

        mockMvc.perform(delete("/habits/1/1"))
                .andExpect(status().isOk());

        verify(habitService).deleteHabitById(1L, 1L);
    }

    @Test
    void deleteHabit_WhenHabitNotFound_ShouldReturnBadRequest() throws Exception {

        doThrow(new IllegalStateException("Habit not found"))
                .when(habitService).deleteHabitById(anyLong(), anyLong());


        mockMvc.perform(delete("/habits/1/999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Habit not found"));
    }
}