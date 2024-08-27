package org.example.humans.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.humans.domain.Note;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class NoteDto {
    private String subject; // 노트 제목
    private Long noteId; // 노트 ID

    public NoteDto(Note note) {
        this.noteId = note.getNoteId();
        this.subject = note.getSubject();
    }
}
