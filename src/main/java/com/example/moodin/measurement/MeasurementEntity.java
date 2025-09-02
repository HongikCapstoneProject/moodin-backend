package com.example.moodin.measurement;

import com.example.moodin.stress.StressLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="measurement")
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class MeasurementEntity {

    @Id
    @UuidGenerator
    private UUID id;

    private String userId;         // 필요 없으면 제거 가능(애매하면 우선 문자열)
    private String deviceId;
    private String source;

    @Column(nullable=false)
    private Instant measuredAt;

    // 입력 원시값
    @Column(nullable=false)
    private double hrvSdnnMs;
    @Column(nullable=false)
    private double gsrTonicUs;

    // 판정 결과
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private StressLevel hrvLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private StressLevel gsrLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private StressLevel finalLevel;

    @Column(nullable=false, updatable=false, insertable=false)
    private Instant createdAt;     // DB default now()
}
