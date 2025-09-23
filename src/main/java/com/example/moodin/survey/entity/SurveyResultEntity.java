package com.example.moodin.survey.entity;

import com.example.moodin.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "surveyResult")
public class SurveyResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private SurveyEntity survey;

    @Column(nullable = false)
    private Integer totalScore; // 문항 점수 합산

    @Column(nullable = false)
    private String level; // 높음,낮음 ..

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public SurveyResultEntity(UserEntity user, SurveyEntity survey, Integer totalScore, String level, LocalDateTime createdAt) {
        this.user = user;
        this.survey = survey;
        this.totalScore = totalScore;
        this.level = level;
        this.createdAt = (createdAt != null) ? createdAt : LocalDateTime.now();
    }
}
