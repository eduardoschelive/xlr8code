package com.xlr8code.server.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class AuditableEntity {

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    protected Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    protected Instant updatedAt;

}
