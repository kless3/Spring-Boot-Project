package com.harmyFounder.SpringBootProject.controller;

import com.harmyFounder.SpringBootProject.dto.NoteUpdateDto;
import com.harmyFounder.SpringBootProject.model.Note;
import com.harmyFounder.SpringBootProject.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NoteService noteService;

    @InjectMocks
    private NoteController noteController;

    private Note testNote;
    private NoteUpdateDto updateDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(noteController).build();

        testNote = new Note();
        testNote.setId(1L);
        testNote.setTittle("Test Title");
        testNote.setText("Test Content");

        updateDto = new NoteUpdateDto();
        updateDto.setTittle("Updated Title");
        updateDto.setText("Updated Content");
    }

    @Test
    void getNoteById_ShouldReturnNote() throws Exception {

        when(noteService.getNoteById(anyLong(), anyLong())).thenReturn(testNote);


        mockMvc.perform(get("/api/notes/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tittle").value("Test Title"))
                .andExpect(jsonPath("$.text").value("Test Content"));

        verify(noteService).getNoteById(1L, 1L);
    }

    @Test
    void createNote_ShouldCreateAndReturnNote() throws Exception {

        when(noteService.createNote(any(Note.class), anyLong())).thenReturn(testNote);


        mockMvc.perform(post("/api/notes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tittle\":\"Test Title\",\"text\":\"Test Content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tittle").value("Test Title"))
                .andExpect(jsonPath("$.text").value("Test Content"));

        verify(noteService).createNote(any(Note.class), eq(1L));
    }

    @Test
    void deleteNoteById_ShouldReturnOk() throws Exception {

        mockMvc.perform(delete("/api/notes/1/1"))
                .andExpect(status().isOk());

        verify(noteService).deleteNoteById(1L, 1L);
    }

    @Test
    void updateNote_ShouldUpdateAndReturnOk() throws Exception {

        mockMvc.perform(put("/api/notes/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tittle\":\"Updated Title\",\"text\":\"Updated Content\"}"))
                .andExpect(status().isOk());

        verify(noteService).updateNote(eq(1L), eq(1L), eq("Updated Title"), eq("Updated Content"));
    }

    @Test
    void getAllNotesByUser_WhenServiceThrowsException_ShouldReturnBadRequest() throws Exception {

        when(noteService.getAllNotesByUser(anyLong()))
                .thenThrow(new IllegalStateException("User not found"));


        mockMvc.perform(get("/api/notes/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

    @Test
    void getNoteById_WhenServiceThrowsException_ShouldReturnBadRequest() throws Exception {

        when(noteService.getNoteById(anyLong(), anyLong()))
                .thenThrow(new IllegalStateException("Note not found"));


        mockMvc.perform(get("/api/notes/1/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Note not found"));
    }

    @Test
    void createNote_WithInvalidInput_ShouldReturnBadRequest() throws Exception {

        mockMvc.perform(post("/api/notes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tittle\":\"\",\"text\":\"Test Content\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Title and text must not be empty"));


        mockMvc.perform(post("/api/notes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tittle\":\"Test Title\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Title and text must not be empty"));
    }
}