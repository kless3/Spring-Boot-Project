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
    public List<Note> getAllNotesByUser(@PathVariable Long id){
        return noteService.getAllNotesByUser(id);
    }

    @GetMapping("/{userId}/{id}")
    public Note getNoteById(@PathVariable("userId") Long userId, @PathVariable("id") Long id){
        return noteService.getNoteById(userId, id);
    }

    @PostMapping("/{userId}")
    public Note createNote(@RequestBody Note note, @PathVariable Long userId){
        return noteService.createNote(note, userId);
    }

    @DeleteMapping("/{userId}/{id}")
    public void deleteNoteById(@PathVariable("userId") Long userId, @PathVariable("id") Long id){
        noteService.deleteNoteById(userId, id);
    }

    @PutMapping("/{userId}/{id}")
    public ResponseEntity<?> updateNote(
            @PathVariable("userId") Long userId,
            @PathVariable("id") Long id,
            @RequestBody NoteUpdateDto updateDto) {
        noteService.updateNote(userId, id, updateDto.getTittle(), updateDto.getText());
        return ResponseEntity.ok().build();
    }
}
