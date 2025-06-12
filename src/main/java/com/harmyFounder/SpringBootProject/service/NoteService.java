package com.harmyFounder.SpringBootProject.service;

import com.harmyFounder.SpringBootProject.model.Note;
import com.harmyFounder.SpringBootProject.model.User;
import com.harmyFounder.SpringBootProject.repository.NoteRepository;
import com.harmyFounder.SpringBootProject.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, UserService userService, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }


    public List<Note> getAllNotesByUser(Long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new IllegalStateException("User not found");
        }

        Optional<List<Note>> notes = noteRepository.findAllByUser(user.get());
        if (notes.isEmpty()){
            throw new IllegalStateException("Notes not found");
        }
        return notes.get();
    }


    public Note getNoteById(Long userId, Long id) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new IllegalStateException("User not found");
        }

        Optional<Note> note = noteRepository.findById(id);
        if (note.isEmpty()){
            throw new IllegalStateException("Note not found");
        }

        return note.get();
    }

    public Note createNote(Note note, Long userId){

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()){
            throw new IllegalStateException("User with this id is not exists!");
        }

        note.setUser(userOptional.get());

        return noteRepository.save(note);

    }

    public void deleteNoteById(Long userId, Long id) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()){
            throw new IllegalStateException("User with this id is not exists!");
        }

        noteRepository.deleteById(id);

    }

    @Transactional
    public void updateNote(Long userId, Long id, String tittle, String text) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()){
            throw new IllegalStateException("User with this id is not exists!");
        }

        Optional<Note> noteOptional = noteRepository.findById(id);
        if (noteOptional.isEmpty()){
            throw new IllegalStateException("Note with this id is not exists!");
        }

        Note note = noteOptional.get();

        if (tittle != null){
            note.setTittle(tittle);
        }

        if (text != null){
            note.setText(text);
        }

    }



}
