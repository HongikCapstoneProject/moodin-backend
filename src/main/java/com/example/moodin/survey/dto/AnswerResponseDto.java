package com.example.moodin.survey.dto;

import com.example.moodin.survey.entity.AnswerEntity;
import com.example.moodin.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerResponseDto {
    private Long userId;
    private Long questionId;
    private Long optionId;

    @Builder
    public AnswerResponseDto(AnswerEntity answerEntity) {
        this.userId = answerEntity.getUser().getUserId();
        this.questionId = answerEntity.getQuestion().getQuestionId();
        this.optionId = answerEntity.getAnswerOption().getOptionId();
    }
}