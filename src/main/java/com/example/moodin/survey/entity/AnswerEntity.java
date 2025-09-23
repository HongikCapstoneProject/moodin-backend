package com.example.moodin.survey.entity;

import com.example.moodin.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "answer")
public class AnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "answer_id")
    private Long answerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id",nullable = false)
    private QuestionEntity question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id",nullable = false)
    private AnswerOptionEntity answerOption;


    @Builder
    public AnswerEntity(UserEntity user, QuestionEntity question, AnswerOptionEntity answerOption) {
        this.user = user;
        this.question = question;
        this.answerOption = answerOption;
    }
}
