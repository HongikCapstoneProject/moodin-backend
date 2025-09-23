package com.example.moodin.survey.dto;

public record SurveyResultDto(Long userId, Long surveyId, int totalScore, String level) {
}
