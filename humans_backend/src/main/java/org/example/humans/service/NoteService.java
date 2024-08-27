package org.example.humans.service;

import lombok.AllArgsConstructor;
import org.example.humans.domain.Note;
import org.example.humans.domain.Users;
import org.example.humans.dto.NoteDto;
import org.example.humans.repository.NoteRepository;
import org.example.humans.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    @Transactional
    public NoteDto createSubject(Long userId, NoteDto noteDto) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Note note = new Note();
        note.setUser(user);  // 사용자 설정
        note.setSubject(noteDto.getSubject()); // subject 설정
        noteRepository.save(note);

        return new NoteDto(note); // Note 객체를 사용하여 NoteDto 생성
    }
    @Transactional
    public List<NoteDto> findAllSubject(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<Note> notelist = noteRepository.findByUser(user);
        List<NoteDto> responseDtoList = new ArrayList<>();

        for (Note note : notelist) {
            responseDtoList.add(new NoteDto(note));
        }
        return responseDtoList;
    }

    @Transactional
    public boolean deleteNote(Long userId, String subject) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<Note> notes = noteRepository.findByUserAndSubject(user, subject);
        if (notes.isEmpty()) {
            return false; // 과목이 없음
        }
        noteRepository.deleteAll(notes);
        return true; // 삭제 성공
    }
}
