package com.harmyFounder.SpringBootProject.controller;

import com.harmyFounder.SpringBootProject.dto.NoteUpdateDto;
import com.harmyFounder.SpringBootProject.model.Note;
import com.harmyFounder.SpringBootProject.service.NoteService;
import com.harmyFounder.SpringBootProject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllNotesByUser(@PathVariable Long id) {
        try {
            return noteService.getAllNotesByUser(id);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/{id}")
    public ResponseEntity<?> getNoteById(
            @PathVariable("userId") Long userId,
            @PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(noteService.getNoteById(userId, id));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> createNote(
            @RequestBody Note note,
            @PathVariable Long userId) {
        try {
            if (note.getTittle() == null || note.getTittle().isEmpty() ||
                    note.getText() == null || note.getText().isEmpty()) {
                return ResponseEntity.badRequest().body("Title and text must not be empty");
            }
            return ResponseEntity.ok(noteService.createNote(note, userId));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/{id}")
    public ResponseEntity<?> deleteNoteById(
            @PathVariable("userId") Long userId,
            @PathVariable("id") Long id) {
        try {
            noteService.deleteNoteById(userId, id);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{userId}/{id}")
    public ResponseEntity<?> updateNote(
            @PathVariable("userId") Long userId,
            @PathVariable("id") Long id,
            @RequestBody NoteUpdateDto updateDto) {
        try {
            noteService.updateNote(userId, id, updateDto.getTittle(), updateDto.getText());
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
