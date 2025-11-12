package org.example.humans.domain.note.service;

import lombok.RequiredArgsConstructor;
import org.example.humans.domain.note.converter.NoteConverter;
import org.example.humans.domain.note.dto.NoteReqDTO;
import org.example.humans.domain.note.dto.NoteResDTO;
import org.example.humans.domain.note.entity.Note;
import org.example.humans.domain.note.exception.NoteErrorCode;
import org.example.humans.domain.note.exception.NoteException;
import org.example.humans.domain.note.repository.NoteRepository;
import org.example.humans.domain.security.entity.AuthUser;
import org.example.humans.domain.user.exception.UserErrorCode;
import org.example.humans.domain.user.exception.UserException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;

@Service
@Transactional //모든 메소드가 하나의 transaction 단위로 동작, 단일 메소드에도 선언 가능
@RequiredArgsConstructor
public class NoteCommandService {
    private final NoteRepository noteRepository;

    public NoteResDTO.NoteDetailsDTO createNote(NoteReqDTO.CreateNoteDTO dto, AuthUser authUser){
        if (authUser == null) {
            throw new UserException(UserErrorCode.NO_USER_DATA_REGISTERED);
        }
        Note newNote = noteRepository.save(NoteConverter.toNote(dto,authUser));
        return NoteConverter.toCreateNoteResponseDto(newNote,authUser);
    }

    public void deleteNote(Long noteId){
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteException(NoteErrorCode.NOT_FOUND));
        note.softDelete();
    }

    public void restoreNote(Long noteId) {
        Note note = noteRepository.findById(noteId).orElseThrow(() ->
                new NoteException(NoteErrorCode.NOT_FOUND));
        note.restore();
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public String hardDeleteCapsule() {
        // 1. Soft delete된 캡슐
        List<Note> deletedNotes = noteRepository.findAllByDeletedAtIsNotNull();
        if (deletedNotes.isEmpty()) {
            return "삭제할 soft delete 과목이 없습니다.";
        }

        for (Note note : deletedNotes) {
            // 3. 캡슐 삭제
            noteRepository.delete(note); // 하드 삭제
        }

        return "soft delete된 과목이 삭제되었습니다.";
    }
}
