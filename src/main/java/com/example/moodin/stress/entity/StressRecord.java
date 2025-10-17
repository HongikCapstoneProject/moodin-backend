package com.example.moodin.stress.entity;

import com.example.moodin.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stress_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StressRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "record_id", columnDefinition = "uuid")
    private UUID recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "eda_value", nullable = false)
    private Double edaValue;

    @Column(name = "hrv_value", nullable = false)
    private Double hrvValue;

    @Column(name = "measured_at", nullable = false)
    private LocalDateTime measuredAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MeasurementStatus status;

    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // StressAnalysis와 1:1 관계
    @OneToOne(mappedBy = "stressRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private StressAnalysis analysis;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (measuredAt == null) {
            measuredAt = LocalDateTime.now();
        }
        if (status == null) {
            status = MeasurementStatus.PENDING;
        }
    }
}
