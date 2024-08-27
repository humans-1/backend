package org.example.humans.repository;

import org.example.humans.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findById(String ID);
    boolean existsById(String ID);
}
