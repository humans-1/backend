package org.example.humans.domain.onlynote.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.humans.domain.note.entity.Note;
import org.example.humans.global.entity.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "onlynote")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OnlyNote extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "onlynote_id")
    private Long id;

    @Column(name = "week")
    private String week;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    private Note note;

    // 소프트 삭제 메서드
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    // 복원 메서드
    public void restore() {
        this.deletedAt = null;
    }

}
