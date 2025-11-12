package org.example.humans.domain.note.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.humans.domain.note.converter.NoteConverter;
import org.example.humans.domain.note.dto.NoteResDTO;
import org.example.humans.domain.note.entity.Note;
import org.example.humans.domain.note.exception.NoteErrorCode;
import org.example.humans.domain.note.exception.NoteException;
import org.example.humans.domain.note.repository.NoteRepository;
import org.example.humans.domain.security.entity.AuthUser;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoteQueryService {
    private final NoteRepository noteRepository;

    public List<NoteResDTO.NoteDetailsDTO> getNotes(AuthUser authUser){
        List<Note> notes = noteRepository.findByUserIdAndDeletedAtIsNull(authUser.getId());
        return NoteConverter.fromList(notes,authUser);
    }

    public NoteResDTO.NoteDetailsDTO getNote(Long noteId, AuthUser authUser){
        Note note = noteRepository.findByIdAndDeletedAtIsNull(noteId)
                .orElseThrow(() -> new NoteException(NoteErrorCode.NOT_FOUND));
        return NoteConverter.toCreateNoteResponseDto(note, authUser);
    }

    public Slice<Note> getNotesByCursor(Long cursor, Integer offset, AuthUser authUser){
        Pageable pageable = PageRequest.of(0, offset);
        if(!noteRepository.existsByUserIdAndDeletedAtIsNull(authUser.getId())){
            throw new NoteException(NoteErrorCode.NOT_FOUND);
        }
        if(cursor==0L){
            return noteRepository.findFirstPageDesc(pageable, authUser.getId());
        }else {
            return noteRepository.findByCursorDesc(cursor, pageable, authUser.getId());
        }
    }
}
