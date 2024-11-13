package org.example.humans.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) //엔티티의 변화를 감지하여 엔티티와 매핑된 테이블의 데이터를 조작, AuditingEntityListener: 엔티티의 영속, 수정 이벤트를 감지하는 역할
public abstract class BaseEntity {
    // 해당 Column에 생성시간 자동 mapping
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // 해당 Column에 수정시간 자동 mapping
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

