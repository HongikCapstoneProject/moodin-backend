package com.example.moodin.stress.entity;

import com.example.moodin.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stress_analysis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StressAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "result_id", columnDefinition = "uuid")
    private UUID resultId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private StressRecord stressRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "eda_stress_level", nullable = false)
    private StressLevel edaStressLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "hrv_stress_level", nullable = false)
    private StressLevel hrvStressLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "total_stress_level", nullable = false)
    private StressLevel totalStressLevel;

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @Column(name = "algorithm_version")
    private String algorithmVersion;

    @Column(name = "analyzed_at", nullable = false)
    private LocalDateTime analyzedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (analyzedAt == null) {
            analyzedAt = LocalDateTime.now();
        }
    }
}
