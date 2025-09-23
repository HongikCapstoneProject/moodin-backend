package com.example.moodin.survey.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "qeustion")
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId; // 설문 문항

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private SurveyEntity survey; // 설문지

    @Column(length = 50, nullable = false)
    private String questionContent; // 설문 문항 내용

    @Column
    private String type; // 보기 선택

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<AnswerOptionEntity> answerOptionList; //

    @Builder
    public QuestionEntity(SurveyEntity survey, String questionContent, String type) {
        this.survey = survey;
        this.questionContent = questionContent;
        this.type = type;
    }
}
