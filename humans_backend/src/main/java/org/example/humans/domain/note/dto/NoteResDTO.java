package org.example.humans.domain.note.dto;

import lombok.Builder;
import org.example.humans.domain.onlynote.entity.OnlyNote;

import java.time.LocalDateTime;
import java.util.List;

public class NoteResDTO {
    @Builder
    public record NoteDetailsDTO(
            String name,
            Long id,
            LocalDateTime deletedAt,
            List<OnlyNote> onlyNote
    ){}

    @Builder
    public record NotePagePreviewDTO(
            List<NoteDetailsDTO> noteDetailsDTOList,
            boolean hasNext,
            Long cursor
    ){}

}
