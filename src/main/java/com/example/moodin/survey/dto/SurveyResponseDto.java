package com.example.moodin.survey.dto;

import com.example.moodin.survey.entity.SurveyEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SurveyResponseDto {
    private String title;
    private String description;

    // entity -> dto 변환
    @Builder
     public SurveyResponseDto(SurveyEntity surveyEntity) {
        this.title = surveyEntity.getTitle();
        this.description = surveyEntity.getDescription();
    }


}


