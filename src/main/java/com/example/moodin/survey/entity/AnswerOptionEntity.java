package com.example.moodin.survey.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "answerOption")
public class AnswerOptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long optionId; // 선택지 보기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quesion_id", nullable = false)
    private QuestionEntity question;

    @Column(nullable = false)
    private String optionContent; // 매우 그렇다, 보통이다 ..

    @Column(nullable = false)
    private Integer score; // 이 선택지를 고르면 부여되는 점수

    @Builder
    AnswerOptionEntity(Long optionId, QuestionEntity question, String optionContent, Integer score) {
        this.optionId = optionId;
        this.question = question;
        this.optionContent = optionContent;
        this.score = score;
    }
}
