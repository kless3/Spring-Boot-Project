package com.harmyFounder.SpringBootProject.service;

import com.harmyFounder.SpringBootProject.model.Note;
import com.harmyFounder.SpringBootProject.model.User;
import com.harmyFounder.SpringBootProject.repository.NoteRepository;
import com.harmyFounder.SpringBootProject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class NoteServiceTest {
    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NoteService noteService;

    private User testUser;
    private Note testNote;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);

        testNote = new Note();
        testNote.setId(1L);
        testNote.setUser(testUser);
        testNote.setTittle("Test Title");
        testNote.setText("Test Content");
    }

    @Test
    void getAllNotesByUser_WhenUserExistsAndHasNotes_ReturnsNotes() {
        List<Note> expectedNotes = Collections.singletonList(testNote);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(noteRepository.findAllByUser(testUser)).thenReturn(Optional.of(expectedNotes));

        ResponseEntity<?> response = noteService.getAllNotesByUser(1L);
        List<Note> result = (List<Note>) response.getBody();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testNote, result.get(0));
        verify(userRepository).findById(1L);
        verify(noteRepository).findAllByUser(testUser);
    }

    @Test
    void getAllNotesByUser_WhenUserNotExists_ReturnsNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = noteService.getAllNotesByUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found with id: 1", response.getBody());
        verify(userRepository).findById(1L);
        verifyNoInteractions(noteRepository);
    }

    @Test
    void getAllNotesByUser_WhenUserHasNoNotes_ReturnsNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(noteRepository.findAllByUser(testUser)).thenReturn(Optional.empty());

        ResponseEntity<?> response = noteService.getAllNotesByUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No notes found for user with id: 1", response.getBody());
        verify(userRepository).findById(1L);
        verify(noteRepository).findAllByUser(testUser);
    }
    @Test
    void getNoteById_WhenUserAndNoteExist_ReturnsNote() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(noteRepository.findById(1L)).thenReturn(Optional.of(testNote));

        Note result = noteService.getNoteById(1L, 1L);

        assertNotNull(result);
        assertEquals(testNote, result);
        verify(userRepository).findById(1L);
        verify(noteRepository).findById(1L);
    }

    @Test
    void getNoteById_WhenUserNotExists_ThrowsException() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(IllegalStateException.class, () -> noteService.getNoteById(1L, 1L));
        verify(userRepository).findById(1L);
        verifyNoInteractions(noteRepository);
    }

    @Test
    void getNoteById_WhenNoteNotExists_ThrowsException() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(noteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> noteService.getNoteById(1L, 1L));
        verify(userRepository).findById(1L);
        verify(noteRepository).findById(1L);
    }

    @Test
    void createNote_WhenUserExists_SavesAndReturnsNote() {

        Note newNote = new Note();
        newNote.setTittle("New Note");
        newNote.setText("New Content");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> {
            Note n = invocation.getArgument(0);
            n.setId(2L);
            return n;
        });

        Note result = noteService.createNote(newNote, 1L);

        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        assertEquals("New Note", result.getTittle());
        assertEquals("New Content", result.getText());
        assertNotNull(result.getId());
        verify(userRepository).findById(1L);
        verify(noteRepository).save(newNote);
    }

    @Test
    void createNote_WhenUserNotExists_ThrowsException() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> noteService.createNote(testNote, 1L));
        verify(userRepository).findById(1L);
        verifyNoInteractions(noteRepository);
    }

    @Test
    void deleteNoteById_WhenUserAndNoteExist_DeletesNote() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        noteService.deleteNoteById(1L, 1L);

        verify(userRepository).findById(1L);
        verify(noteRepository).deleteById(1L);
    }

    @Test
    void deleteNoteById_WhenUserNotExists_ThrowsException() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> noteService.deleteNoteById(1L, 1L));
        verify(userRepository).findById(1L);
        verifyNoInteractions(noteRepository);
    }

    @Test
    void updateNote_WhenUserAndNoteExist_UpdatesNote() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(noteRepository.findById(1L)).thenReturn(Optional.of(testNote));

        noteService.updateNote(1L, 1L, "Updated Title", "Updated Content");


        assertEquals("Updated Title", testNote.getTittle());
        assertEquals("Updated Content", testNote.getText());
        verify(userRepository).findById(1L);
        verify(noteRepository).findById(1L);
    }

    @Test
    void updateNote_WhenUserNotExists_ThrowsException() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> noteService.updateNote(1L, 1L, "Title", "Content"));
        verify(userRepository).findById(1L);
        verifyNoInteractions(noteRepository);
    }

    @Test
    void updateNote_WhenNoteNotExists_ThrowsException() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(noteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> noteService.updateNote(1L, 1L, "Title", "Content"));
        verify(userRepository).findById(1L);
        verify(noteRepository).findById(1L);
    }

    @Test
    void updateNote_WhenOnlyTitleProvided_UpdatesOnlyTitle() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(noteRepository.findById(1L)).thenReturn(Optional.of(testNote));

        noteService.updateNote(1L, 1L, "Updated Title", null);

        assertEquals("Updated Title", testNote.getTittle());
        assertEquals("Test Content", testNote.getText()); // Original content unchanged
    }

    @Test
    void updateNote_WhenOnlyContentProvided_UpdatesOnlyContent() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(noteRepository.findById(1L)).thenReturn(Optional.of(testNote));

        noteService.updateNote(1L, 1L, null, "Updated Content");

        assertEquals("Test Title", testNote.getTittle()); // Original title unchanged
        assertEquals("Updated Content", testNote.getText());
    }
}
