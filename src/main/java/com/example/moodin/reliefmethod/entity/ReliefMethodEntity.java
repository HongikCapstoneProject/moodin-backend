package com.example.moodin.reliefmethod.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "stress_relief_method",
        indexes = {
                @Index(name = "idx_method_title", columnList = "method_title")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReliefMethodEntity {
    @Id
    @Column(name = "method_id", nullable = false, updatable = false)
    private UUID methodId;

    @Column(name = "method_title", length = 120, nullable = false)
    private String methodTitle;

    @Column(name = "method_description", nullable = false, columnDefinition = "text")
    private String methodDescription;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
