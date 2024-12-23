package org.example.humans.domain.note.repository;

import org.aspectj.weaver.ast.Not;
import org.example.humans.domain.note.entity.Note;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Long> {
    List<Note> findByDeletedAtIsNull();

    Slice<Note> findAllByIdLessThanOrderByIdDesc(Long cursor, Pageable pageable);
    Slice<Note> findAllByOrderByIdDesc(Pageable pageable);
}
