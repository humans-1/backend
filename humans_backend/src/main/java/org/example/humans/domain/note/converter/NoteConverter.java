package org.example.humans.domain.note.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.humans.domain.note.dto.NoteReqDTO;
import org.example.humans.domain.note.dto.NoteResDTO;
import org.example.humans.domain.note.entity.Note;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoteConverter {
    // DTO -> Entity
    public static Note toNote(NoteReqDTO.CreateNoteDTO reqDTO) {
        return Note.builder()
                .name(reqDTO.name())
                .build();
    }


    // Entity -> DTO
    public static NoteResDTO.NoteDetailsDTO toCreateNoteResponseDto(Note note) {
        return NoteResDTO.NoteDetailsDTO.builder()
                .id(note.getId())
                .name(note.getName())
                .deletedAt(note.getDeletedAt())
                .onlyNote(note.getOnlyNote()) // List<OnlyNote>를 직접 사용
                .build();
    }

    // Entity 리스트 -> DTO 리스트
    public static List<NoteResDTO.NoteDetailsDTO> fromList(List<Note> notes) {
        return notes.stream()
                .map(NoteConverter::toCreateNoteResponseDto)
                .collect(Collectors.toList());
    }

    public static NoteResDTO.NotePagePreviewDTO toNotePageDTO(Slice<Note> notes){
        List<NoteResDTO.NoteDetailsDTO> noteDetailsDTOList = fromList(notes.getContent());
        return NoteResDTO.NotePagePreviewDTO.builder()
                .noteDetailsDTOList(noteDetailsDTOList)
                .hasNext(notes.hasNext())
                .cursor(notes.getContent().get(notes.getContent().size() - 1).getId())
                .build();
    }

}
