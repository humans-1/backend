package org.example.humans.domain.note.repository;

import org.aspectj.weaver.ast.Not;
import org.example.humans.domain.note.entity.Note;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note,Long> {
    Boolean existsByUserIdAndDeletedAtIsNull(Long userId);
    List<Note> findByUserIdAndDeletedAtIsNull(Long userId);
    List<Note> findAllByDeletedAtIsNotNull();
    Optional<Note> findByIdAndDeletedAtIsNull(Long noteId);

    @Query("SELECT n FROM Note n WHERE n.user.id = :userId AND n.deletedAt IS NULL ORDER BY n.id DESC")
    Slice<Note> findFirstPageDesc(Pageable pageable, @Param("userId") Long userId);

    @Query("SELECT n FROM Note n WHERE n.user.id = :userId AND n.deletedAt IS NULL AND n.id < :cursor ORDER BY n.id DESC")
    Slice<Note> findByCursorDesc(@Param("cursor") Long cursor, Pageable pageable, @Param("userId") Long userId);
}
