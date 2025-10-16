package org.example.humans.domain.user.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.example.humans.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndActiveTrue(String email);
    boolean existsByEmail(String email);

    @Modifying
    @Query("DELETE FROM User u WHERE u.active = false AND u.deletedAt <= :expiryDate")
    void findByDeletedAtBefore(@Param("expiryDate")LocalDateTime expiryDate);

    @Query("SELECT u FROM User u WHERE u.email=:email AND u.active=false AND u.deletedAt > :expiryDate")
    Optional<User> findRestoreUser(@Param("email") String email,
                                   @Param("expiryDate") LocalDateTime expiryDate);
}
