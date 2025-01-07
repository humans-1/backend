package org.example.humans.domain.note.service;

import lombok.RequiredArgsConstructor;
import org.example.humans.domain.note.converter.NoteConverter;
import org.example.humans.domain.note.dto.NoteReqDTO;
import org.example.humans.domain.note.dto.NoteResDTO;
import org.example.humans.domain.note.entity.Note;
import org.example.humans.domain.note.exception.NoteErrorCode;
import org.example.humans.domain.note.exception.NoteException;
import org.example.humans.domain.note.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional //모든 메소드가 하나의 transaction 단위로 동작, 단일 메소드에도 선언 가능
@RequiredArgsConstructor
public class NoteCommandService {
    private final NoteRepository noteRepository;

    public NoteResDTO.NoteDetailsDTO createNote(NoteReqDTO.CreateNoteDTO dto){
        Note newNote = noteRepository.save(NoteConverter.toNote(dto));
        return NoteConverter.toCreateNoteResponseDto(newNote);
    }

    public void deleteNote(Long noteId){
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteException(NoteErrorCode.NOT_FOUND));
        note.softDelete();
    }
}
