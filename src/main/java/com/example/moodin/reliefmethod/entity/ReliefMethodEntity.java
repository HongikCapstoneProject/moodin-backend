package com.example.moodin.reliefmethod.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
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
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    //@Type(type = "uuid-char") // DB에 VARCHAR(36)으로 저장
    @Column(name = "method_id", columnDefinition = "CHAR(36)")
    private UUID methodId;

    @Column(name = "method_title", length = 120, nullable = false)
    private String methodTitle;

    @Column(name = "method_description", nullable = false, columnDefinition = "text")
    private String methodDescription;
}
