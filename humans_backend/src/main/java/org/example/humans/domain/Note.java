package org.example.humans.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.humans.dto.NoteDto;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "notes")
public class Note {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long noteId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    @Column(nullable = true)
    private String subject;

    public Note(NoteDto requestDto) {
        this.subject = requestDto.getSubject();
    }
}
