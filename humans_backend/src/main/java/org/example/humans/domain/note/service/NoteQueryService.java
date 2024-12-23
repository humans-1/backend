package org.example.humans.domain.note.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.humans.domain.note.converter.NoteConverter;
import org.example.humans.domain.note.dto.NoteResDTO;
import org.example.humans.domain.note.entity.Note;
import org.example.humans.domain.note.exception.NoteErrorCode;
import org.example.humans.domain.note.exception.NoteException;
import org.example.humans.domain.note.repository.NoteRepository;
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

    public List<NoteResDTO.NoteDetailsDTO> getNotes(){
        List<Note> notes = noteRepository.findByDeletedAtIsNull();
        return NoteConverter.fromList(notes);
    }

    public NoteResDTO.NoteDetailsDTO getNote(Long noteId){
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteException(NoteErrorCode.NOT_FOUND));
        return NoteConverter.toCreateNoteResponseDto(note);
    }

    public Slice<Note> getNotesByCursor(Long cursor, Integer offset){
        Pageable pageable = PageRequest.of(0, offset);
        if(cursor.equals(0L)){
            return noteRepository.findAllByOrderByIdDesc(pageable);
        }
        return noteRepository.findAllByIdLessThanOrderByIdDesc(cursor,pageable);

    }
}
