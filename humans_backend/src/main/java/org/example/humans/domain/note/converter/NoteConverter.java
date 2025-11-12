package org.example.humans.domain.note.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.humans.domain.note.dto.NoteReqDTO;
import org.example.humans.domain.note.dto.NoteResDTO;
import org.example.humans.domain.note.entity.Note;
import org.example.humans.domain.security.entity.AuthUser;
import org.example.humans.domain.user.entity.User;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoteConverter {
    public static User convertToUser(AuthUser authUser) {
        return User.builder()
                .id(authUser.getId()) // 필요한 필드 매핑
                .build();
    }
    // DTO -> Entity
    public static Note toNote(NoteReqDTO.CreateNoteDTO reqDTO,AuthUser authUser) {
        return Note.builder()
                .name(reqDTO.name())
                .user(convertToUser(authUser))
                .build();
    }


    // Entity -> DTO
    public static NoteResDTO.NoteDetailsDTO toCreateNoteResponseDto(Note note,AuthUser authUser) {
        return NoteResDTO.NoteDetailsDTO.builder()
                .id(note.getId())
                .userId(authUser.getId())
                .name(note.getName())
                .deletedAt(note.getDeletedAt())
                .onlyNote(note.getOnlyNote()) // List<OnlyNote>를 직접 사용
                .build();
    }

    // Entity 리스트 -> DTO 리스트
    public static List<NoteResDTO.NoteDetailsDTO> fromList(List<Note> notes, AuthUser authUser) {
        return notes.stream()
                .map(note->toCreateNoteResponseDto(note,authUser))
                .collect(Collectors.toList());
    }

    public static NoteResDTO.NotePagePreviewDTO toNotePageDTO(Slice<Note> notes, AuthUser authUser){
        List<NoteResDTO.NoteDetailsDTO> noteDetailsDTOList = fromList(notes.getContent(),authUser);
        return NoteResDTO.NotePagePreviewDTO.builder()
                .noteDetailsDTOList(noteDetailsDTOList)
                .hasNext(notes.hasNext())
                .cursor(notes.getContent().get(notes.getContent().size() - 1).getId())
                .build();
    }

}
