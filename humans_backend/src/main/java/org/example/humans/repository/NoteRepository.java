package org.example.humans.repository;

import org.example.humans.domain.Note;
import org.example.humans.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUser(Users user); // 사용자에 따른 모든 노트 조회
    List<Note> findByUserAndSubject(Users user, String subject); // 사용자와 과목으로 노트 조회
    void deleteBySubject(String subject); // 과목으로 노트 삭제
}
