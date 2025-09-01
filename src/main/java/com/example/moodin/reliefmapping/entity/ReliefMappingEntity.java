package com.example.moodin.reliefmapping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import java.util.UUID;

@Entity
@Table(
        name = "relief_mapping",
        uniqueConstraints = @UniqueConstraint(name="uk_result_method", columnNames={"result_id","method_id"})
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReliefMappingEntity {
    @Id
    @GeneratedValue(generator="uuid2")
    @GenericGenerator(name="uuid2", strategy="uuid2")
    //@Type(type="uuid-char")
    @Column(columnDefinition="CHAR(36)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="result_id", nullable=false)
    private StressResultEntity result;

    //@Type(type="uuid-char")
    @Column(name="method_id", nullable=false, columnDefinition="CHAR(36)")
    private UUID methodId;  // 프론트가 가진 해소법의 ID
}
