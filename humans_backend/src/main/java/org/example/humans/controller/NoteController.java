package org.example.humans.controller;

import org.example.humans.dto.NoteDto;
import org.example.humans.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/{userId}/note")
    public ResponseEntity<NoteDto> createSubject(@PathVariable Long userId, @RequestBody NoteDto requestDto) {
        NoteDto note = noteService.createSubject(userId, requestDto);
        return new ResponseEntity<>(note, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/note")
    public ResponseEntity<List<NoteDto>> getAllSubjects(@PathVariable Long userId) {
        List<NoteDto> notes = noteService.findAllSubject(userId);
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/note")
    public ResponseEntity<Object> deleteNote(@PathVariable Long userId, @RequestParam String subject) {
        try {
            noteService.deleteNote(userId, subject);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "삭제되었습니다."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "과목이 없습니다."));
        }
    }

}
