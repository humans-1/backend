package org.example.humans.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.humans.global.entity.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "active", nullable = false)
    private boolean active;

    // 소프트 삭제 메서드
    public void softDelete() {

        this.deletedAt = LocalDateTime.now();
        this.active = false;
    }
}
