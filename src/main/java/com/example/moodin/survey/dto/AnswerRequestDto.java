package com.example.moodin.survey.dto;

import com.example.moodin.survey.entity.AnswerEntity;
import com.example.moodin.survey.entity.AnswerOptionEntity;
import com.example.moodin.survey.entity.QuestionEntity;
import com.example.moodin.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerRequestDto {
    private Long userId;
    private Long questionId;
    private Long optionId;

    @Builder
    public AnswerRequestDto(Long userId, Long questionId, Long optionId) {
        this.userId = userId;
        this.questionId = questionId;
        this.optionId = optionId;
    }

    // service 에서 조회한 엔터티 파라미터로 받기
    public AnswerEntity toEntity(UserEntity user, QuestionEntity question, AnswerOptionEntity option) {
        return AnswerEntity.builder()
                .user(user)
                .question(question)
                .answerOption(option)
                .build();
    }
}
