package com.example.moodin.survey.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerUpdateRequestDto {
    private Long optionId;

    @Builder
    public AnswerUpdateRequestDto(Long optionId) {
        this.optionId = optionId;
    }
}
