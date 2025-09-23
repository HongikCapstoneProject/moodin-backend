package com.example.moodin.survey.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "survey")
public class SurveyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "survey_id")
    private Long surveyId; // 설문지 아이디

    @Column(length = 500, nullable = false)
    private String title; // 설문지 제목 ex) 스트레스 자가진단

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description; // 설문지 출처

    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY)
    private List<QuestionEntity> questionList = new ArrayList<>();

    @Builder
    public SurveyEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
