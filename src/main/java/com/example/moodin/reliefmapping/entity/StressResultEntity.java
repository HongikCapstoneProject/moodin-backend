package com.example.moodin.reliefmapping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import java.util.UUID;

@Entity @Table(name = "stress_result")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StressResultEntity {
    @Id
    @GeneratedValue(generator="uuid2")
    @GenericGenerator(name="uuid2", strategy="uuid2")
    //@Type(type=UUID)
    @Column(name="result_id", columnDefinition="CHAR(36)")
    private UUID id;

    @Column(nullable=false, length=80)
    private String label; // e.g. LOW / MEDIUM / HIGH
}