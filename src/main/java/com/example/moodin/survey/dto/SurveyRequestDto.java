package com.example.moodin.survey.dto;

import com.example.moodin.survey.entity.SurveyEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SurveyRequestDto {
    private String title;
    private String description;

    @Builder
    public SurveyRequestDto(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public SurveyEntity toEntity() {
        return SurveyEntity.builder()
                .title(title)
                .description(description)
                .build();
    }
}
